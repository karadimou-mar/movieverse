package com.example.movieverse.util

fun String?.valueOrEmpty(): String = when {
    isNullOrBlank() -> ""
    else -> this
}

fun String?.valueOrDashes(): String = when {
    isNullOrBlank() -> "--"
    else -> this
}