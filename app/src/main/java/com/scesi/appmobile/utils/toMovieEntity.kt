package com.scesi.appmobile.utils

import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.model.Result

fun Result.toMovieEntity(category: String): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        posterPath = this.poster_path,
        voteAverage = this.vote_average,
        overview = this.overview,
        category = category,
        lastUpdated = System.currentTimeMillis()
    )
}
