package com.example.movieverse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.movieverse.NavigationActivity
import com.example.movieverse.R
import com.example.movieverse.adapter.CastAdapter
import com.example.movieverse.databinding.MovieDetailsScreenBinding
import com.example.movieverse.util.*
import com.example.movieverse.viewmodel.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import java.util.concurrent.TimeUnit

class MovieDetailsScreen : Fragment(), MovieViewModelUser, CastViewModelUser {

    private var _binding: MovieDetailsScreenBinding? = null
    private val binding
        get() = _binding!!

    private val args: MovieDetailsScreenArgs by navArgs()
    override val movieViewModel: MovieViewModel by activityMovieViewModel()
    override val castViewModel: CastViewModel by activityCastViewModel()
    private var castAdapter: CastAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailsScreenBinding.inflate(inflater, container, false)

        lifecycle.addObserver(binding.ytPlayer)

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
        getMovieVideo(args.selectedMovieId)

        // for shared element transition
        binding.movieImage.transitionName = args.selectedMoviePoster
    }

    private fun subscribeObservers() {
        movieViewModel.movieDetailsResult.observe(viewLifecycleOwner, {
            // TODO: check for when it == null
            if (it != null) {
                binding.topView.visibility = View.VISIBLE
                binding.overview.visibility = View.VISIBLE
                binding.overview.text = it.overview
                binding.movieImage.loadImage(
                    "${Constants.POSTER_BASE_URL}${args.selectedMoviePoster}",
                    R.drawable.ic_launcher_foreground
                )
                binding.title.text = args.selectedMovieTitle
                binding.year.text = context?.getString(R.string.yearOfRelease, args.selectedReleaseDate.substringBefore('-'))
                binding.ratingBar.rating = args.selectedRating.toFloat()
                binding.runtime.text = it.runtime.toHoursMinutes()
            }
        })

        castViewModel.castResult.observe(viewLifecycleOwner, {
            // TODO: check for when it == null
            if (!it.cast.isNullOrEmpty()) {
                binding.topView.visibility = View.VISIBLE
                binding.castLabel.visibility = View.VISIBLE
                castAdapter?.submit(it.cast)
            }
        })

        movieViewModel.movieVideosResult.observe(viewLifecycleOwner, {
            if (it.results?.isNotEmpty() == true) {
                binding.ytPlayer.visibility = View.VISIBLE
                it.results[0].key?.let { key ->
                    loadYouTube(key)
                }
            } else {
                binding.topView.visibility = View.GONE
                binding.ytPlayer.visibility = View.GONE
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

    private val castDetailsItemListener = CastAdapter.OnClickListener { position, image ->
        val personId = castAdapter?.getSelectedPerson(position)?.personId
        val personImage = castAdapter?.getSelectedPerson(position)?.profilePath.valueOrEmpty()

        val direction: NavDirections? =
            personId?.let {
                MovieDetailsScreenDirections.actionMovieDetailsScreenToCastDetailsScreen(
                    selectedPersonId = it,
                    selectedPersonImage = personImage
                )
            }
        val extras = FragmentNavigatorExtras(
             image to personImage
        )
        direction?.let { findNavController().navigate(it, extras) }
    }

    private fun getMovieDetailsById(movieId: Int) {
        (activity as NavigationActivity).showProgressBar(true)
        movieViewModel.getMovieDetailsById(movieId)
    }

    private fun getMovieCast(movieId: Int) {
        (activity as NavigationActivity).showProgressBar(true)
        castViewModel.getMovieCast(movieId)
    }

    private fun getMovieVideo(movieId: Int) {
        movieViewModel.getMovieVideo(movieId)
    }

    private fun loadYouTube(id: String) {
        binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(id, 0f)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.ytPlayer.release()
        _binding = null
    }

    companion object {
        private val TAG = MovieDetailsScreen::class.java.simpleName
    }
}