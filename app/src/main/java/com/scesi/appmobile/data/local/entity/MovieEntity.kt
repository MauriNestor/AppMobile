package com.scesi.appmobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
    val overview: String,
    val isFavorite: Boolean,
    val releaseDate: String,
    val popularity: Double,
    val category: String,
    val status: String,
    val language: String,
    val budget: Int,
    val revenue: Int,
    val runtime: Int,
    val lastUpdated: Long
)
