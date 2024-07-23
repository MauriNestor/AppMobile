package com.scesi.appmobile.data.repository

import android.util.Log
import com.scesi.appmobile.data.local.dao.MovieDao
import com.scesi.appmobile.data.local.entity.FavoriteEntity
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.model.MovieDetailResponsive
import com.scesi.appmobile.data.model.MovieVideoResponse
import com.scesi.appmobile.data.network.ApiService
import com.scesi.appmobile.domain.model.MovieVideo
import com.scesi.appmobile.utils.toMovieEntity
import com.scesi.appmobile.utils.toDomainModel
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
                    movieDao.clearMoviesByCategory(category)
                }
                movieDao.insertMovies(newMovies)
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
        val favoriteEntities = movieDao.getAllFavorites()
        val favoriteMovieIds = favoriteEntities.map { it.movieId }
        return favoriteMovieIds.mapNotNull { movieDao.getMovieById(it) }
    }

    suspend fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean) {
        if (isFavorite) {
            movieDao.insertFavorite(FavoriteEntity(movieId))
        } else {
            movieDao.deleteFavorite(movieId)
        }
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetailResponsive {
        return apiService.getMovieDetail(movieId)
    }

    suspend fun getMovieById(movieId: Int): MovieEntity? {
        return movieDao.getMovieById(movieId)
    }

    suspend fun insertMovie(movie: MovieEntity) {
        movieDao.insertMovies(listOf(movie))
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return movieDao.isFavorite(movieId)
    }
    suspend fun getMovieVideos(movieId: Int): MovieVideoResponse {
        return apiService.getMovieVideos(movieId)
    }
}
