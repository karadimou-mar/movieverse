package com.example.movieverse.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.adapter.MovieAdapter
import com.example.movieverse.adapter.OnMovieListener
import com.example.movieverse.databinding.HomeScreenBinding
import com.example.movieverse.model.movie.MovieResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.util.callBackWhileTyping
import com.example.movieverse.util.hideKeyboard
import com.example.movieverse.util.visibilityGone
import com.example.movieverse.viewmodel.SearchViewModelUser
import com.example.movieverse.viewmodel.activitySearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn

@ExperimentalCoroutinesApi
@FlowPreview
class HomeScreen : Fragment(), SearchViewModelUser, OnMovieListener {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setupRecyclerView()
        subscribeObservers()
        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()

        // search on search icon click
        binding.searchSection.searchIcon.setOnClickListener {
            searchMovie()
            getMoviesGenres()
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
                    setupRecyclerView(movies, binding.moviesList, requireContext())
                    // remove other movie lists
                    // TODO: improve that part later on
                    visibilityGone(binding.topRatedLyt, binding.upcomingLyt)
                    binding.movieLabel.visibility = View.GONE
                    binding.movieLyt.updateLayoutParams { height = WRAP_CONTENT }
                    for (m in movies.indices) {
                        Log.d(TAG, "Success: ${movies[m]}")
                    }
                    Log.d(TAG, "total results: ${movies.size}")
                }
                is NetworkResponse.ApiError -> Log.d(
                    TAG,
                    "ApiError: statusCode: ${response.body.statusCode} , statusMsg: ${response.body.statusMsg}"
                )
                is NetworkResponse.NetworkError -> Log.d(TAG, "NetworkError: ${response.error.message}")
                is NetworkResponse.UnknownError -> Log.d(TAG, "UnknownError: ${response.error?.message}")
            }
        })

        searchViewModel.popularMoviesResult.observe(viewLifecycleOwner, {
            when (val response = it) {
                is NetworkResponse.Success -> {
                    val movies = response.body.results
                    setupRecyclerView(movies, binding.moviesList, requireContext())
                    binding.movieLabel.visibility = View.VISIBLE
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

        searchViewModel.topRatedMoviesResult.observe(viewLifecycleOwner, {
            when (val response = it) {
                is NetworkResponse.Success -> {
                    val topRatedMovies = response.body.results
                    setupRecyclerView(topRatedMovies, binding.topRatedList, requireContext())
                    binding.topRatedLabel.visibility = View.VISIBLE
                    for (m in topRatedMovies.indices) {
                        Log.d(TAG, "Top Rated: Success: ${topRatedMovies[m]}")
                    }
                    Log.d(TAG, "Top Rated: total results: ${topRatedMovies.size}")
                }
                is NetworkResponse.ApiError -> Log.d(
                    TAG,
                    "ApiError: Top Rated: statusCode: ${response.body.statusCode} , statusMsg: ${response.body.statusMsg}"
                )
                is NetworkResponse.NetworkError -> Log.d(TAG, "Top Rated: NetworkError")
                is NetworkResponse.UnknownError -> Log.d(TAG, "Top Rated: UnknownError")
            }
        })

        searchViewModel.upcomingMoviesResult.observe(viewLifecycleOwner, {
            when (val response = it) {
                is NetworkResponse.Success -> {
                    val upcomingMovies = response.body.results
                    setupRecyclerView(upcomingMovies, binding.upcomingList, context)
                    binding.upcomingLabel.visibility = View.VISIBLE
                    for (m in upcomingMovies.indices) {
                        Log.d(TAG, "Upcoming: Success: ${upcomingMovies[m]}")
                    }
                    Log.d(TAG, "Upcoming: total results: ${upcomingMovies.size}")
                }
                is NetworkResponse.ApiError -> Log.d(
                    TAG,
                    "ApiError: Upcoming: statusCode: ${response.body.statusCode} , statusMsg: ${response.body.statusMsg}"
                )
                is NetworkResponse.NetworkError -> Log.d(TAG, "Upcoming: NetworkError")
                is NetworkResponse.UnknownError -> Log.d(TAG, "Upcoming: UnknownError")
            }
        })

        searchViewModel.moviesGenreResponse.observe(viewLifecycleOwner, {
            when (val response = it) {
                is NetworkResponse.Success -> {
                    val genres = response.body
                    Log.d(TAG, "Success: Genres: $genres")
                }
                is NetworkResponse.ApiError -> Log.d(
                    TAG,
                    "ApiError: Genre: statusCode: ${response.body.statusCode} , statusMsg: ${response.body.statusMsg}"
                )
                is NetworkResponse.NetworkError -> Log.d(TAG, "Upcoming: NetworkError")
                is NetworkResponse.UnknownError -> Log.d(TAG, "Upcoming: UnknownError")
            }
        })
    }

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

    private fun getPopularMovies() {
        searchViewModel.getPopularMovies()
    }

    private fun getTopRatedMovies() {
        searchViewModel.getTopRatedMovies()
    }

    private fun getUpcomingMovies() {
        searchViewModel.getUpcomingMovies()
    }

    private fun getMoviesGenres() {
        searchViewModel.getMoviesGenres()
    }

    private fun setupRecyclerView(
        movies: List<MovieResponse>,
        recyclerView: RecyclerView,
        context: Context?
    ) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        movieAdapter = MovieAdapter(movies = movies, context = context, onMovieListener = this)
        recyclerView.adapter = movieAdapter
    }

    override fun onMovieClick(position: Int) {
        val movieId = movieAdapter?.getSelectedMovieId(position)?.id
        val action = movieId?.let { HomeScreenDirections.actionHomeScreenToMovieDetails(selectedMovieId = it) }
        action?.let { findNavController().navigate(it) }
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

