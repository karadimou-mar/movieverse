package com.example.movieverse.model.movie

import com.squareup.moshi.Json

data class MovieImdbIdResponse (
    @Json(name = "imdb_id")
    val imdbID: String
)
