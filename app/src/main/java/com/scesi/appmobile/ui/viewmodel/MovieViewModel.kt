package com.scesi.appmobile.ui.viewmodel

import androidx.lifecycle.*
import com.scesi.appmobile.data.model.MovieRepository
import com.scesi.appmobile.data.model.Result
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Result>>()
    val movies: LiveData<List<Result>> get() = _movies

    private var currentPage = 1
    private var isLoading = false

    fun getNowPlayingMovies() {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            try {
                val response = repository.getNowPlayingMovies(currentPage)
                val updatedMovies = _movies.value.orEmpty() + response.results
                _movies.value = updatedMovies
                currentPage++
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }
}

