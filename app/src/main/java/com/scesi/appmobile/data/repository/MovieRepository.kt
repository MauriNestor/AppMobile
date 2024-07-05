package com.scesi.appmobile.data.repository

import com.scesi.appmobile.data.local.MovieDao
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.network.ApiService
import com.scesi.appmobile.utils.toMovieEntity

class MovieRepository(private val apiService: ApiService, private val movieDao: MovieDao) {

    suspend fun getMoviesFromApi(category: String, page: Int): List<MovieEntity> {
        val response = apiService.getMovies(category, page)
        val movies = response.results.map { it.toMovieEntity(category) }
        movieDao.insertMovies(movies)
        return movies
    }

    suspend fun getMoviesFromDatabase(category: String): List<MovieEntity> {
        return movieDao.getMoviesByCategory(category)
    }

    suspend fun getMovies(category: String, page: Int): List<MovieEntity> {
        val moviesFromDb = getMoviesFromDatabase(category)
        return if (moviesFromDb.isEmpty()) {
            getMoviesFromApi(category, page)
        } else {
            moviesFromDb
        }
    }
}
