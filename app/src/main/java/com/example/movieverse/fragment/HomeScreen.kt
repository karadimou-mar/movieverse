package com.example.movieverse.fragment

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
import androidx.transition.TransitionInflater
import com.example.movieverse.NavigationActivity
import com.example.movieverse.adapter.MovieAdapter
import com.example.movieverse.databinding.HomeScreenBinding
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
class HomeScreen : Fragment(), SearchViewModelUser, MovieViewModelUser {

    private var _binding: HomeScreenBinding? = null
    private val binding
        get() = _binding!!

    private var searchJob: Job? = null
    private var searchAdapter: MovieAdapter? = null
    private var upcomingAdapter: MovieAdapter? = null

    override val searchViewModel by activitySearchViewModel()
    override val movieViewModel by activityMovieViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeScreenBinding.inflate(inflater, container, false)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        //getUpcomingMovies()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        subscribeObservers()
        getUpcomingMovies()

        binding.searchSection.searchIcon.setOnClickListener {
            searchMovie()
            //getMoviesGenres()
            it.hideKeyboard(context)
        }
        //search also while typing
        //TODO: keep both?
        //searchMovieWhileTyping()

        // When user hits back button transition takes backward
        postponeEnterTransition()
        binding.upcomingList.doOnPreDraw {
            startPostponedEnterTransition()
        }
        binding.moviesList.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun subscribeObservers() {
        searchViewModel.searchListResult.observe(viewLifecycleOwner, { movies ->
            toggleVisibilities(binding.movieLyt, binding.upcomingLyt)
            searchAdapter?.submit(movies)
        })

        searchViewModel.upcomingListResult.observe(viewLifecycleOwner, { movies ->
            upcomingAdapter?.submit(movies)
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

    private fun initAdapters() {
        searchAdapter = MovieAdapter(searchItemListener)
        binding.moviesList.initRecyclerView(customAdapter = searchAdapter)
        upcomingAdapter = MovieAdapter(upcomingItemListener)
        binding.upcomingList.initRecyclerView(customAdapter = upcomingAdapter)

    }

    private val searchItemListener = MovieAdapter.OnClickListener { position, poster ->
        val movieId = searchAdapter?.getSelectedMovie(position)?.id
        val imagePoster = searchAdapter?.getSelectedMovie(position)?.posterPath.valueOrEmpty()
        val title = searchAdapter?.getSelectedMovie(position)?.title

        val direction: NavDirections? =
            movieId?.let {
                HomeScreenDirections.actionHomeScreenToMovieDetails(
                    selectedMovieId = it,
                    selectedMoviePoster = imagePoster,
                    selectedMovieTitle = title!!
                )
            }
        val extras = FragmentNavigatorExtras(
            poster to imagePoster
        )
        direction?.let { findNavController().navigate(it, extras) }
    }

    private val upcomingItemListener = MovieAdapter.OnClickListener { position, poster ->
        val movieId = upcomingAdapter?.getSelectedMovie(position)?.id
        val imagePoster = upcomingAdapter?.getSelectedMovie(position)?.posterPath.valueOrEmpty()
        val title = upcomingAdapter?.getSelectedMovie(position)?.title.valueOrEmpty()

        val direction: NavDirections? =
            movieId?.let {
                HomeScreenDirections.actionHomeScreenToMovieDetails(
                    selectedMovieId = it,
                    selectedMoviePoster = imagePoster,
                    selectedMovieTitle = title
                )
            }

        val extras = FragmentNavigatorExtras(
            poster to imagePoster
        )
        direction?.let { findNavController().navigate(it, extras) }
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

