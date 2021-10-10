package com.example.movieverse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieverse.databinding.FavoritesScreenBinding
import com.example.movieverse.viewmodel.MovieViewModelUser
import com.example.movieverse.viewmodel.activityMovieViewModel

class FavoritesScreen : Fragment(), MovieViewModelUser {

    private var _binding: FavoritesScreenBinding? = null
    private val binding
        get() = _binding!!

    override val movieViewModel by activityMovieViewModel()

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

        subscribeObservers()
        loadMovies()
    }

    private fun subscribeObservers() {
        movieViewModel.moviesInDb.observe(viewLifecycleOwner, {
            // TODO: continue later on
            for (i in it) {
                binding.favTv.append(i.title)
            }
        })
    }

    private fun loadMovies() {
        movieViewModel.getMoviesList()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = FavoritesScreen::class.java.simpleName
    }
}