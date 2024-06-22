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

    fun fetchNowPlayingMovies(apiKey: String) {
        viewModelScope.launch {
            val response = repository.getNowPlayingMovies(apiKey)
            _movies.postValue(response.results)
        }
    }
}
