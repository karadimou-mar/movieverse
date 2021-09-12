package com.example.movieverse.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.NavigationActivity
import com.example.movieverse.R
import com.example.movieverse.adapter.CastAdapter
import com.example.movieverse.databinding.MovieDetailsScreenBinding
import com.example.movieverse.model.movie.CastResponse
import com.example.movieverse.net.NetworkResponse
import com.example.movieverse.util.Constants
import com.example.movieverse.util.loadImage
import com.example.movieverse.viewmodel.SearchViewModel
import com.example.movieverse.viewmodel.SearchViewModelUser
import com.example.movieverse.viewmodel.activitySearchViewModel

class MovieDetailsScreen : Fragment(), SearchViewModelUser {

    private var _binding: MovieDetailsScreenBinding? = null
    private val binding
        get() = _binding!!

    // TODO: split viewModels??
    // TODO: when going back to HomeScreen, popular etc. lists appear

    private val args: MovieDetailsScreenArgs by navArgs()
    override val searchViewModel: SearchViewModel by activitySearchViewModel()
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
    }

    private fun subscribeObservers() {
        searchViewModel.movieDetailsResult.observe(viewLifecycleOwner, {
            when (val response = it) {
                is NetworkResponse.Success -> {
                    (activity as NavigationActivity).showProgressBar(false)
                    Log.d(TAG, "MovieDetails: Success: ${response.body}")
                    binding.view.visibility = View.VISIBLE
                    binding.overview.visibility = View.VISIBLE
                    binding.movieImage.loadImage(
                        "${Constants.POSTER_BASE_URL}${args.selectedMoviePoster}",
                        R.drawable.ic_launcher_foreground
                    )
                    binding.title.text = args.selectedMovieTitle
                    binding.overview.text = response.body.overview
                }
                is NetworkResponse.ApiError -> {
                    (activity as NavigationActivity).showProgressBar(false)
                    Log.d(
                        TAG,
                        "MovieDetails: ApiError: statusCode: ${response.body.statusCode} , statusMsg: ${response.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    (activity as NavigationActivity).showProgressBar(false)
                    Log.d(
                        TAG,
                        "MovieDetails: NetworkError: ${response.error.message}"
                    )
                }
                is NetworkResponse.UnknownError -> {
                    (activity as NavigationActivity).showProgressBar(false)
                    Log.d(
                        TAG,
                        "MovieDetails: UnknownError: ${response.error?.message}"
                    )
                }
            }
        })

        searchViewModel.castResult.observe(viewLifecycleOwner, {
            when (val response = it) {
                is NetworkResponse.Success -> {
                    (activity as NavigationActivity).showProgressBar(false)
                    Log.d(TAG, "MovieDetails: Success: ${response.body}")
                    binding.view.visibility = View.VISIBLE
                    binding.castLabel.visibility = View.VISIBLE
                    setupRecyclerView(response.body.cast, binding.castList, requireActivity())
                }
                is NetworkResponse.ApiError -> {
                    (activity as NavigationActivity).showProgressBar(false)
                    Log.d(
                        TAG,
                        "Cast: ApiError: statusCode: ${response.body.statusCode} , statusMsg: ${response.body.statusMsg}"
                    )
                }
                is NetworkResponse.NetworkError -> {
                    (activity as NavigationActivity).showProgressBar(false)
                    Log.d(
                        TAG,
                        "Cast: NetworkError: ${response.error.message}"
                    )
                }
                is NetworkResponse.UnknownError -> {
                    (activity as NavigationActivity).showProgressBar(false)
                    Log.d(
                        TAG,
                        "Cast: UnknownError: ${response.error?.message}"
                    )
                }
            }
        })
    }

    private fun setupRecyclerView(
        cast: List<CastResponse>,
        recyclerView: RecyclerView?,
        context: Context?
    ) {
        recyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        castAdapter = CastAdapter(cast = cast, context = context)
        recyclerView?.adapter = castAdapter
    }

    private fun getMovieDetailsById(movieId: Int) {
        (activity as NavigationActivity).showProgressBar(true)
        searchViewModel.getMovieDetailsById(movieId)
    }

    private fun getMovieCast(movieId: Int) {
        (activity as NavigationActivity).showProgressBar(true)
        searchViewModel.getMovieCast(movieId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = MovieDetailsScreen::class.java.simpleName
    }
}