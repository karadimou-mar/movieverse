package com.example.movieverse.repo

import com.example.movieverse.db.MovieDao
import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.GenreResponse
import com.example.movieverse.model.cast.CastDetailsResponse
import com.example.movieverse.model.cast.PersonMoviesResponse
import com.example.movieverse.model.movie.*
import com.example.movieverse.model.search.SearchResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.net.search.getMovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: split repositories
class SearchRepository(private val movieDao: MovieDao) {

    suspend fun searchMovie(query: String): NetworkResponse<SearchResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val movieService = getMovieService()
            movieService.searchMovie(query = query)
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

    suspend fun getImdbId(movieId: Int): NetworkResponse<MovieImdbIdResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val movieService = getMovieService()
            movieService.getImdbId(movieId)
        }

    suspend fun getMovieCast(movieId: Int): NetworkResponse<CreditsResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val movieService = getMovieService()
            movieService.getMovieCast(movieId)
        }

    suspend fun getCastDetailsById(personId: Int): NetworkResponse<CastDetailsResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val movieService = getMovieService()
            movieService.getCastDetailsById(personId)
        }

    suspend fun getPersonMoviesById(personId: Int): NetworkResponse<PersonMoviesResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val movieService = getMovieService()
            movieService.getPersonMoviesById(personId)
        }

    //room db
    suspend fun storeMovie(movieResponse: MovieResponse): Long =
        withContext(Dispatchers.IO) {
            movieDao.insertMovie(movieResponse.toMovieInDb())
        }

    suspend fun getMoviesList(): List<MovieResponse> =
        withContext(Dispatchers.IO) {
            movieDao.getMoviesList().map {
                it.toMovieResponse()
            }
        }
}