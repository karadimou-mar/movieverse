package com.example.movieverse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.movieverse.NavigationActivity
import com.example.movieverse.databinding.CastDetailsScreenBinding
import com.example.movieverse.viewmodel.CastViewModel
import com.example.movieverse.viewmodel.CastViewModelUser
import com.example.movieverse.viewmodel.activityCastViewModel

class CastDetailsScreen : Fragment(), CastViewModelUser {

    private var _binding: CastDetailsScreenBinding? = null
    private val binding
        get() = _binding!!

    override val castViewModel: CastViewModel by activityCastViewModel()
    private val args: CastDetailsScreenArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CastDetailsScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        getCastDetailsById(args.selectedPersonId)
    }

    private fun subscribeObservers() {
        castViewModel.castDetailsResult.observe(viewLifecycleOwner, {
            binding.castDetails.text = it.name
        })
    }

    private fun getCastDetailsById(personId: Int) {
        (activity as NavigationActivity).showProgressBar(true)
        castViewModel.getCastDetailsById(personId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = CastDetailsScreen::class.java.simpleName
    }
}