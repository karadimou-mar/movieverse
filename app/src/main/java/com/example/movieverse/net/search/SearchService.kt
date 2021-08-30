package com.example.movieverse.net.search

import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.search.SearchResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.net.createRetrofitService
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/"
private lateinit var INSTANCE: SearchService

fun getSearchService(): SearchService =
    synchronized(SearchService::class) {
       if (!::INSTANCE.isInitialized) {
           INSTANCE = createRetrofitService(BASE_URL)
       }
        return INSTANCE
    }

interface SearchService {

    @GET("3/search/movie?api_key=be50dba59668df4bfe6966ad9edcc025")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): NetworkResponse<SearchResponse, ErrorResponse>
}