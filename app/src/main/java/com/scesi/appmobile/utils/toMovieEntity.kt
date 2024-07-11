package com.scesi.appmobile.utils

import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.model.Result

fun Result.toMovieEntity(category: String): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        posterPath = poster_path,
        voteAverage = vote_average,
        overview = overview,
        category = category,
        isFavorite = false,
        lastUpdated = System.currentTimeMillis() // Set the current time as the last updated time
    )
}
