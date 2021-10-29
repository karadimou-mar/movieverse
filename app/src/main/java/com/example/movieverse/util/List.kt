package com.example.movieverse.util

import com.example.movieverse.model.cast.CrewResponse

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
