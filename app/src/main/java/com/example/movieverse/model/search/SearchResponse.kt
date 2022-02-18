package com.example.movieverse.model.search

import android.os.Parcelable
import com.example.movieverse.model.movie.MovieResponse
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(
    val page: Int,
    val results: List<MovieResponse>,
    @Json(name = "total_results")
    val totalResults: Int,
    @Json(name = "total_pages")
    val totalPages: Int
): Parcelable

@Parcelize
data class CinemaResponse(
    val cinemas: List<Cinema>
): Parcelable

@Parcelize
data class Cinema(
    val cinema_id: Int
): Parcelable