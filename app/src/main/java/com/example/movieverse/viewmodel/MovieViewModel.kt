package com.example.movieverse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.GenreResponse
import com.example.movieverse.model.movie.MovieDetailsResponse
import com.example.movieverse.model.search.SearchResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.repo.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    // TODO: change types??
    val searchMovieResult: LiveData<NetworkResponse<SearchResponse, ErrorResponse>>
        get() = _searchMovieResult
    private val _searchMovieResult = MutableLiveData<NetworkResponse<SearchResponse, ErrorResponse>>()

    val popularMoviesResult: LiveData<NetworkResponse<SearchResponse, ErrorResponse>>
        get() = _popularMoviesResult
    private val _popularMoviesResult = MutableLiveData<NetworkResponse<SearchResponse, ErrorResponse>>()

    val topRatedMoviesResult: LiveData<NetworkResponse<SearchResponse, ErrorResponse>>
        get() = _topRatedMoviesResult
    private val _topRatedMoviesResult = MutableLiveData<NetworkResponse<SearchResponse, ErrorResponse>>()

    val upcomingMoviesResult: LiveData<NetworkResponse<SearchResponse, ErrorResponse>>
        get() = _upcomingMoviesResult
    private val _upcomingMoviesResult = MutableLiveData<NetworkResponse<SearchResponse, ErrorResponse>>()

    val moviesGenreResult: LiveData<NetworkResponse<GenreResponse, ErrorResponse>>
        get() = _moviesGenreResult
    private val _moviesGenreResult = MutableLiveData<NetworkResponse<GenreResponse, ErrorResponse>>()

    val movieDetailsResult: LiveData<NetworkResponse<MovieDetailsResponse, ErrorResponse>>
        get() = _movieDetailsResult
    private val _movieDetailsResult = MutableLiveData<NetworkResponse<MovieDetailsResponse, ErrorResponse>>()

    internal fun searchMovie(query: String) {
        viewModelScope.launch {
            val search = movieRepository.searchMovie(query)
            _searchMovieResult.value = search
        }
    }

    internal fun getPopularMovies() {
        viewModelScope.launch {
            val search = movieRepository.getPopularMovies()
            _popularMoviesResult.value = search
        }
    }

    internal fun getTopRatedMovies() {
        viewModelScope.launch {
            val search = movieRepository.getTopRatedMovies()
            _topRatedMoviesResult.value = search
        }
    }

    internal fun getUpcomingMovies() {
        viewModelScope.launch {
            val search = movieRepository.getUpcomingMovies()
            _upcomingMoviesResult.value = search
        }
    }

    internal fun getMoviesGenres() {
        viewModelScope.launch {
            val search = movieRepository.getMoviesGenres()
            _moviesGenreResult.value = search
        }
    }

    internal fun getMovieDetailsById(movieId: Int) {
        viewModelScope.launch {
            val details = movieRepository.getMovieDetailsById(movieId)
            _movieDetailsResult.value = details
        }
    }
}

fun defaultMovieViewModelFactory(context: Context) = factory {
    val repository = MovieRepository()
    SearchViewModel(repository)
}