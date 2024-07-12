package com.scesi.appmobile.data.repository

import android.util.Log
import com.scesi.appmobile.data.local.MovieDao
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.network.ApiService
import com.scesi.appmobile.utils.toMovieEntity

class MovieRepository(private val movieDao: MovieDao, private val apiService: ApiService) {

    suspend fun getMovies(category: String, page: Int = 1): List<MovieEntity> {
        val cachedMovies = movieDao.getMoviesByCategory(category)
        if (cachedMovies.isNotEmpty() && page == 1) {
            return cachedMovies
        }

        val response = apiService.getMovies(category, page)
        val currentTime = System.currentTimeMillis()
        val movies = response.results.map {
            it.toMovieEntity(category).copy(lastUpdated = currentTime)
        }
        if (page == 1) {
            movieDao.insertMovies(movies)
        }
        return movies
    }

    suspend fun loadNextPage(category: String, page: Int): List<MovieEntity> {
        val response = apiService.getMovies(category, page)
        val currentTime = System.currentTimeMillis()
        val movies = response.results.map {
            it.toMovieEntity(category).copy(lastUpdated = currentTime)
        }
        movieDao.insertMovies(movies)
        return movies
    }

    suspend fun getFavoriteMovies(): List<MovieEntity> {
        return movieDao.getFavoriteMovies()
    }

    suspend fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean) {
        movieDao.updateFavoriteStatus(movieId, isFavorite)
    }
}
