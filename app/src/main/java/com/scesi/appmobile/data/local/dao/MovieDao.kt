package com.scesi.appmobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.local.entity.FavoriteEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies WHERE category = :category")
    suspend fun getMoviesByCategory(category: String): List<MovieEntity>

    @Query("DELETE FROM movies WHERE category = :category")
    suspend fun clearMoviesByCategory(category: String)

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE movieId = :movieId")
    suspend fun deleteFavorite(movieId: Int)

    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    suspend fun getAllFavorites(): List<FavoriteEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE movieId = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean
}
