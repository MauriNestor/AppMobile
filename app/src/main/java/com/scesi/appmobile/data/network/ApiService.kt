package com.scesi.appmobile.data.network

import com.scesi.appmobile.BuildConfig
import com.scesi.appmobile.data.model.MovieDetailResponsive
import com.scesi.appmobile.data.model.MovieResponse
import com.scesi.appmobile.data.model.MovieVideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/{category}")
    suspend fun getMovies(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): MovieResponse
    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): MovieDetailResponsive
    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): MovieVideoResponse
}
