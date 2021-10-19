package com.example.movieverse.model.movie

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieVideoResponse(
    val id: Int,
    val results: List<VideoResults>
): Parcelable

@Parcelize
data class VideoResults(
    val name: String,
    val key: String?,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    @Json(name = "published_at")
    val publishedAt: String,
    val id: String
): Parcelable
