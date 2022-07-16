package com.example.movieverse.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.movieverse.databinding.FavouritesVideoDialogBinding
import com.example.movieverse.util.*
import com.example.movieverse.viewmodel.MovieViewModel
import com.example.movieverse.viewmodel.MovieViewModelUser
import com.example.movieverse.viewmodel.activityMovieViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class FavouritesVideoDialog : BottomSheetDialogFragment(), MovieViewModelUser {

    private var _binding: FavouritesVideoDialogBinding? = null
    val binding
        get() = _binding!!
    private val args: FavouritesVideoDialogArgs by navArgs()
    override val movieViewModel: MovieViewModel by activityMovieViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavouritesVideoDialogBinding
            .inflate(inflater, container, false)
            .also {
                (dialog as? BottomSheetDialog)?.customBehavior()
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel.clearVideoId()

        binding.parentView.bottomSheetHeight(requireActivity())
        (dialog as? BottomSheetDialog)?.disableManualDismiss(outside = true)
        subscribeObservers()

        binding.cancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun subscribeObservers() {
        movieViewModel.movieId.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                visibilityVisible(binding.ytPlayer)
                loadYouTube(it)
            } else {
                //nothing?
            }
        })
    }

    private fun loadYouTube(id: String) {
        binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(id, 0f)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = FavouritesVideoDialog::class.java.simpleName
    }
}