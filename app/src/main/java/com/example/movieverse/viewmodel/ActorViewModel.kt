package com.example.movieverse.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieverse.model.movie.CreditsResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.repo.SearchRepository
import kotlinx.coroutines.launch

class ActorViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    // TODO: abstract progressbar logic
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar
    private val _showProgressBar = MutableLiveData<Boolean>()

    val castResult: LiveData<CreditsResponse>
        get() = _castResult
    private val _castResult = MutableLiveData<CreditsResponse>()


    internal fun getMovieCast(movieId: Int) {
        viewModelScope.launch {
            when (val cast = searchRepository.getMovieCast(movieId)) {
                is NetworkResponse.Success -> {
                    Log.d(TAG, "MovieDetails: Success: ${cast.body}")
                    _castResult.value = cast.body
                }
                is NetworkResponse.ApiError -> {
                    Log.d(
                        TAG,
                        "Cast: ApiError: statusCode: ${cast.body.statusCode} , statusMsg: ${cast.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    Log.d(TAG, "Cast: NetworkError: ${cast.error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    Log.d(TAG, "Cast: UnknownError: ${cast.error?.message}")
                }
            }
            _showProgressBar.value = false
        }
    }

    companion object {
        private val TAG = ActorViewModel::class.java.simpleName
    }
}

fun defaultActorViewModelFactory(context: Context) = factory {
    val repository = SearchRepository()
    ActorViewModel(repository)
}