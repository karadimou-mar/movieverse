package com.example.movieverse.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieverse.db.getMovieDatabase
import com.example.movieverse.model.cast.CastDetailsResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.repo.SearchRepository
import kotlinx.coroutines.launch

class CastViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    // TODO: abstract progressbar logic
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar
    private val _showProgressBar = MutableLiveData<Boolean>()

    val castDetailsResult: LiveData<CastDetailsResponse>
        get() = _castDetailsResult
    private val _castDetailsResult = MutableLiveData<CastDetailsResponse>()

    internal fun getCastDetailsById(personId: Int) {
        viewModelScope.launch {
            when (val person = searchRepository.getCastDetailsById(personId)) {
                is NetworkResponse.Success -> {
                    Log.d(TAG, "CastDetails: Success: ${person.body}")
                    _castDetailsResult.value = person.body
                }
                is NetworkResponse.ApiError -> {
                    Log.d(
                        TAG,
                        "CastDetails: ApiError: statusCode: ${person.body.statusCode} , statusMsg: ${person.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    Log.d(TAG, "CastDetails: NetworkError: ${person.error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    Log.d(TAG, "CastDetails: UnknownError: ${person.error?.message}")
                }
            }
            _showProgressBar.value = false
        }
    }

    companion object {
        private val TAG = CastViewModel::class.java.simpleName
    }
}

fun defaultCastViewModelFactory(context: Context) = factory {
    val db = getMovieDatabase(context)
    val repository = SearchRepository(db.movieDao)
    CastViewModel(repository)
}