package com.example.movieverse.net

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

inline fun <reified T> createRetrofitService(
    baseUrl: String): T =

    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(createOkHttpClient())
        .build()
        .create(T::class.java)


// TODO: maybe we could add other adapters later on
val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

fun createOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { this.level = Level.BODY })
        .build()