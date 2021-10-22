package com.example.movieverse.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreResponse(
    @Json(name = "genres")
    val genres: List<Genre>
): Parcelable

@Parcelize
data class Genre(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String?
): Parcelable