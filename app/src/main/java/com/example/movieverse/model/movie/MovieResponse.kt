package com.example.movieverse.model.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieResponse(
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "adult")
    val isAdultMovie: Boolean,
    val overview: String,
    @Json(name = "release_date")
    val releaseDate: String,
    @Json(name = "genre_ids")
    val genreIds: List<Int>,
    @Json(name = "original_title")
    val originalTitle: String,
    @Json(name = "original_language")
    val originalLang: String,
    val title: String,
    @Json(name = "backdrop_path")
    val backDropPath: String?,
    val popularity: Double,
    @Json(name = "vote_count")
    val voteCount: Int,
    @Json(name = "video")
    val hasVideo: Boolean,
    @Json(name = "vote_average")
    val voteAverage: Double
): Parcelable


