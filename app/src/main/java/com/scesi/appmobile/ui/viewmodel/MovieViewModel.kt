package com.scesi.appmobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scesi.appmobile.data.model.MovieResponse
import com.scesi.appmobile.data.model.MovieRepository

class MovieViewModel (private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<MovieResponse>>()
    val movies: LiveData<List<MovieResponse>> get() = _movies

    fun fetchPopularMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(apiKey)
                _movies.postValue(response.results)
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
}