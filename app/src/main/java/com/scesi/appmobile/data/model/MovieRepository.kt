package com.scesi.appmobile.data.model

import com.scesi.appmobile.network.ApiService

class MovieRepository(private val apiService: ApiService) {

    suspend fun getCarteleraMovies(): MovieResponse {
        return apiService.getCarteleraMovies()
    }
    suspend fun getPopularMovies(): MovieResponse {
        return apiService.getPopularMovies()
    }
}

