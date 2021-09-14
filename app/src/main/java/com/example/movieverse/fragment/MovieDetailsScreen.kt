package com.example.movieverse.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.example.movieverse.NavigationActivity
import com.example.movieverse.R
import com.example.movieverse.adapter.CastAdapter
import com.example.movieverse.databinding.MovieDetailsScreenBinding
import com.example.movieverse.model.movie.CastResponse
import com.example.movieverse.util.Constants
import com.example.movieverse.util.loadImage
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        getMovieDetailsById(args.selectedMovieId)
        getMovieCast(args.selectedMovieId)

        // for shared element transition
        binding.movieImage.transitionName = args.selectedMoviePoster
    }

    private fun subscribeObservers() {
        movieViewModel.movieDetailsResult.observe(viewLifecycleOwner, {
            // TODO: check for when it == null
            if (it != null) {
                binding.view.visibility = View.VISIBLE
                binding.overview.visibility = View.VISIBLE
                binding.movieImage.loadImage(
                    "${Constants.POSTER_BASE_URL}${args.selectedMoviePoster}",
                    R.drawable.ic_default_black
                )
                binding.title.text = args.selectedMovieTitle
                binding.overview.text = it.overview
            }
        })

        actorViewModel.castResult.observe(viewLifecycleOwner, {
            // TODO: check for when it == null
            if (!it.cast.isNullOrEmpty()) {
                binding.view.visibility = View.VISIBLE
                binding.castLabel.visibility = View.VISIBLE
                setupRecyclerView(it.cast, binding.castList, requireActivity())
            }
        })

        movieViewModel.showProgressBar.observe(viewLifecycleOwner, {
            (activity as NavigationActivity).showProgressBar(it)
        })
    }

    private fun setupRecyclerView(
        cast: List<CastResponse>,
        recyclerView: RecyclerView?,
        context: Context?
    ) {
        recyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        castAdapter = CastAdapter(cast = cast, context = context)
        recyclerView?.adapter = castAdapter
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