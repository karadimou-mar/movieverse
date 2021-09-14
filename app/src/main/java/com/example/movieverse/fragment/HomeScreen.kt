package com.example.movieverse.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.example.movieverse.NavigationActivity
import com.example.movieverse.adapter.MovieAdapter
import com.example.movieverse.databinding.HomeScreenBinding
import com.example.movieverse.model.movie.MovieResponse
import com.example.movieverse.util.*
import com.example.movieverse.viewmodel.MovieViewModelUser
import com.example.movieverse.viewmodel.SearchViewModelUser
import com.example.movieverse.viewmodel.activityMovieViewModel
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
    override val movieViewModel by activityMovieViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUpcomingMovies()

//        val onBackPressedCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }

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
        //getUpcomingMovies()

        binding.searchSection.searchIcon.setOnClickListener {
            searchMovie()
            //getMoviesGenres()
            it.hideKeyboard(context)
        }
        //search also while typing
        //TODO: keep both?
        //searchMovieWhileTyping()
    }

    private fun subscribeObservers() {
        searchViewModel.searchListResult.observe(viewLifecycleOwner, {
            toggleVisibilities(binding.movieLyt, binding.upcomingLyt)
            setupRecyclerView(it, binding.moviesList, requireContext())
        })

        searchViewModel.upcomingListResult.observe(viewLifecycleOwner, {
            toggleVisibilities(binding.upcomingLyt, binding.movieLyt)
            setupRecyclerView(it, binding.upcomingList, requireContext())
        })

        searchViewModel.showProgressBar.observe(viewLifecycleOwner, {
            (activity as NavigationActivity).showProgressBar(it)
        })

        // TODO: observe for genre
    }

    private fun searchMovieWhileTyping() {
        searchJob = binding.searchSection.searchInput.callBackWhileTyping {
            it?.let { textToSearch ->
                searchViewModel.searchMovie(query = textToSearch.toString())
            }
        }.launchIn(lifecycleScope)
    }

    private fun searchMovie() {
        (activity as NavigationActivity).showProgressBar(true)
        val textToSearch = binding.searchSection.searchInput.text.toString()
        searchViewModel.searchMovie(query = textToSearch)
    }

    private fun getUpcomingMovies() {
        searchViewModel.getUpcomingMovies()
    }

    private fun getMoviesGenres() {
        movieViewModel.getMoviesGenres()
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
        val poster = movieAdapter?.getSelectedMovieId(position)?.posterPath
        val title = movieAdapter?.getSelectedMovieId(position)?.title
        val action =
            movieId?.let { HomeScreenDirections.actionHomeScreenToMovieDetails(
                selectedMovieId = it,
                selectedMoviePoster = poster!!,
                selectedMovieTitle = title!!) }
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

