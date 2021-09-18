package com.example.movieverse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.movieverse.NavigationActivity
import com.example.movieverse.R
import com.example.movieverse.adapter.CastAdapter
import com.example.movieverse.databinding.MovieDetailsScreenBinding
import com.example.movieverse.util.*
import com.example.movieverse.viewmodel.*
import java.util.concurrent.TimeUnit

class MovieDetailsScreen : Fragment(), MovieViewModelUser, ActorViewModelUser {

    private var _binding: MovieDetailsScreenBinding? = null
    private val binding
        get() = _binding!!

    private val args: MovieDetailsScreenArgs by navArgs()
    override val movieViewModel: MovieViewModel by activityMovieViewModel()
    override val actorViewModel: ActorViewModel by activityActorViewModel()
    private var castAdapter: CastAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailsScreenBinding.inflate(inflater, container, false)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        subscribeObservers()
        getMovieDetailsById(args.selectedMovieId)
        getMovieCast(args.selectedMovieId)
        //setupBackButton()

        // for shared element transition
        binding.movieImage.transitionName = args.selectedMoviePoster
    }

    private fun subscribeObservers() {
        movieViewModel.movieDetailsResult.observe(viewLifecycleOwner, {
            // TODO: check for when it == null
            if (it != null) {
                binding.view.visibility = View.VISIBLE
                binding.overview.visibility = View.VISIBLE
                binding.overview.text = it.overview
                binding.movieImage.loadImage(
                    "${Constants.POSTER_BASE_URL}${args.selectedMoviePoster}",
                    R.drawable.ic_default_black
                )
                binding.title.text = args.selectedMovieTitle
            }
        })

        actorViewModel.castResult.observe(viewLifecycleOwner, {
            // TODO: check for when it == null
            if (!it.cast.isNullOrEmpty()) {
                binding.view.visibility = View.VISIBLE
                binding.castLabel.visibility = View.VISIBLE
                castAdapter?.submit(it.cast)
            }
        })

        movieViewModel.showProgressBar.observe(viewLifecycleOwner, {
            (activity as NavigationActivity).showProgressBar(it)
        })
    }

    private fun initAdapters() {
        castAdapter = CastAdapter(castDetailsItemListener)
        binding.castList.initHorizontalRecyclerView(customAdapter = castAdapter)
    }

    private val castDetailsItemListener = CastAdapter.OnClickListener { position ->
        val directions: NavDirections =
            MovieDetailsScreenDirections.actionMovieDetailsScreenToCastDetailsScreen()

        directions.let { findNavController().navigate(it) }
    }

    private fun getMovieDetailsById(movieId: Int) {
        (activity as NavigationActivity).showProgressBar(true)
        movieViewModel.getMovieDetailsById(movieId)
    }

    private fun getMovieCast(movieId: Int) {
        (activity as NavigationActivity).showProgressBar(true)
        actorViewModel.getMovieCast(movieId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = MovieDetailsScreen::class.java.simpleName
    }
}