package com.example.movieverse.repo

import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.search.SearchResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.net.search.getSearchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: maybe we could pass the DAO in the constructor later on
class SearchRepository() {

    suspend fun searchMovie(query: String): NetworkResponse<SearchResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val searchService = getSearchService()
            searchService.searchMovie(query = query)
        }
}