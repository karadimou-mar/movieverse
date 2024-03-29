package com.example.movieverse.viewmodel

import android.content.Context
import android.util.Log
import android.widget.TableRow
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

    val shouldShowConnectionError: LiveData<Boolean>
        get() = _shouldShowConnectionError
    private val _shouldShowConnectionError = MutableLiveData<Boolean>()

    val searchListResult: LiveData<List<MovieResponse>>
        get() = _searchListResult
    private val _searchListResult = MutableLiveData<List<MovieResponse>>()

    val upcomingListResult: LiveData<List<MovieResponse>>
        get() = _upcomingListResult
    private val _upcomingListResult = MutableLiveData<List<MovieResponse>>()


    var isFavMovie = false

    internal fun searchMovie(query: String) {
        viewModelScope.launch {
            when (val search = searchRepository.searchMovie(query)) {
                is NetworkResponse.Success -> {
                    val movies = search.body.results
                    _searchListResult.value = movies
                    _shouldShowConnectionError.value = false
                }
                is NetworkResponse.ApiError -> {
                    Log.d(
                        TAG,
                        "Search: ApiError: statusCode: ${search.body.statusCode} , statusMsg: ${search.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    _shouldShowConnectionError.value = true
                    Log.d(TAG, "Search: NetworkError: ${search.error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    _shouldShowConnectionError.value = false
                    Log.d(TAG, "UnknownError: ${search.error?.message}")
                }
            }
            _showProgressBar.value = false
        }
    }

    internal fun getUpcomingMovies() {
        viewModelScope.launch {
            try {
                when (val search = searchRepository.getUpcomingMovies()) {
                    is NetworkResponse.Success -> {
                        val movies = search.body.results
                        _upcomingListResult.value = movies
                        for (m in movies.indices) {
                            Log.d(TAG, "Upcoming: Success: ${movies[m].id}")
                            //_isFavMovie.value = movies[m].id?.let { searchRepository.isFavMovie(it) } == true
                        }
                        Log.d(TAG, "Upcoming: total results: ${movies.size}")
                        _shouldShowConnectionError.value = false
                    }
                    is NetworkResponse.ApiError -> {
                        Log.d(
                            TAG,
                            "Upcoming: ApiError: statusCode: ${search.body.statusCode} , statusMsg: ${search.body.statusMsg}"
                        )
                    }
                    is NetworkResponse.NetworkError -> {
                        _shouldShowConnectionError.value = true
                        Log.d(TAG, "Upcoming: NetworkError: ${search.error.message}")
                    }
                    is NetworkResponse.UnknownError -> {
                        _shouldShowConnectionError.value = false
                        Log.d(TAG, "Upcoming: UnknownError: ${search.error?.message}")
                    }
                }
                _showProgressBar.value = false
            }catch (error: Throwable) {
                Log.e(TAG, "Error on isFavMovie() from local db")
            }
        }
    }

    fun isFavMovie(movieId: Int): Boolean {
        viewModelScope.launch {
            try {
               movieId.let {
                    isFavMovie = searchRepository.isFavMovie(it)
                }
            } catch (error: Throwable) {
                Log.e(TAG, "isFavMovie: Cannot get movie by id", error)
            }
        }
        return isFavMovie
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