package com.example.movieverse.util

import java.time.LocalDate

fun String?.valueOrEmpty(): String = when {
    isNullOrBlank() -> ""
    else -> this
}

fun String?.valueOrDashes(): String = when {
    isNullOrBlank() -> "--"
    else -> this
}

// todo: double check
fun String?.toLocalDate(): LocalDate? {
    return if (this != null) {
        LocalDate.parse(this)
    } else {
        null
    }
}

