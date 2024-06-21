package com.scesi.appmobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scesi.appmobile.data.model.MovieResponse
import com.scesi.appmobile.data.model.Result
import com.scesi.appmobile.data.model.MovieRepository

class MovieViewModel (private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Result>>()
    val movies: LiveData<List<Result>> get() = _movies

    fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies()
                _movies.postValue(response.results)
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
}