package com.example.movieverse.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.R
import com.example.movieverse.databinding.MovieItemBinding
import com.example.movieverse.model.movie.MovieResponse
import com.example.movieverse.util.Constants.POSTER_BASE_URL
import com.example.movieverse.util.loadImage

class MovieViewHolder(
    private val binding: MovieItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: MovieResponse, context: Context) {
        // TODO image
        // TODO remove MovieResponse and add Movie data class
        binding.title.text = movie.title
        binding.year.text = context.getString(R.string.yearOfRelease, movie.releaseDate.substringBefore('-'))
        binding.ratingBar.rating = (movie.voteAverage / 2).toFloat()
        binding.movieImage.loadImage("${POSTER_BASE_URL}${movie.posterPath}", R.drawable.ic_launcher_foreground)
        // TODO: genres need extra GET request => /genre/movie/list
        //binding.genre.text = "${movie.genreIds[0]}"
    }
}