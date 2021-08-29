package com.example.movieverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.net.NetworkResponseAdapterFactory
import com.example.movieverse.net.search.SearchService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        val okHttpClient = OkHttpClient.Builder().build()
        val retrofit = createRetrofit(moshi, okHttpClient)
        val service = retrofit.create<SearchService>()

        GlobalScope.launch {
            when (val response1 = service.searchMovie(query = "harry")) {
                is NetworkResponse.Success      -> Log.d(TAG, "Success: ${response1.body.results}")
                is NetworkResponse.ApiError     -> Log.d(TAG, "ApiError: statusCode: ${response1.body.statusCode} , statusMsg: ${response1.body.statusMsg}")
                is NetworkResponse.NetworkError -> Log.d(TAG, "NetworkError")
                is NetworkResponse.UnknownError -> Log.d(TAG, "UnknownError")
            }
        }
    }

    private fun createRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}

