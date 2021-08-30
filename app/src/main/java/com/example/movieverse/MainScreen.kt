package com.example.movieverse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieverse.databinding.MainScreenBinding
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

class MainScreen : Fragment() {

    private var _binding: MainScreenBinding? = null
    val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val okHttpClient = OkHttpClient.Builder().build()
        val retrofit = createRetrofit(moshi, okHttpClient)
        val service = retrofit.create<SearchService>()

        GlobalScope.launch {
            when (val response1 = service.searchMovie(query = "harry")) {
                is NetworkResponse.Success -> Log.d(TAG, "Success: ${response1.body.results}")
                is NetworkResponse.ApiError -> Log.d(
                    TAG,
                    "ApiError: statusCode: ${response1.body.statusCode} , statusMsg: ${response1.body.statusMsg}"
                )
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
        private val TAG = MainScreen::class.java.simpleName
    }
}

