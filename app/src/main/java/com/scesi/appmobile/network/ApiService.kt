package com.scesi.appmobile.network

import com.scesi.appmobile.core.Constantes
import com.scesi.appmobile.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/{category}")
    suspend fun getMovies(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = Constantes.API_KEY
    ): MovieResponse
}
