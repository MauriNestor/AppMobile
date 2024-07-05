package com.scesi.appmobile.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.repository.MovieRepository
import kotlinx.coroutines.launch
import java.io.IOException

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<MovieEntity>>()
    val movies: LiveData<List<MovieEntity>> get() = _movies

    private var page = 1

    fun getMovies(category: String) {
        viewModelScope.launch {
            try {
                val moviesFromRepo = repository.getMoviesFromApi(category, page)
                _movies.postValue(moviesFromRepo)
            } catch (e: IOException) {
                val moviesFromDb = repository.getMoviesFromDatabase(category)
                _movies.postValue(moviesFromDb)
            }
        }
    }

    fun loadNextPage(category: String) {
        viewModelScope.launch {
            try {
                val newMovies = repository.getMoviesFromApi(category, page)
                val currentMovies = _movies.value.orEmpty().toMutableList()
                currentMovies.addAll(newMovies)
                _movies.postValue(currentMovies)
                page++
            } catch (e: IOException) {
                val moviesFromDb = repository.getMoviesFromDatabase(category)
                _movies.postValue(moviesFromDb)
            }
        }
    }
}
