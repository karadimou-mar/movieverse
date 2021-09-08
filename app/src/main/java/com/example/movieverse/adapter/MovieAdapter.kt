package com.example.movieverse.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.databinding.MovieItemBinding
import com.example.movieverse.model.movie.MovieResponse

class MovieAdapter(
    private val movies: List<MovieResponse>? = null,
    private val context: Context?,
    private val onMovieListener: OnMovieListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onMovieListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                movies?.get(position)?.let { holder.bind(it, context) }
            }
        }
    }

    override fun getItemCount(): Int = movies?.size ?: 0

    fun getSelectedMovieId(position: Int): MovieResponse {
        if (movies?.isNotEmpty() == true) {
            return movies[position]
        }
        return MovieResponse(
            -1, "", false,
            "", "", emptyList(), "", "",
            "", "", 0.0, -1, false, 0.0
        )
    }
}
