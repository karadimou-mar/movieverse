package com.example.movieverse.util

import com.example.movieverse.model.cast.CastResponse
import com.example.movieverse.model.cast.CrewResponse
import com.example.movieverse.model.movie.MovieResponse

fun List<CrewResponse>.toDirectors(): String {
    return this
        .filter { it.job == "Director" }
        .joinToString(separator = ", ") { directors -> directors.name }
}

fun List<CrewResponse>.toWriters(): String {
    return this
        .filter { it.job == "Writer" }
        .joinToString(separator = ", ") { directors -> directors.name }
}

fun List<MovieResponse>.toProducers(): List<MovieResponse> {
    return this
        .filter { it.job!!.contains("producer", true) }
}

fun List<CastResponse>.sortByOrder() =
    this.sortedBy {
        it.order
    }

fun List<MovieResponse>.sortByPopularity() =
    this.sortedByDescending {
        it.popularity
    }
