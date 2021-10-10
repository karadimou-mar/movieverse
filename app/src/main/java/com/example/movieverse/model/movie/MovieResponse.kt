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
//    @Json(name = "adult")
//    val isAdultMovie: Boolean,
    val overview: String,
    @Json(name = "release_date")
    val releaseDate: String?,
//    @Json(name = "genre_ids")
//    val genreIds: List<Int>,
    val title: String,
//    @Json(name = "backdrop_path")
//    val backDropPath: String?,
//    val popularity: Double,
//    @Json(name = "vote_count")
//    val voteCount: Int,
    @Json(name = "video")
    val hasVideo: Boolean,
    @Json(name = "vote_average")
    val voteAverage: Double
): Parcelable

fun MovieResponse.toMovieInDb() = MovieInDB(
    id!!,
    title,
    posterPath,
    overview,
    voteAverage,
    releaseDate!!
)

fun MovieInDB.toMovieResponse() = MovieResponse(
    id = id,
    title = title,
    posterPath = posterPath,
    overview = overview,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
    hasVideo = false
)


