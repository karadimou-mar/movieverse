package com.example.movieverse.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.R
import com.example.movieverse.databinding.CastItemBinding
import com.example.movieverse.model.movie.CastResponse
import com.example.movieverse.util.Constants.POSTER_BASE_URL
import com.example.movieverse.util.loadImage

class CastViewHolder(
    private val binding: CastItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: CastResponse, context: Context?) {
        // TODO remove MovieDetailsResponse and add Movie data class
        // TODO find a way to remove context from here
        binding.memberImage.loadImage(
            "${POSTER_BASE_URL}${cast.profilePath}",
            R.drawable.ic_launcher_foreground
        )
        binding.memberName.text = cast.name
        binding.character.text = context?.getString(R.string.cast_character, cast.character)
    }
}