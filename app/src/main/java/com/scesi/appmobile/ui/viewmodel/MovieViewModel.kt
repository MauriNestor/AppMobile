package com.scesi.appmobile.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scesi.appmobile.data.model.MovieRepository
import com.scesi.appmobile.data.model.Result
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movies = MutableLiveData<List<Result>>()
    val movies: LiveData<List<Result>> get() = _movies

    private var currentPage = 1

    fun getMovies(category: String) {
        viewModelScope.launch {
            val movieResponse = repository.getMovies(category, currentPage)
            _movies.value = movieResponse.results
        }
    }
}
