package com.example.movieverse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.movieverse.NavigationActivity
import com.example.movieverse.R
import com.example.movieverse.databinding.PersonDetailsScreenBinding
import com.example.movieverse.util.Constants
import com.example.movieverse.util.loadImage
import com.example.movieverse.viewmodel.CastViewModel
import com.example.movieverse.viewmodel.CastViewModelUser
import com.example.movieverse.viewmodel.activityCastViewModel
import java.util.concurrent.TimeUnit

class PersonDetailsScreen : Fragment(), CastViewModelUser {

    private var _binding: PersonDetailsScreenBinding? = null
    private val binding
        get() = _binding!!

    override val castViewModel: CastViewModel by activityCastViewModel()
    private val args: PersonDetailsScreenArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PersonDetailsScreenBinding.inflate(inflater, container, false)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        getCastDetailsById(args.selectedPersonId)
        getPersonMoviesById(args.selectedPersonId)

        // for shared element transition
        binding.personImage.transitionName = args.selectedPersonImage
    }

    private fun subscribeObservers() {
        castViewModel.castDetailsResult.observe(viewLifecycleOwner, {
            // TODO: check for when it == null
            if (it != null) {
                binding.view.visibility = View.VISIBLE
                binding.personImage.loadImage(
                    "${Constants.POSTER_BASE_URL}${args.selectedPersonImage}",
                    R.drawable.ic_launcher_foreground
                )
                //TODO: format the date
                binding.birthDate.text = it.birthday
                binding.birthPlace.text = binding.birthPlace.context.getString(R.string.person_birth_place, it.placeOfBirth)
                binding.deathDate.text = it.deathDay
                binding.biography.text = it.biography
            }
        })

        castViewModel.showProgressBar.observe(viewLifecycleOwner, {
            (activity as NavigationActivity).showProgressBar(it)
        })
    }

    private fun getCastDetailsById(personId: Int) {
        (activity as NavigationActivity).showProgressBar(true)
        castViewModel.getCastDetailsById(personId)
    }

    private fun getPersonMoviesById(personId: Int) {
        (activity as NavigationActivity).showProgressBar(true)
        castViewModel.getPersonMoviesById(personId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = PersonDetailsScreen::class.java.simpleName
    }
}