package com.example.movieverse.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieverse.db.MovieInDB
import com.example.movieverse.db.getMovieDatabase
import com.example.movieverse.model.Genre
import com.example.movieverse.model.movie.MovieDetailsResponse
import com.example.movieverse.model.movie.MovieResponse
import com.example.movieverse.model.movie.MovieVideoResponse
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

    val imdbIdResult: LiveData<String>
        get() = _imdbIdResult
    private val _imdbIdResult =
        MutableLiveData<String>()

    val movieVideosResult: LiveData<MovieVideoResponse>
        get() = _movieVideosResult
    private val _movieVideosResult =
        MutableLiveData<MovieVideoResponse>()

    val moviesInDb: LiveData<MutableList<MovieInDB>>
        get() = _moviesInDb
    private val _moviesInDb =
        MutableLiveData<MutableList<MovieInDB>>()

    val movieRemoved: LiveData<MovieInDB>
        get() = _movieRemoved
    private val _movieRemoved = MutableLiveData<MovieInDB>()

    fun getMoviesGenres() {
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

    fun getMovieDetailsById(movieId: Int) {
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

    fun getImdbId(movieId: Int) {
        viewModelScope.launch {
            when (val details = searchRepository.getImdbId(movieId)) {
                is NetworkResponse.Success -> {
                    _imdbIdResult.value = details.body.imdbID
                    Log.d(TAG, "Imdb: Success: ${details.body.imdbID}")
                }
                is NetworkResponse.ApiError -> {
                    Log.d(
                        TAG,
                        "Imdb: ApiError: statusCode: ${details.body.statusCode} , statusMsg: ${details.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    Log.d(TAG, "Imdb: NetworkError: ${details.error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    Log.d(TAG, "Imdb: UnknownError: ${details.error?.message}")
                }
            }
        }
    }

    fun clearImdbId() {
        _imdbIdResult.value = ""
    }

    fun getMovieVideo(movieId: Int) {
        viewModelScope.launch {
            when (val videos = searchRepository.getMovieVideo(movieId)) {
                is NetworkResponse.Success -> {
                    _movieVideosResult.value = videos.body
                    Log.d(TAG, "MovieVideo: Success: ${videos.body}")
                }
                is NetworkResponse.ApiError -> {
                    Log.d(
                        TAG,
                        "MovieVideo: ApiError: statusCode: ${videos.body.statusCode} , statusMsg: ${videos.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    Log.d(TAG, "MovieVideo: NetworkError: ${videos.error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    Log.d(TAG, "MovieVideo: UnknownError: ${videos.error?.message}")
                }
            }
        }
    }

    fun storeMovie(movie: MovieResponse) {
        viewModelScope.launch {
            searchRepository.storeMovie(movie)
        }
    }

    fun getMoviesFromDb(): List<MovieInDB>? {
        _movieRemoved.value = null
        viewModelScope.launch {
            try {
                val movies = searchRepository.getMoviesFromDb()
                _moviesInDb.value = movies.toMutableList()
            } catch (error: Throwable) {
                Log.e(TAG, "getMoviesList: Cannot get movies from db", error)
                _moviesInDb.value = mutableListOf()
            }
        }
        return _moviesInDb.value
    }

    fun deleteMovieAtPosition(position: Int) {
        viewModelScope.launch {
            try {
                val movies = moviesInDb.value
                movies?.forEachIndexed { index, movieInDB ->
                    if (index == position) {
                        searchRepository.deleteMovie(movieInDB)
                        _moviesInDb.value?.removeAt(index)
                        _movieRemoved.value = movieInDB
                    }
                }
                _moviesInDb.value = movies?.toMutableList()

            } catch (error: Throwable) {
                _movieRemoved.value = null
                Log.e(TAG, "getMoviesList: Cannot delete movie from db", error)
            }
        }
    }

    companion object {
        private val TAG = SearchViewModel::class.java.simpleName
    }
}

fun defaultMovieViewModelFactory(context: Context) = factory {
    val db = getMovieDatabase(context)
    val repository = SearchRepository(db.movieDao)
    MovieViewModel(repository)
}