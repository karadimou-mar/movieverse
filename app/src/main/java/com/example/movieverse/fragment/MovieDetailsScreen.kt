package com.example.movieverse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.movieverse.databinding.MovieDetailsScreenBinding

class MovieDetailsScreen : Fragment() {

    private var _binding: MovieDetailsScreenBinding? = null
    private val binding
        get() = _binding!!

    val args: MovieDetailsScreenArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailsScreenBinding.inflate(inflater, container, false)
        Toast.makeText(activity, "$TAG ${args.selectedMovieId}", Toast.LENGTH_SHORT).show()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = MovieDetailsScreen::class.java.simpleName
    }
}