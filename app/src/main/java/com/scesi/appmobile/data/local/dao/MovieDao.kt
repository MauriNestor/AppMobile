package com.scesi.appmobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scesi.appmobile.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies WHERE category = :category")
    suspend fun getMoviesByCategory(category: String): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    suspend fun getFavoriteMovies(): List<MovieEntity>

    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean)

    @Query("DELETE FROM movies WHERE category = :category")
    suspend fun clearMoviesByCategory(category: String)

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?
}
