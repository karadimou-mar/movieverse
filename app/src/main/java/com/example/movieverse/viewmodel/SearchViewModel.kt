package com.example.movieverse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.search.SearchResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.net.search.SearchService
import com.example.movieverse.repo.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    val searchMovieResult: LiveData<NetworkResponse<SearchResponse, ErrorResponse>>
        get() = _searchMovieResult
    private val _searchMovieResult = MutableLiveData<NetworkResponse<SearchResponse, ErrorResponse>>()

    internal fun searchMovie(query: String, searchClient: SearchService) {
        viewModelScope.launch {
            val search = searchRepository.searchMovie(query, searchClient)
            _searchMovieResult.value = search
        }
    }
}

fun defaultSearchViewModelFactory() = factory {
    val repository = SearchRepository()
    SearchViewModel(repository)
}