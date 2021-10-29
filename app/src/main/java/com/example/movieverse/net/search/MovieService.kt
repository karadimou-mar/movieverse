package com.example.movieverse.net.search

import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.GenreResponse
import com.example.movieverse.model.cast.CastDetailsResponse
import com.example.movieverse.model.movie.MovieDetailsResponse
import com.example.movieverse.model.search.SearchResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.net.createRetrofitService
import com.example.movieverse.util.Constants.API_KEY
import com.example.movieverse.util.Constants.BASE_URL
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private lateinit var INSTANCE: MovieService

fun getMovieService(): MovieService =
    synchronized(MovieService::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = createRetrofitService(BASE_URL)
        }
        return INSTANCE
    }

interface MovieService {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): NetworkResponse<SearchResponse, ErrorResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): NetworkResponse<SearchResponse, ErrorResponse>

    @GET("genre/movie/list")
    suspend fun getMoviesGenres(
        @Query("api_key") apiKey: String = API_KEY,
    ): NetworkResponse<GenreResponse, ErrorResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetailsById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("append_to_response") append: String = "videos,external_ids,credits,recommendations"
    ): NetworkResponse<MovieDetailsResponse, ErrorResponse>

    @GET("person/{person_id}")
    suspend fun getCastDetailsById(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("append_to_response") append: String = "movie_credits"
    ): NetworkResponse<CastDetailsResponse, ErrorResponse>
}