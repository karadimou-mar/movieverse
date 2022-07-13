package com.example.movieverse.net

import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.search.CinemaResponse
import com.example.movieverse.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header

private lateinit var INSTANCE: CinemaService

fun getCinemaService(): CinemaService =
    synchronized(MovieService::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = createRetrofitService(Constants.BASE_URL2)
        }
        return INSTANCE
    }

interface CinemaService {

    @GET("cinemasNearby/?n=10")
    suspend fun getCinemasNearby(
        @Header("api-version") version: String = "v200",
        @Header("Authorization") auth: String = "Basic TUFSSV8wX1hYOmYycU9IR05iYTdtSQ==",
        @Header("client") client: String = "MARI_0",
        @Header("x-api-key") apikey: String = "G5yU53b2VlPAvlql2Mkq8RJ3ZqhXlo82iVMG2m10",
        @Header("territory") territory: String = "XX",
        @Header("geolocation") coords: String = "-22.0;14.0",
        @Header("device-datetime") datetime: String = "2022-08-17T12:07:57.296Z",
    ): NetworkResponse<CinemaResponse, ErrorResponse>
}