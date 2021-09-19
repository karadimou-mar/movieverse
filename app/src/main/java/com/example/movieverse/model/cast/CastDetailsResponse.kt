package com.example.movieverse.model.cast

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class CastDetailsResponse(
    val birthday: String,
    @Json(name = "known_for_department")
    val department: String,
    @Json(name = "deathday")
    val deathDay: String?,
    val name: String,
    @Json(name = "also_known_as")
    val knownAs: List<String>,
    val gender: Int,
    val biography: String,
    @Json(name = "profile_path")
    val profilePath: String,
    @Json(name = "imdb_id")
    val imdbId: String
): Parcelable