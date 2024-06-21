package com.scesi.appmobile.model

class MovieRepository(private val apiService: ApiService) {

    suspend fun getPopularMovies(apiKey: String): MovieResponse {
        return apiService.getPopularMovies(apiKey)
    }
}
