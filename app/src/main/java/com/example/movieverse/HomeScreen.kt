package com.example.movieverse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieverse.databinding.HomeScreenBinding
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.viewmodel.SearchViewModelUser
import com.example.movieverse.viewmodel.activitySearchViewModel

class HomeScreen : Fragment(), SearchViewModelUser {

    private var _binding: HomeScreenBinding? = null
    private val binding
        get() = _binding!!

    override val searchViewModel by activitySearchViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        searchMovie("harry")
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
        searchViewModel.searchMovie(query)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = HomeScreen::class.java.simpleName
    }
}

