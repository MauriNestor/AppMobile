package com.scesi.appmobile.data.repository

import com.scesi.appmobile.data.network.ApiService

class MovieRepository(private val apiService: ApiService) {
    suspend fun getMovies(category: String, page: Int) = apiService.getMovies(category, page)
}
