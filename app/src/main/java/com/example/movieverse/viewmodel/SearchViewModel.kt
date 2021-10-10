package com.example.movieverse.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieverse.db.getMovieDatabase
import com.example.movieverse.model.movie.MovieResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.repo.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar
    private val _showProgressBar = MutableLiveData<Boolean>()

    val searchListResult: LiveData<List<MovieResponse>>
        get() = _searchListResult
    private val _searchListResult = MutableLiveData<List<MovieResponse>>()

    val upcomingListResult: LiveData<List<MovieResponse>>
        get() = _upcomingListResult
    private val _upcomingListResult = MutableLiveData<List<MovieResponse>>()

    internal fun searchMovie(query: String) {
        viewModelScope.launch {
            when (val search = searchRepository.searchMovie(query)) {
                is NetworkResponse.Success -> {
                    val movies = search.body.results
                    _searchListResult.value = movies
                    for (m in movies.indices) {
                        Log.d(TAG, "Search: Success: ${movies[m]}")
                    }
                    Log.d(TAG, "Search: total results: ${movies.size}")
                }
                is NetworkResponse.ApiError -> {
                    Log.d(
                        TAG,
                        "Search: ApiError: statusCode: ${search.body.statusCode} , statusMsg: ${search.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    Log.d(TAG, "Search: NetworkError: ${search.error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    Log.d(TAG, "UnknownError: ${search.error?.message}")
                }
            }
            _showProgressBar.value = false
        }
    }

    internal fun getUpcomingMovies() {
        viewModelScope.launch {
            when (val search = searchRepository.getUpcomingMovies()) {
                is NetworkResponse.Success -> {
                    val movies = search.body.results
                    _upcomingListResult.value = movies
                    for (m in movies.indices) {
                        Log.d(TAG, "Upcoming: Success: ${movies[m]}")
                    }
                    Log.d(TAG, "Upcoming: total results: ${movies.size}")
                }
                is NetworkResponse.ApiError -> {
                    Log.d(
                        TAG,
                        "Upcoming: ApiError: statusCode: ${search.body.statusCode} , statusMsg: ${search.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    Log.d(TAG, "Upcoming: NetworkError: ${search.error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    Log.d(TAG, "Upcoming: UnknownError: ${search.error?.message}")
                }
            }
            _showProgressBar.value = false
        }
    }

    companion object {
        private val TAG = SearchViewModel::class.java.simpleName
    }
}

fun defaultSearchViewModelFactory(context: Context) = factory {
    val db = getMovieDatabase(context)
    val repository = SearchRepository(db.movieDao)
    SearchViewModel(repository)
}