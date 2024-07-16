package com.scesi.appmobile.data.repository

import android.util.Log
import com.scesi.appmobile.data.local.MovieDao
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.network.ApiService
import com.scesi.appmobile.utils.toMovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class MovieRepository(private val movieDao: MovieDao, private val apiService: ApiService) {

    suspend fun getMovies(category: String, page: Int = 1): List<MovieEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMovies(category, page)
                val currentTime = System.currentTimeMillis()
                val movies = response.results.map {
                    it.toMovieEntity(category).copy(lastUpdated = currentTime)
                }
                if (page == 1) {
                    movieDao.clearMoviesByCategory(category)
                }
                movieDao.insertMovies(movies)
                return@withContext movies
            } catch (e: IOException) {
                Log.e("MovieRepository", "Error fetching movies: ${e.message}")
                return@withContext movieDao.getMoviesByCategory(category)
            }
        }
    }

    suspend fun getMoviesFromDatabase(category: String): List<MovieEntity> {
        return movieDao.getMoviesByCategory(category)
    }

    suspend fun getFavoriteMovies(): List<MovieEntity> {
        return movieDao.getFavoriteMovies()
    }

    suspend fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean) {
        movieDao.updateFavoriteStatus(movieId, isFavorite)
    }
}
