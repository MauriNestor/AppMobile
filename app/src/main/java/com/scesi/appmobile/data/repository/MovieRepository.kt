package com.scesi.appmobile.data.repository

import android.util.Log
import com.scesi.appmobile.data.local.dao.MovieDao
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.model.MovieDetailResponsive
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
                val newMovies = response.results.map {
                    it.toMovieEntity(category, currentTime)
                }
                if (page == 1) {
                    // Obtener el estado de favoritos antes de limpiar
                    val existingMovies = movieDao.getMoviesByCategory(category)
                    val favoriteMoviesMap = existingMovies.filter { it.isFavorite }.associateBy { it.id }

                    // Actualizar el estado de favoritos en las nuevas películas
                    val updatedMovies = newMovies.map { movie ->
                        val isFavorite = favoriteMoviesMap[movie.id]?.isFavorite ?: false
                        movie.copy(isFavorite = isFavorite)
                    }

                    movieDao.clearMoviesByCategory(category)
                    movieDao.insertMovies(updatedMovies)
                } else {
                    movieDao.insertMovies(newMovies)
                }
                return@withContext newMovies
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

    suspend fun getMovieDetails(movieId: Int): MovieDetailResponsive {
        return apiService.getMovieDetail(movieId)
    }

    suspend fun getMovieById(movieId: Int): MovieEntity? {
        return withContext(Dispatchers.IO) {
            val movieDetailResponse = apiService.getMovieDetail(movieId)
            movieDetailResponse.toMovieEntity("category_placeholder", System.currentTimeMillis())
        }
    }
}
