package com.example.movieverse.model.cast

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreditsResponse(
    val cast: List<CastResponse>,
    val crew: List<CrewResponse>
) : Parcelable

@Parcelize
data class CastResponse(
    @Json(name = "id")
    val personId: Int?,
    @Json(name = "cast_id")
    val castId: Int?,
    @Json(name = "known_for_department")
    val department: String,
    val name: String,
    @Json(name = "profile_path")
    val profilePath: String?,
    val character: String,
    val popularity: Double
) : Parcelable

@Parcelize
data class CrewResponse(
    @Json(name = "id")
    val personId: Int?,
    val department: String,
    val name: String,
    @Json(name = "profile_path")
    val profilePath: String?,
    val popularity: Double,
    val job: String
) : Parcelable