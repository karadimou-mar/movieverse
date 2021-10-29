package com.example.movieverse.model.movie

import android.os.Parcelable
import com.example.movieverse.model.cast.CreditsResponse
import com.example.movieverse.model.search.SearchResponse
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailsResponse(
    val id: Int,
    val overview: String,
    val runtime: Int,
    val videos: MovieVideoResponse?,
    @Json(name = "external_ids")
    val externalIds: MovieImdbIdResponse?,
    val credits: CreditsResponse?,
    val recommendations: SearchResponse?
) : Parcelable