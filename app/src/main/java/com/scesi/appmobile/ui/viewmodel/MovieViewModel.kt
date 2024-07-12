package com.scesi.appmobile.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.repository.MovieRepository
import kotlinx.coroutines.launch
import java.io.IOException

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val moviesMap = mutableMapOf<String, MutableLiveData<List<MovieEntity>>>()
    private val currentPageMap = mutableMapOf<String, Int>()
    private val isLoadingMap = mutableMapOf<String, Boolean>()

    private val _favoriteMovies = MutableLiveData<List<MovieEntity>>()
    val favoriteMovies: LiveData<List<MovieEntity>> = _favoriteMovies

    fun getMoviesLiveData(endpoint: String): MutableLiveData<List<MovieEntity>> {
        return moviesMap.getOrPut(endpoint) { MutableLiveData<List<MovieEntity>>() }
    }

    fun getMovies(endpoint: String) {
        if (isLoadingMap[endpoint] == true) return

        val liveData = getMoviesLiveData(endpoint)
        val currentPage = currentPageMap[endpoint] ?: 1

        viewModelScope.launch {
            isLoadingMap[endpoint] = true
            try {
                val movies = repository.getMovies(endpoint, currentPage)
                liveData.postValue(movies)
                Log.d("MovieViewModel", "Fetched ${movies.size} movies for endpoint $endpoint")
            } catch (e: IOException) {
                Log.e("MovieViewModel", "Error fetching movies for endpoint $endpoint: ${e.message}")
                // Handle error loading data
            }
            isLoadingMap[endpoint] = false
        }
    }

    fun loadNextPage(endpoint: String) {
        if (isLoadingMap[endpoint] == true) return

        val liveData = getMoviesLiveData(endpoint)
        val nextPage = (currentPageMap[endpoint] ?: 1) + 1

        viewModelScope.launch {
            isLoadingMap[endpoint] = true
            try {
                val newMovies = repository.getMovies(endpoint, nextPage)
                val currentMovies = liveData.value.orEmpty().toMutableList()
                currentMovies.addAll(newMovies.distinctBy { it.id })
                liveData.postValue(currentMovies)
                currentPageMap[endpoint] = nextPage
                Log.d("MovieViewModel", "Loaded next page $nextPage for endpoint $endpoint")
            } catch (e: IOException) {
                Log.e("MovieViewModel", "Error loading next page for endpoint $endpoint: ${e.message}")
                // Handle error loading next page from API
            }
            isLoadingMap[endpoint] = false
        }
    }

    fun getFavoriteMovies() {
        viewModelScope.launch {
            val favorites = repository.getFavoriteMovies()
            _favoriteMovies.postValue(favorites)
            Log.d("MovieViewModel", "Fetched ${favorites.size} favorite movies")
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
