package com.scesi.appmobile.utils

import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.data.model.Result

fun Result.toMovieEntity(category: String): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterPath = this.poster_path,
        voteAverage = this.vote_average,
        category = category
    )
}
