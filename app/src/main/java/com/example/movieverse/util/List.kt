package com.example.movieverse.util

import com.example.movieverse.model.cast.CastResponse
import com.example.movieverse.model.cast.CrewResponse
import com.example.movieverse.model.movie.MovieResponse
import java.time.LocalDate

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

fun List<Int>?.toGenres(): String? {
   return if (this?.isNotEmpty() == true) {
        this
            .firstNotNullOf { genreId -> genreId.mapGenres() }
    } else {
        return ""
    }
}

fun List<CastResponse>.sortByOrder() =
    this.sortedBy {
        it.order
    }

fun List<MovieResponse>.sortByPopularity() =
    this.sortedByDescending {
        it.popularity
    }
