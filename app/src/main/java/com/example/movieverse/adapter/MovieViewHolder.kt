package com.example.movieverse.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.R
import com.example.movieverse.databinding.MovieItemBinding
import com.example.movieverse.model.movie.MovieResponse
import com.example.movieverse.util.Constants.POSTER_BASE_URL
import com.example.movieverse.util.loadImage

class MovieViewHolder(
    private val binding: MovieItemBinding,
    private val onMovieListener: MovieAdapter.OnClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: MovieResponse) {
        // TODO image
        // TODO remove MovieResponse and add Movie data class
        // TODO find a way to remove context from here
        binding.title.text = movie.title
        binding.year.text = binding.year.context.getString(R.string.yearOfRelease, movie.releaseDate.substringBefore('-'))
        binding.ratingBar.rating = (movie.voteAverage / 2).toFloat()
        binding.movieImage.loadImage(
            "${POSTER_BASE_URL}${movie.posterPath}",
            R.drawable.ic_default_black
        )
        // TODO: genres need extra GET request => /genre/movie/list
        //binding.genre.text = "${movie.genreIds[0]}"

        binding.movieImage.transitionName = movie.id.toString()
        itemView.setOnClickListener {
            onMovieListener.onMovieClick(adapterPosition, binding.movieImage)
        }
    }
}