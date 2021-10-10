package com.example.movieverse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.databinding.MovieItemBinding
import com.example.movieverse.model.movie.MovieResponse

class MovieAdapter(
    private val onMovieListener: OnClickListener,
    private val onShareListener: OnShareListener
) :
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
        return MovieViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onMovieListener,
            onShareListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submit(list: List<MovieResponse>) {
        differ.submitList(list)
    }

    fun getSelectedMovie(position: Int): MovieResponse {
        if (differ.currentList.isNotEmpty()) {
            return differ.currentList[position]
        }
        return MovieResponse(
            -1, "", false,
            "", "", emptyList(), "", "",
            "", "", 0.0, -1, false, 0.0
        )
    }

    class OnClickListener(
        val clickListener: (Int, ImageView) -> Unit
    ) {
        fun onMovieClick(
            position: Int,
            poster: ImageView
        ) = clickListener(position, poster)
    }

    class OnShareListener(
        val shareListener: (Int) -> Unit
    ) {
        fun onShareBtnClick(
            movieId: Int?
        ) = movieId?.let { shareListener(it) }
    }
}
