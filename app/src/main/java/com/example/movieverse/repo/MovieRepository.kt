package com.example.movieverse.repo

import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.GenreResponse
import com.example.movieverse.model.movie.MovieDetailsResponse
import com.example.movieverse.model.search.SearchResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.net.search.getMovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository() {

    suspend fun searchMovie(query: String): NetworkResponse<SearchResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val movieService = getMovieService()
            movieService.searchMovie(query = query)
        }

    suspend fun getPopularMovies(): NetworkResponse<SearchResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val movieService = getMovieService()
            movieService.getPopularMovies()
        }

    suspend fun getTopRatedMovies(): NetworkResponse<SearchResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val movieService = getMovieService()
            movieService.getTopRatedMovies()
        }

    suspend fun getUpcomingMovies(): NetworkResponse<SearchResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val movieService = getMovieService()
            movieService.getUpcomingMovies()
        }

    suspend fun getMoviesGenres(): NetworkResponse<GenreResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val movieService = getMovieService()
            movieService.getMoviesGenres()
        }

    suspend fun getMovieDetailsById(movieId: Int): NetworkResponse<MovieDetailsResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val movieService = getMovieService()
            movieService.getMovieDetailsById(movieId)
        }
}