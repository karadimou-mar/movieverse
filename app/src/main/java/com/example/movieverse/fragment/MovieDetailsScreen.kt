package com.example.movieverse.fragment

import android.content.Intent
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
import com.example.movieverse.adapter.HorizMovieAdapter
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
    private var recomAdapter: HorizMovieAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailsScreenBinding.inflate(inflater, container, false)

        lifecycle.addObserver(binding.ytPlayer)

        sharedElementEnterTransition =
            TransitionInflater.from(binding.root.context).inflateTransition(android.R.transition.move)
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        subscribeObservers()
        getMovieDetailsById(args.selectedMovieId)
        movieViewModel.clearImdbId()
        movieViewModel.clearVideoId()
        movieViewModel.clearCast()

        // for shared element transition
        binding.movieImage.transitionName = args.selectedMoviePoster
    }

    private fun subscribeObservers() {
        movieViewModel.movieDetailsResult.observe(viewLifecycleOwner, {
            // TODO: check for when it == null
            if (it != null) {
                visibilityVisible(binding.parentLyt, binding.topView)
                binding.overview.text = it.overview
                binding.movieImage.loadImage(
                    "${Constants.POSTER_BASE_URL}${args.selectedMoviePoster}",
                    R.drawable.ic_launcher_foreground
                )
                binding.title.text = args.selectedMovieTitle
                binding.year.text = context?.getString(
                    R.string.yearOfRelease,
                    args.selectedReleaseDate.toLocalDate()?.year
                )
                binding.ratingBar.rating = args.selectedRating.toFloat()
                binding.runtime.text = it.runtime.toHoursMinutes()
            }
        })

        movieViewModel.movieId.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                visibilityVisible(binding.ytPlayer)
                loadYouTube(it)
            } else {
                visibilityGone(binding.topView, binding.ytPlayer)
            }
        })

        movieViewModel.castResult.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                visibilityVisible(binding.castLabel)
                castAdapter?.submit(it.sortByOrder())
            }
        })

        movieViewModel.crewResult.observe(viewLifecycleOwner, {
            if (it.toDirectors() != "") {
                visibilityVisible(binding.director)
                binding.director.text = getString(R.string.director, it.toDirectors())
            } else {
                visibilityGone(binding.director)
            }

            if (it.toWriters() != "") {
                visibilityVisible(binding.writer)
                binding.writer.text = getString(R.string.writer, it.toDirectors())
            } else {
                visibilityGone(binding.writer)
            }
        })

        movieViewModel.recomResult.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                visibilityVisible(binding.recommendationsLabel)
                recomAdapter?.submit(it.sortByPopularity())
            }
        })

        movieViewModel.imdbIdResult.observe(viewLifecycleOwner, { imdbId ->
            binding.shareImg.setOnClickListener {
                if (imdbId.isNotEmpty()) {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "https://www.imdb.com/title/$imdbId/")
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
            }
        })

        movieViewModel.showProgressBar.observe(viewLifecycleOwner, {
            (activity as NavigationActivity).showProgressBar(it)
        })
    }

    private fun initAdapters() {
        castAdapter = CastAdapter(castDetailsItemListener)
        binding.castList.initHorizontalRecyclerView(customAdapter = castAdapter)
        recomAdapter = HorizMovieAdapter(recomItemListener)
        binding.recommendationsList.initHorizontalRecyclerView(customAdapter = recomAdapter)
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

    private val recomItemListener = HorizMovieAdapter.OnClickListener { position, image ->
        val movieId = recomAdapter?.getSelectedMovie(position)?.id
        val imagePoster = recomAdapter?.getSelectedMovie(position)?.posterPath.valueOrEmpty()
        val title = recomAdapter?.getSelectedMovie(position)?.title.valueOrDashes()
        val releaseDate = recomAdapter?.getSelectedMovie(position)?.releaseDate.valueOrEmpty()
        val rating = recomAdapter?.getSelectedMovie(position)?.voteAverage?.div(2).valueOrEmpty()

        val direction: NavDirections? =
            movieId?.let {
                MovieDetailsScreenDirections.actionMovieDetailsScreenToMovieDetailsScreen(
                    selectedMovieId = it,
                    selectedMoviePoster = imagePoster,
                    selectedMovieTitle = title,
                    selectedReleaseDate = releaseDate,
                    selectedRating = rating
                )
            }
        val extras = FragmentNavigatorExtras(
            image to imagePoster
        )
        direction?.let { findNavController().navigate(it, extras) }
    }

    private fun getMovieDetailsById(movieId: Int) {
        (activity as NavigationActivity).showProgressBar(true)
        movieViewModel.getMovieDetailsById(movieId)
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