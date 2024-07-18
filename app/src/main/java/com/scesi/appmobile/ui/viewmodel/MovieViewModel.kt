package com.scesi.appmobile.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scesi.appmobile.data.repository.MovieRepository
import com.scesi.appmobile.domain.model.Movie
import com.scesi.appmobile.utils.NetworkUtils
import com.scesi.appmobile.utils.toDomainModel
import kotlinx.coroutines.launch
import java.io.IOException

class MovieViewModel(private val repository: MovieRepository, private val context: Context) : ViewModel() {

    private val moviesMap = mutableMapOf<String, MutableLiveData<List<Movie>>>()
    private val currentPageMap = mutableMapOf<String, Int>()
    private val isLoadingMap = mutableMapOf<String, Boolean>()

    private val _favoriteMovies = MutableLiveData<List<Movie>>()
    val favoriteMovies: LiveData<List<Movie>> = _favoriteMovies

    fun getMoviesLiveData(endpoint: String): MutableLiveData<List<Movie>> {
        return moviesMap.getOrPut(endpoint) { MutableLiveData<List<Movie>>() }
    }

    fun getMovies(endpoint: String) {
        val liveData = getMoviesLiveData(endpoint)
        val currentPage = currentPageMap[endpoint] ?: 1

        if (isLoadingMap[endpoint] == true) return

        viewModelScope.launch {
            isLoadingMap[endpoint] = true
            try {
                val movies = if (NetworkUtils.isOnline(context)) {
                    repository.getMovies(endpoint, currentPage).map { it.toDomainModel() }
                } else {
                    repository.getMoviesFromDatabase(endpoint).map { it.toDomainModel() }
                }
                liveData.postValue(movies)
            } catch (e: IOException) {
                liveData.postValue(repository.getMoviesFromDatabase(endpoint).map { it.toDomainModel() })
            }
            isLoadingMap[endpoint] = false
        }
    }

    fun loadNextPage(endpoint: String) {
        if (isLoadingMap[endpoint] == true || !NetworkUtils.isOnline(context)) return

        val liveData = getMoviesLiveData(endpoint)
        val nextPage = (currentPageMap[endpoint] ?: 1) + 1

        viewModelScope.launch {
            isLoadingMap[endpoint] = true
            try {
                val newMovies = repository.getMovies(endpoint, nextPage).map { it.toDomainModel() }
                val currentMovies = liveData.value.orEmpty().toMutableList()
                currentMovies.addAll(newMovies.distinctBy { it.id })
                liveData.postValue(currentMovies)
                currentPageMap[endpoint] = nextPage
            } catch (e: IOException) {
                // Handle error loading next page from API
            }
            isLoadingMap[endpoint] = false
        }
    }

    fun getFavoriteMovies() {
        viewModelScope.launch {
            val favorites = repository.getFavoriteMovies().map { it.toDomainModel() }
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

        fun getInstance(repository: MovieRepository, context: Context): MovieViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieViewModel(repository, context).also { INSTANCE = it }
            }
        }
    }
}
