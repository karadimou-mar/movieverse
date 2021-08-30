package com.example.movieverse.net.search

import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.search.SearchResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.net.createRetrofitService
import com.example.movieverse.util.Constants.API_KEY
import com.example.movieverse.util.Constants.BASE_URL
import retrofit2.http.GET
import retrofit2.http.Query

private lateinit var INSTANCE: SearchService

fun getSearchService(): SearchService =
    synchronized(SearchService::class) {
       if (!::INSTANCE.isInitialized) {
           INSTANCE = createRetrofitService(BASE_URL)
       }
        return INSTANCE
    }

interface SearchService {

    @GET("3/search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): NetworkResponse<SearchResponse, ErrorResponse>
}