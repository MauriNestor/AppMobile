package com.scesi.appmobile.network

import com.scesi.appmobile.data.model.MovieResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("now_playing")
    suspend fun getCarteleraMovies(): MovieResponse

    @GET("popular")
    suspend fun getPopularMovies(): MovieResponse

}
