package com.example.movieverse.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.viewholder.MovieViewHolder
import com.example.movieverse.databinding.MovieItemBinding
import com.example.movieverse.model.movie.MovieResponse

class MovieAdapter(private val movies: List<MovieResponse>? = null, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//    private val movies: List<MovieResponse>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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
}
