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
    private val currentMovies = mutableListOf<Result>()

    fun getMovies(category: String, page: Int = 1) {
        viewModelScope.launch {
            val movieResponse = repository.getMovies(category, page)
            if (page == 1) {
                currentMovies.clear()
            }
            currentMovies.addAll(movieResponse.results)
            _movies.value = currentMovies
        }
    }
    fun loadNextPage(category: String) {
        currentPage++
        getMovies(category, currentPage)
    }
}
