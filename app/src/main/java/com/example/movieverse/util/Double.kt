package com.example.movieverse.util

fun Double?.valueOrEmpty(): String = when (this) {
    -1.0 -> ""
    else -> this.toString()
}