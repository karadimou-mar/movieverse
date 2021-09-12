package com.example.movieverse.model.movie

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailsResponse(
    val id: Int,
    val overview: String,
    val cast: List<Cast>?
): Parcelable

@Parcelize
data class Cast(
    @Json(name = "know_for_department")
    val department: String,
    val name: String,
    @Json(name = "profile_path")
    val profilePath: String?,
    val character: String
): Parcelable