package com.scesi.appmobile.data.model

import com.scesi.appmobile.network.ApiService

class MovieRepository(private val apiService: ApiService) {

    suspend fun getNowPlayingMovies(apiKey: String): MovieResponse {
        return apiService.getNowPlayingMovies(apiKey)
    }
}
