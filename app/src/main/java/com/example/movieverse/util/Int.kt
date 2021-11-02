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