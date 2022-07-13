package com.example.movieverse.model.movie

import android.os.Parcelable
import com.example.movieverse.db.MovieInDB
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieResponse(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "poster_path")
    val posterPath: String?,
    val overview: String,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "genre_ids")
    val genreIds: List<Int>,
    val title: String,
    val popularity: Double,
    @Json(name = "video")
    val hasVideo: Boolean,
    @Json(name = "vote_average")
    val voteAverage: Double,
    val job: String?
) : Parcelable

fun MovieResponse.toMovieInDb() = MovieInDB(
    id ?: -1,
    title,
    posterPath ?: "",
    overview,
    voteAverage,
    releaseDate,
    genreIds,
    hasVideo,
    popularity,
    job ?: ""
)

fun MovieInDB.toMovieResponse() = MovieResponse(
    id = id,
    title = title,
    posterPath = posterPath,
    overview = overview,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
    genreIds = genresId,
    hasVideo = hasVideos,
    popularity = popularity,
    job = job
)
