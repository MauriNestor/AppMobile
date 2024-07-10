package com.scesi.appmobile.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.repository.MovieRepository
import kotlinx.coroutines.launch
import java.io.IOException

class MovieViewModel private constructor(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<MovieEntity>>()
    val movies: LiveData<List<MovieEntity>> = _movies

    private val _favoriteMovies = MutableLiveData<List<MovieEntity>>()
    val favoriteMovies: LiveData<List<MovieEntity>> = _favoriteMovies

    private val currentMovies = mutableListOf<MovieEntity>()
    private var currentPage = 1

    fun getMovies(endpoint: String) {
        viewModelScope.launch {
            try {
                val newMovies = repository.getMoviesFromApi(endpoint, currentPage)
                currentMovies.addAll(newMovies.distinctBy { it.id })
                _movies.value = currentMovies
            } catch (e: IOException) {
                val moviesFromDb = repository.getMoviesFromDatabase(endpoint)
                _movies.postValue(moviesFromDb)
            }
        }
    }

    fun loadNextPage(endpoint: String) {
        viewModelScope.launch {
            try {
                currentPage++
                val newMovies = repository.getMoviesFromApi(endpoint, currentPage)
                currentMovies.addAll(newMovies.distinctBy { it.id })
                _movies.postValue(currentMovies)
            } catch (e: IOException) {
                val moviesFromDb = repository.getMoviesFromDatabase(endpoint)
                _movies.postValue(moviesFromDb)
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
        @Volatile
        private var INSTANCE: MovieViewModel? = null

        fun getInstance(repository: MovieRepository): MovieViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieViewModel(repository).also { INSTANCE = it }
            }
        }
    }
}
