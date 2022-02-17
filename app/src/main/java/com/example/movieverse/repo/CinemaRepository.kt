package com.example.movieverse.repo

import com.example.movieverse.model.ErrorResponse
import com.example.movieverse.model.search.CinemaResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.net.getCinemaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CinemaRepository() {

    suspend fun getCinemasNearby(): NetworkResponse<CinemaResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val cinemaService = getCinemaService()
            cinemaService.getCinemasNearby()
        }
}