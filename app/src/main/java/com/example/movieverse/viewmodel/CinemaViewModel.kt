package com.example.movieverse.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieverse.model.search.Cinema
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.repo.CinemaRepository
import kotlinx.coroutines.launch

class CinemaViewModel(
    private val cinemaRepository: CinemaRepository
) : ViewModel() {

    val cinemaResult: LiveData<List<Cinema>>
        get() = _cinemaResult
    private val _cinemaResult = MutableLiveData<List<Cinema>>()

    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar
    private val _showProgressBar = MutableLiveData<Boolean>()

    val shouldShowConnectionError: LiveData<Boolean>
        get() = _shouldShowConnectionError
    private val _shouldShowConnectionError = MutableLiveData<Boolean>()

    internal fun getCinemasNearby() {
        viewModelScope.launch {
            when (val search = cinemaRepository.getCinemasNearby()) {
                is NetworkResponse.Success -> {
                    val cinemas = search.body.cinemas
                    for (m in cinemas.indices) {
                        Log.d(TAG, "Cinemas: Success: ${cinemas[m]}")
                    }
                    _cinemaResult.value = cinemas
                    _shouldShowConnectionError.value = false
                    _showProgressBar.value = false
                }
                is NetworkResponse.ApiError -> {
                    Log.d(
                        TAG,
                        "Cinemas: ApiError: statusCode: ${search.body.statusCode} , statusMsg: ${search.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    _shouldShowConnectionError.value = true
                    Log.d(TAG, "Cinemas: NetworkError: ${search.error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    _shouldShowConnectionError.value = false
                    Log.d(TAG, "Cinemas: UnknownError: ${search.error?.message}")
                }
            }
            _showProgressBar.value = false
        }
    }

    companion object {
        private val TAG = CinemaViewModel::class.java.simpleName
    }
}

fun defaultCinemaViewModelFactory() = factory {
    val repository = CinemaRepository()
    CinemaViewModel(repository)
}