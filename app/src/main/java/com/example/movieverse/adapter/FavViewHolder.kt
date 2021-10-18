package com.example.movieverse.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.R
import com.example.movieverse.databinding.FavMovieItemBinding
import com.example.movieverse.db.MovieInDB
import com.example.movieverse.util.Constants
import com.example.movieverse.util.loadImage

class FavViewHolder(
    private val binding: FavMovieItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: MovieInDB) {
        binding.favMovieImage.loadImage(
            "${Constants.POSTER_BASE_URL}${movie.posterPath}",
            R.drawable.ic_launcher_foreground
        )
       binding.favTitle.text = movie.title
    }
}