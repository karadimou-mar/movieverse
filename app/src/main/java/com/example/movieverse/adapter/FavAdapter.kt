package com.example.movieverse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.databinding.FavMovieItemBinding
import com.example.movieverse.db.MovieInDB

class FavAdapter(
    private val onFavMovieListener: OnClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var deletedMovie: MovieInDB? = null
    var deletedPosition: Int? = null

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieInDB>() {
        override fun areItemsTheSame(oldItem: MovieInDB, newItem: MovieInDB): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieInDB, newItem: MovieInDB): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FavViewHolder(
            FavMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onFavMovieListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FavViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submit(list: List<MovieInDB>?) {
        differ.submitList(list)
    }

    fun deleteMovieFromDb(position: Int) {
        deletedMovie = differ.currentList[position]
        deletedPosition = position
    }

    class OnClickListener(
        val clickListener: (Int) -> Unit
    ) {
        fun onFavMovieListener(
            // this is for youtube loading
            movieId: Int
        ) = clickListener(movieId)
    }
}
