package com.example.movieverse.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.R
import com.example.movieverse.databinding.CastItemBinding
import com.example.movieverse.model.movie.CastResponse
import com.example.movieverse.util.Constants.POSTER_BASE_URL
import com.example.movieverse.util.loadImage

class CastViewHolder(
    private val binding: CastItemBinding,
    private val onCastListener: CastAdapter.OnClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: CastResponse) {
        // TODO remove MovieDetailsResponse and add Movie data class
        binding.memberImage.loadImage(
            "${POSTER_BASE_URL}${cast.profilePath}",
            R.drawable.ic_launcher_foreground
        )
        binding.memberName.text = cast.name
        binding.character.text = binding.character.context?.getString(R.string.cast_character, cast.character)

        binding.memberImage.transitionName = cast.personId.toString()
        itemView.setOnClickListener {
            onCastListener.onMovieClick(adapterPosition, binding.memberImage)
        }
    }
}