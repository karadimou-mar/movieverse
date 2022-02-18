package com.example.movieverse.util

fun Int?.valueOrEmpty(): String = when (this) {
    -1 -> ""
    else -> this.toString()
}

fun Int?.toHoursMinutes(): String {
    val hours = this?.div(60)
    val min = this?.mod(60)
    return "${hours}h ${min}min"
}

fun Int?.toGender(): String {
    return when (this) {
         1 -> "as actress"
         2 -> "as actor"
        else -> "acting"
    }
}

fun Int.mapGenres(): String {
    return when (this) {
        28 -> "Action"
        12 -> "Adventure"
        99 -> "Documentary"
        18 -> "Drama"
        35 -> "Comedy"
        80 -> "Crime"
        10751 -> "Family"
        16 -> "Animation"
        14 -> "Fantasy"
        36 -> "History"
        27 -> "Horror"
        10402 -> "Music"
        9648 -> "Mystery"
        10749 -> "Romance"
        878 -> "Science Fiction"
        10770 -> "TV Movie"
        53 -> "Thriller"
        10752 -> "War"
        37 -> "Western"
        else -> ""
    }
}