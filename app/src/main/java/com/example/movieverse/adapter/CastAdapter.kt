package com.example.movieverse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.databinding.CastItemBinding
import com.example.movieverse.model.movie.CastResponse

class CastAdapter(
    private val onCastListener: OnClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CastResponse>() {
        override fun areItemsTheSame(oldItem: CastResponse, newItem: CastResponse): Boolean {
            return oldItem.castId == newItem.castId
        }

        override fun areContentsTheSame(oldItem: CastResponse, newItem: CastResponse): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CastViewHolder(
            CastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCastListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CastViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submit(list: List<CastResponse>) {
        differ.submitList(list)
    }

    fun getSelectedPerson(position: Int): CastResponse {
        if (differ.currentList.isNotEmpty()) {
            return differ.currentList[position]
        }
        return CastResponse(
            -1, -1,"", "",
            "", ""
        )
    }

    class OnClickListener(val clickListener: (Int) -> Unit) {
        fun onMovieClick(
            position: Int
        ) = clickListener(position)
    }
}
