package com.scesi.appmobile.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.repository.MovieRepository
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.concurrent.TimeUnit

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val moviesMap = mutableMapOf<String, MutableLiveData<List<MovieEntity>>>()
    private val currentPageMap = mutableMapOf<String, Int>()
    private val cacheExpirationTime = TimeUnit.HOURS.toMillis(1) // 1 hour cache expiration

    private val _favoriteMovies = MutableLiveData<List<MovieEntity>>()
    val favoriteMovies: LiveData<List<MovieEntity>> = _favoriteMovies

    fun getMoviesLiveData(endpoint: String): MutableLiveData<List<MovieEntity>> {
        return moviesMap.getOrPut(endpoint) { MutableLiveData<List<MovieEntity>>() }
    }

    fun getMovies(endpoint: String) {
        val liveData = getMoviesLiveData(endpoint)
        val currentPage = currentPageMap[endpoint] ?: 1

        viewModelScope.launch {
            try {
                val moviesFromDb = repository.getMoviesFromDatabase(endpoint)
                val currentTime = System.currentTimeMillis()

                if (moviesFromDb.isNotEmpty() && currentTime - moviesFromDb.first().lastUpdated < cacheExpirationTime) {
                    liveData.postValue(moviesFromDb)
                } else {
                    val newMovies = repository.getMoviesFromApi(endpoint, currentPage)
                    liveData.postValue(newMovies)
                }
            } catch (e: IOException) {
                val moviesFromDb = repository.getMoviesFromDatabase(endpoint)
                liveData.postValue(moviesFromDb)
            }
        }
    }

    fun loadNextPage(endpoint: String) {
        val liveData = getMoviesLiveData(endpoint)
        val nextPage = (currentPageMap[endpoint] ?: 1) + 1

        viewModelScope.launch {
            try {
                currentPageMap[endpoint] = nextPage
                val newMovies = repository.getMoviesFromApi(endpoint, nextPage)
                val currentMovies = liveData.value.orEmpty().toMutableList()
                currentMovies.addAll(newMovies.distinctBy { it.id })
                liveData.postValue(currentMovies)
            } catch (e: IOException) {
                val moviesFromDb = repository.getMoviesFromDatabase(endpoint)
                liveData.postValue(moviesFromDb)
            }
        }
    }

    fun getFavoriteMovies() {
        viewModelScope.launch {
            val favorites = repository.getFavoriteMovies()
            _favoriteMovies.postValue(favorites)
        }
    }

    fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(movieId, isFavorite)
            getFavoriteMovies()
        }
    }

    companion object {
        @Volatile private var INSTANCE: MovieViewModel? = null

        fun getInstance(repository: MovieRepository): MovieViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieViewModel(repository).also { INSTANCE = it }
            }
        }
    }
}
