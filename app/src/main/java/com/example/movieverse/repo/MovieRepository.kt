package com.example.movieverse.repo

import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.search.SearchResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.net.search.getMovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: maybe we could pass the DAO in the constructor later on
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
}