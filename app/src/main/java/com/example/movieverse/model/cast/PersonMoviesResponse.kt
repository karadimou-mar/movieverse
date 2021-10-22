package com.example.movieverse.model.cast

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonMoviesResponse(
    val cast: List<Cast>
): Parcelable

@Parcelize
data class Cast(
    @Json(name = "id")
    val personId: Int?,
    val character: String,
    val title: String,
    @Json(name = "poster_path")
    val posterPath: String?
): Parcelable