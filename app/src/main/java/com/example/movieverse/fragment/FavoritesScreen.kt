package com.example.movieverse.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.movieverse.NavigationActivity
import com.example.movieverse.R
import com.example.movieverse.adapter.FavAdapter
import com.example.movieverse.adapter.MovieAdapter
import com.example.movieverse.databinding.FavoritesScreenBinding
import com.example.movieverse.util.*
import com.example.movieverse.viewmodel.MovieViewModelUser
import com.example.movieverse.viewmodel.activityMovieViewModel

class FavoritesScreen : Fragment(), MovieViewModelUser {

    private var _binding: FavoritesScreenBinding? = null
    private val binding
        get() = _binding!!

    override val movieViewModel by activityMovieViewModel()

    private var favAdapter: FavAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritesScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMoviesFromDb()
        subscribeObservers()
        initAdapters()
    }

    private fun subscribeObservers() {
        movieViewModel.moviesInDb.observe(viewLifecycleOwner, {
            Log.d(TAG, "subscribeObservers: $it")
            favAdapter?.submit(it)
        })
        movieViewModel.movieRemoved.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.layout.showSnackbar(R.string.fav_movie_deleted, it.title) {}
            }
        })
    }

    private fun getMoviesFromDb() {
        movieViewModel.getMoviesFromDb()
    }

    private fun initAdapters() {
        favAdapter = FavAdapter(favMovieListener)
        binding.favMoviesList.initRecyclerViewWithCallback(customAdapter = favAdapter) {
            favAdapter?.deleteMovieFromDb(it)
            movieViewModel.deleteMovieAtPosition(it)
            favAdapter?.notifyItemRemoved(it)
        }
    }

    private val favMovieListener = FavAdapter.OnClickListener { movieId ->
        getMovieDetailsById(movieId)
        val direction: NavDirections =
                FavoritesScreenDirections.actionFavoritesScreenToFavVideoDialog(
                    movieId = movieId
                )
        findNavController().navigate(direction)
    }

    private fun getMovieDetailsById(movieId: Int) {
        movieViewModel.getMovieDetailsById(movieId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = FavoritesScreen::class.java.simpleName
    }
}