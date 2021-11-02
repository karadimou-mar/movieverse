package com.example.movieverse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.databinding.HorizMovieItemBinding
import com.example.movieverse.model.movie.MovieResponse

class HorizMovieAdapter(
    private val onMovieListener: OnClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieResponse>() {
        override fun areItemsTheSame(oldItem: MovieResponse, newItem: MovieResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieResponse, newItem: MovieResponse): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HorMovieViewHolder(
            HorizMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onMovieListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HorMovieViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submit(list: List<MovieResponse>) {
        differ.submitList(list)
    }

    fun getSelectedMovie(position: Int): MovieResponse? {
        return differ.currentList[position]
    }

    class OnClickListener(
        val clickListener: (Int, ImageView) -> Unit
    ) {
        fun onMovieClick(
            position: Int,
            poster: ImageView
        ) = clickListener(position, poster)
    }
}
