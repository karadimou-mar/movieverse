package com.example.movieverse.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.R
import com.example.movieverse.databinding.MovieItemBinding
import com.example.movieverse.model.movie.MovieResponse
import com.example.movieverse.util.Constants.POSTER_BASE_URL
import com.example.movieverse.util.loadImage

class MovieViewHolder(
    private val binding: MovieItemBinding,
    private val onMovieListener: OnMovieListener,
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.root.setOnClickListener(this)
    }

    fun bind(movie: MovieResponse, context: Context?) {
        // TODO image
        // TODO remove MovieResponse and add Movie data class
        // TODO find a way to remove context from here
        binding.title.text = movie.title
        binding.year.text =
            context?.getString(R.string.yearOfRelease, movie.releaseDate.substringBefore('-'))
        binding.ratingBar.rating = (movie.voteAverage / 2).toFloat()
        binding.movieImage.loadImage(
            "${POSTER_BASE_URL}${movie.posterPath}",
            R.drawable.ic_launcher_foreground
        )
        // TODO: genres need extra GET request => /genre/movie/list
        //binding.genre.text = "${movie.genreIds[0]}"
    }

    override fun onClick(view: View?) {
        onMovieListener.onMovieClick(adapterPosition)
    }
}