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
import com.example.movieverse.viewmodel.SearchViewModelUser
import com.example.movieverse.viewmodel.activitySearchViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class MainScreen : Fragment(), SearchViewModelUser {

    private var _binding: MainScreenBinding? = null
    private val binding
        get() = _binding!!

    override val searchViewModel by activitySearchViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        searchMovie("harry")
    }

    private fun createRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    private fun subscribeObservers() {
        searchViewModel.searchMovieResult.observe(viewLifecycleOwner, {
            when (val response1 = it) {
                is NetworkResponse.Success -> Log.d(TAG, "Success: ${response1.body.results}")
                is NetworkResponse.ApiError -> Log.d(
                    TAG,
                    "ApiError: statusCode: ${response1.body.statusCode} , statusMsg: ${response1.body.statusMsg}"
                )
                is NetworkResponse.NetworkError -> Log.d(TAG, "NetworkError")
                is NetworkResponse.UnknownError -> Log.d(TAG, "UnknownError")
            }
        })
    }

    private fun searchMovie(query: String) {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val okHttpClient = OkHttpClient.Builder().build()
        val retrofit = createRetrofit(moshi, okHttpClient)
        val service = retrofit.create<SearchService>()

        searchViewModel.searchMovie(query, service)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = MainScreen::class.java.simpleName
    }
}

