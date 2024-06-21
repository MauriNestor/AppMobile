package com.scesi.appmobile.data.model

import com.scesi.appmobile.network.ApiService

class MovieRepository(private val apiService: ApiService) {

    suspend fun getPopularMovies(apiKey: String): MovieResponse {
        return apiService.getPopularMovies(apiKey)
    }
}
