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
import com.example.movieverse.R
import com.example.movieverse.adapter.MovieAdapter
import com.example.movieverse.databinding.HomeScreenBinding
import com.example.movieverse.model.movie.toMovieInDb
import com.example.movieverse.util.*
import com.example.movieverse.viewmodel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn

@ExperimentalCoroutinesApi
@FlowPreview
class HomeScreen : Fragment(),
    SearchViewModelUser, MovieViewModelUser, CinemaViewModelUser {

    private var _binding: HomeScreenBinding? = null
    private val binding
        get() = _binding!!

    private var searchJob: Job? = null
    private var searchAdapter: MovieAdapter? = null
    private var upcomingAdapter: MovieAdapter? = null

    override val searchViewModel by activitySearchViewModel()
    override val movieViewModel by activityMovieViewModel()
    override val cinemaViewModel by activityCinemaViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeScreenBinding.inflate(inflater, container, false)
        sharedElementReturnTransition =
            TransitionInflater.from(binding.root.context).inflateTransition(android.R.transition.move)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        subscribeObservers()
        getUpcomingMovies()
        movieViewModel.clearVideoId()

        binding.searchSection.searchIcon.setOnClickListener {
            searchMovie()
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
            //toggleVisibilities(binding.upcomingLyt, binding.movieLyt)
            upcomingAdapter?.submit(movies)
        })

        searchViewModel.shouldShowConnectionError.observe(viewLifecycleOwner, {
            if (it) {
                visibilityVisible(binding.connectionError)
                visibilityGone(binding.moviesList, binding.upcomingList)
            }
             else {
                visibilityGone(binding.connectionError)
                visibilityVisible(binding.moviesList, binding.upcomingList)
            }
        })

        searchViewModel.showProgressBar.observe(viewLifecycleOwner, {
            (activity as NavigationActivity).showProgressBar(it)
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
        (activity as NavigationActivity).showProgressBar(true)
        val textToSearch = binding.searchSection.searchInput.text.toString()
        searchViewModel.searchMovie(query = textToSearch)
    }

    private fun getUpcomingMovies() {
        searchViewModel.getUpcomingMovies()
        //todo: remove to another fragment
        //cinemaViewModel.getCinemasNearby()
    }

    private fun getMoviesGenres() {
        movieViewModel.getMoviesGenres()
    }

    private fun initAdapters() {
        searchAdapter = MovieAdapter(searchItemListener, storeListener)
        binding.moviesList.initRecyclerView(customAdapter = searchAdapter)
        upcomingAdapter = MovieAdapter(upcomingItemListener, storeListener)
        binding.upcomingList.initRecyclerView(customAdapter = upcomingAdapter)

    }

    private val searchItemListener = MovieAdapter.OnClickListener { position, poster ->
        val movieId = searchAdapter?.getSelectedMovie(position)?.id
        val imagePoster = searchAdapter?.getSelectedMovie(position)?.posterPath.valueOrEmpty()
        val title = searchAdapter?.getSelectedMovie(position)?.title.valueOrDashes()
        val releaseDate = searchAdapter?.getSelectedMovie(position)?.releaseDate.valueOrEmpty()
        val rating = searchAdapter?.getSelectedMovie(position)?.voteAverage?.div(2).valueOrEmpty()

        val direction: NavDirections? =
            movieId?.let {
                HomeScreenDirections.actionHomeScreenToMovieDetails(
                    selectedMovieId = it,
                    selectedMoviePoster = imagePoster,
                    selectedMovieTitle = title,
                    selectedReleaseDate = releaseDate,
                    selectedRating = rating
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
        val releaseDate = upcomingAdapter?.getSelectedMovie(position)?.releaseDate.valueOrEmpty()
        val rating = upcomingAdapter?.getSelectedMovie(position)?.voteAverage?.div(2).valueOrEmpty()

        val direction: NavDirections? =
            movieId?.let {
                HomeScreenDirections.actionHomeScreenToMovieDetails(
                    selectedMovieId = it,
                    selectedMoviePoster = imagePoster,
                    selectedMovieTitle = title,
                    selectedReleaseDate = releaseDate,
                    selectedRating = rating
                )
            }

        val extras = FragmentNavigatorExtras(
            poster to imagePoster
        )
        direction?.let { findNavController().navigate(it, extras) }
    }

    private val storeListener = MovieAdapter.OnStoreInDbListener { movie ->
        if (movieViewModel.getMovieById(movie.id) != movie.toMovieInDb()) {
            movieViewModel.storeMovie(movie)
            binding.homeLyt.showSnackbar(R.string.added_to_fav) {}
//            val movieBinding = MovieItemBinding.bind(binding.upcomingList.getChildAt(0))
//            movieBinding.favorite.isChecked = true
        } else {
            movie.id?.let { movieViewModel.removeMovie(it) }
            binding.homeLyt.showSnackbar(R.string.remove_from_fav) {}
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

