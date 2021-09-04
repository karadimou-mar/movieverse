package com.example.movieverse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.search.SearchResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.repo.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

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
}

fun defaultMovieViewModelFactory() = factory {
    val repository = MovieRepository()
    SearchViewModel(repository)
}