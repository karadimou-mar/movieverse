package com.example.movieverse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieverse.adapter.MovieAdapter
import com.example.movieverse.databinding.HomeScreenBinding
import com.example.movieverse.model.movie.MovieResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.util.callBackWhileTyping
import com.example.movieverse.util.hideKeyboard
import com.example.movieverse.viewmodel.SearchViewModelUser
import com.example.movieverse.viewmodel.activitySearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn

@FlowPreview
class HomeScreen : Fragment(), SearchViewModelUser {

    private var _binding: HomeScreenBinding? = null
    private val binding
        get() = _binding!!

    private var searchJob: Job? = null
    private var movieAdapter: MovieAdapter? = null

    override val searchViewModel by activitySearchViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setupRecyclerView()
        subscribeObservers()

        // search on search icon click
        binding.searchSection.searchIcon.setOnClickListener {
            searchMovie()
            it.hideKeyboard(context)
        }
        //search also while typing
        //TODO: keep both?
        //searchMovieWhileTyping()
    }

    private fun subscribeObservers() {
        searchViewModel.searchMovieResult.observe(viewLifecycleOwner, {
            when (val response = it) {
                is NetworkResponse.Success -> {
                    val movies = response.body.results
                    setupRecyclerView(movies)
                    for (m in movies.indices) {
                        Log.d(TAG, "Success: ${movies[m]}")
                    }
                    Log.d(TAG, "total results: ${movies.size}")
                }
                is NetworkResponse.ApiError -> Log.d(
                    TAG,
                    "ApiError: statusCode: ${response.body.statusCode} , statusMsg: ${response.body.statusMsg}"
                )
                is NetworkResponse.NetworkError -> Log.d(TAG, "NetworkError")
                is NetworkResponse.UnknownError -> Log.d(TAG, "UnknownError")
            }
        })
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun searchMovieWhileTyping() {
        searchJob = binding.searchSection.searchInput.callBackWhileTyping {
            it?.let { textToSearch ->
                searchViewModel.searchMovie(query = textToSearch.toString())
            }
        }.launchIn(lifecycleScope)
    }

    private fun searchMovie() {
        val textToSearch = binding.searchSection.searchInput.text.toString()
        searchViewModel.searchMovie(query = textToSearch)
    }

    private fun setupRecyclerView(movies: List<MovieResponse>) {
        binding.moviesList.apply {
            layoutManager = LinearLayoutManager(activity)
            movieAdapter = MovieAdapter(movies = movies, context = context)
            adapter = movieAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        searchJob?.cancel()
    }

    companion object {
        private val TAG = HomeScreen::class.java.simpleName
    }
}

