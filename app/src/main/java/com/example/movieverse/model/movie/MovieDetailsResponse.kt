package com.example.movieverse.model.movie

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailsResponse(
    @Json(name = "overview")
    val overview: String
): Parcelable