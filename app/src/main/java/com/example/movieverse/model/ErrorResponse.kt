package com.example.movieverse.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorResponse(
    @Json(name ="status_code")
    val statusCode: Int,
    @Json(name = "status_message")
    val statusMsg: String
) : Parcelable