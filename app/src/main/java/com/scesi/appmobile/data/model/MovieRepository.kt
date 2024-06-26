package com.scesi.appmobile.data.model

import com.scesi.appmobile.network.ApiService

class MovieRepository(private val apiService: ApiService) {
    suspend fun getMovies(category: String, page: Int) = apiService.getMovies(category, page)
}
