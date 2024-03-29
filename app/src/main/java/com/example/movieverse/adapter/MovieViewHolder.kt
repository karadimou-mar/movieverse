package com.example.movieverse.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.R
import com.example.movieverse.databinding.MovieItemBinding
import com.example.movieverse.model.movie.MovieResponse
import com.example.movieverse.util.*
import com.example.movieverse.util.Constants.POSTER_BASE_URL

class MovieViewHolder(
    private val isFavMovie: Boolean,
    private val binding: MovieItemBinding,
    private val onMovieListener: MovieAdapter.OnClickListener,
    private val onStoreInDbListener: MovieAdapter.OnStoreInDbListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: MovieResponse) {
        binding.parentView.changeTouchableAreaOfView(binding.favorite, 20)
        binding.title.text = movie.title
        if (movie.releaseDate != "") {
            visibilityVisible(binding.year)
            binding.year.text = binding.year.context.getString(
                R.string.yearOfRelease,
                movie.releaseDate.toLocalDate()?.year
            )
        } else {
            visibilityGone(binding.year)
        }
        binding.ratingBar.rating = (movie.voteAverage.div(2)).toFloat()
        binding.movieImage.loadImage(
            "${POSTER_BASE_URL}${movie.posterPath}",
            R.drawable.ic_launcher_foreground
        )
        binding.genre.text = movie.genreIds.toGenres()
        binding.movieImage.transitionName = movie.id.toString()
        itemView.setOnClickListener {
            onMovieListener.onMovieClick(adapterPosition, binding.movieImage)
        }

        binding.favorite.setOnClickListener {
            onStoreInDbListener.onStoreClick(movie)
        }
    }
}