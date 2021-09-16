package com.example.movieverse.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieverse.model.Genre
import com.example.movieverse.model.movie.MovieDetailsResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.repo.SearchRepository
import kotlinx.coroutines.launch

class MovieViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar
    private val _showProgressBar = MutableLiveData<Boolean>()

    val moviesGenreResult: LiveData<List<Genre>>
        get() = _moviesGenreResult
    private val _moviesGenreResult =
        MutableLiveData<List<Genre>>()

    val movieDetailsResult: LiveData<MovieDetailsResponse>
        get() = _movieDetailsResult
    private val _movieDetailsResult =
        MutableLiveData<MovieDetailsResponse>()

    val isDetailsShown: LiveData<Boolean>
        get() = _isDetailsShown
    private val _isDetailsShown = MutableLiveData<Boolean>(false)

    val isBackFromDetails: LiveData<Boolean>
        get() = _isBackFromDetails
    private val _isBackFromDetails = MutableLiveData(false)

    internal fun getMoviesGenres() {
        viewModelScope.launch {
            when (val response = searchRepository.getMoviesGenres()) {
                is NetworkResponse.Success -> {
                    val genres = response.body.genres
                    _moviesGenreResult.value = genres
                    Log.d(TAG, "Genres: Success: $genres")
                }
                is NetworkResponse.ApiError -> {
                    Log.d(
                        TAG,
                        "Genre: ApiError: statusCode: ${response.body.statusCode} , statusMsg: ${response.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    Log.d(TAG, "Genre: NetworkError: ${response.error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    Log.d(TAG, "Genre: UnknownError:  ${response.error?.message}")
                }
            }
        }
    }

    internal fun getMovieDetailsById(movieId: Int) {
        viewModelScope.launch {
            when (val details = searchRepository.getMovieDetailsById(movieId)) {
                is NetworkResponse.Success -> {
                    _movieDetailsResult.value = details.body
                    Log.d(TAG, "MovieDetails: Success: ${details.body}")
                }
                is NetworkResponse.ApiError -> {
                    Log.d(
                        TAG,
                        "MovieDetails: ApiError: statusCode: ${details.body.statusCode} , statusMsg: ${details.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    Log.d(TAG, "MovieDetails: NetworkError: ${details.error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    Log.d(TAG, "MovieDetails: UnknownError: ${details.error?.message}")
                }
            }
            _showProgressBar.value = false
        }
    }

    internal fun isBackFromDetails(bool: Boolean) {
        _isBackFromDetails.value = bool
    }

    companion object {
        private val TAG = SearchViewModel::class.java.simpleName
    }
}

fun defaultMovieViewModelFactory(context: Context) = factory {
    val repository = SearchRepository()
    MovieViewModel(repository)
}