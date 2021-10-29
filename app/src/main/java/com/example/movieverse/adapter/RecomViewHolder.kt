package com.example.movieverse.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.R
import com.example.movieverse.databinding.MovieItemBinding
import com.example.movieverse.databinding.RecommendationItemBinding
import com.example.movieverse.model.movie.MovieResponse
import com.example.movieverse.util.Constants.POSTER_BASE_URL
import com.example.movieverse.util.changeTouchableAreaOfView
import com.example.movieverse.util.loadImage
import java.math.RoundingMode

class RecomViewHolder(
    private val binding: RecommendationItemBinding,
    private val onMovieListener: RecomAdapter.OnClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: MovieResponse) {
        binding.recTitle.text = movie.title
        binding.recRating.text = movie.voteAverage.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()
        binding.recImage.loadImage(
            "${POSTER_BASE_URL}${movie.posterPath}",
            R.drawable.ic_launcher_foreground
        )
        // TODO: genres need extra GET request => /genre/movie/list
        //binding.genre.text = "${movie.genreIds[0]}"

        binding.recImage.transitionName = movie.id.toString()
        itemView.setOnClickListener {
            onMovieListener.onMovieClick(adapterPosition, binding.recImage)
        }
    }
}