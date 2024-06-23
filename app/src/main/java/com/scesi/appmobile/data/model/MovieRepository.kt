package com.scesi.appmobile.data.model

import com.scesi.appmobile.core.Constantes
import com.scesi.appmobile.network.ApiService

class MovieRepository(private val apiService: ApiService) {

    suspend fun getNowPlayingMovies(page: Int): MovieResponse {
        return apiService.getNowPlayingMovies(Constantes.API_KEY, page = page)
    }
}

