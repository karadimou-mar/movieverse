package com.example.movieverse.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.databinding.CastItemBinding
import com.example.movieverse.model.movie.CastResponse

class CastAdapter(
    private val cast: List<CastResponse>? = null,
    private val context: Context?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CastViewHolder(
            CastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CastViewHolder -> {
                cast?.get(position)?.let { holder.bind(it, context) }
            }
        }
    }

    override fun getItemCount(): Int = cast?.size ?: 0
}
