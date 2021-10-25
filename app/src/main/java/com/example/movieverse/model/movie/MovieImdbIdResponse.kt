package com.example.movieverse.model.movie

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieImdbIdResponse(
    @Json(name = "imdb_id")
    val imdbID: String
) : Parcelable
