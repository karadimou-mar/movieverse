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
import com.example.movieverse.model.cast.CastResponse
import com.example.movieverse.model.cast.CrewResponse
import com.example.movieverse.model.movie.*
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

    val movieId: LiveData<String>
        get() = _movieId
    private val _movieId =
        MutableLiveData<String>()

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

    val castResult: LiveData<List<CastResponse>>
        get() = _castResult
    private val _castResult = MutableLiveData<List<CastResponse>>()

    val crewResult: LiveData<List<CrewResponse>>
        get() = _crewResult
    private val _crewResult = MutableLiveData<List<CrewResponse>>()

    val recomResult: LiveData<List<MovieResponse>>
        get() = _recomResult
    private val _recomResult = MutableLiveData<List<MovieResponse>>()

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
                    // youtube video
                    if (_movieDetailsResult.value!!.videos?.results?.isNotEmpty() == true
                        && _movieDetailsResult.value!!.videos?.results?.get(0)?.official == true
                    ) {
                        _movieId.value = _movieDetailsResult.value!!.videos?.results?.get(0)?.key
                    } else {
                        _movieId.value = ""
                    }

                    _imdbIdResult.value = details.body.externalIds?.imdbID
                    _crewResult.value = details.body.credits?.crew
                    _castResult.value = details.body.credits?.cast
                    _recomResult.value = details.body.recommendations?.results
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

    fun clearImdbId() {
        _imdbIdResult.value = ""
    }

    fun clearVideoId() {
        _movieId.value = ""
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