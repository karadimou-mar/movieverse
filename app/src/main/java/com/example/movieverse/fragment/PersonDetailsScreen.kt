package com.example.movieverse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.movieverse.NavigationActivity
import com.example.movieverse.R
import com.example.movieverse.adapter.HorizMovieAdapter
import com.example.movieverse.databinding.PersonDetailsScreenBinding
import com.example.movieverse.util.*
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
    // person's filmography as actor
    private var movieAdapter: HorizMovieAdapter? = null
    // person's filmography as producer
    private var prodAdapter: HorizMovieAdapter? = null

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

        initAdapters()
        subscribeObservers()
        getCastDetailsById(args.selectedPersonId)

        // for shared element transition
        binding.personImage.transitionName = args.selectedPersonImage
    }

    private fun subscribeObservers() {
        castViewModel.castDetailsResult.observe(viewLifecycleOwner, {
            // TODO: check for when it == null
            if (it != null) {
                visibilityVisible(binding.parentLyt)
                binding.personImage.loadImage(
                    "${Constants.POSTER_BASE_URL}${args.selectedPersonImage}",
                    R.drawable.ic_launcher_foreground
                )
            }

            if (it.birthday != null && it.placeOfBirth != null) {
                visibilityVisible(binding.birthLabel, binding.birthDate, binding.birthPlace)
                binding.birthDate.text = binding.birthDate.context.getString(
                    R.string.person_birth_date,
                    it.birthday.toLocalDate()?.dayOfMonth,
                    it.birthday.toLocalDate()?.month?.value,
                    it.birthday.toLocalDate()?.year
                )
                binding.birthPlace.text = binding.birthPlace.context.getString(R.string.person_birth_place, it.placeOfBirth)
            } else {
                visibilityGone(binding.birthLabel, binding.birthDate, binding.birthPlace)
            }

            if (it.deathDay != null) {
                visibilityVisible(binding.deathLabel, binding.deathDate)
                binding.deathDate.text = it.deathDay
            } else {
                visibilityGone(binding.deathLabel, binding.deathDate)
            }

            if (it.biography != "") {
                visibilityVisible(binding.bioLabel)
                binding.biography.text = it.biography
            } else {
                visibilityGone(binding.bioLabel, binding.biography)
            }
        })

        castViewModel.movieCreditsCastResult.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                visibilityVisible(binding.filmographyLabel, binding.actingLabel, binding.actingView)
                binding.actingLabel.text = castViewModel.genderResult.value.toGender()
                movieAdapter?.submit(it.sortByPopularity())
            } else {
                visibilityGone(binding.filmographyLabel, binding.actingLabel, binding.actingView)
                movieAdapter?.submit(emptyList())
            }
        })

        castViewModel.prodsResult.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty() ) {
                visibilityVisible(binding.filmographyLabel, binding.prodLabel, binding.prodView)
                prodAdapter?.submit(it)
            }else {
                if (castViewModel.movieCreditsCastResult.value.isNullOrEmpty())
                    visibilityVisible(binding.filmographyLabel)
                visibilityGone(binding.prodLabel, binding.prodView)
                prodAdapter?.submit(emptyList())
            }
        })

        castViewModel.showProgressBar.observe(viewLifecycleOwner, {
            (activity as NavigationActivity).showProgressBar(it)
        })
    }

    private fun initAdapters() {
        movieAdapter = HorizMovieAdapter(movieItemListener)
        binding.moviesList.initHorizontalRecyclerView(customAdapter = movieAdapter)
        prodAdapter = HorizMovieAdapter(prodItemListener)
        binding.prodsList.initHorizontalRecyclerView(customAdapter = prodAdapter)
    }

    private val movieItemListener = HorizMovieAdapter.OnClickListener { position, image ->
        val movieId = movieAdapter?.getSelectedMovie(position)?.id
        val imagePoster = movieAdapter?.getSelectedMovie(position)?.posterPath.valueOrEmpty()
        val title = movieAdapter?.getSelectedMovie(position)?.title.valueOrDashes()
        val releaseDate = movieAdapter?.getSelectedMovie(position)?.releaseDate.valueOrEmpty()
        val rating = movieAdapter?.getSelectedMovie(position)?.voteAverage?.div(2).valueOrEmpty()

        val direction: NavDirections? =
            movieId?.let {
                PersonDetailsScreenDirections.actionPersonDetailsScreenToMovieDetailsScreen(
                    selectedMovieId = it,
                    selectedMoviePoster = imagePoster,
                    selectedMovieTitle = title,
                    selectedReleaseDate = releaseDate,
                    selectedRating = rating
                )
            }
        val extras = FragmentNavigatorExtras(
            image to imagePoster
        )
        direction?.let { findNavController().navigate(it, extras) }
    }

    private val prodItemListener = HorizMovieAdapter.OnClickListener { position, image ->
        val movieId = prodAdapter?.getSelectedMovie(position)?.id
        val imagePoster = prodAdapter?.getSelectedMovie(position)?.posterPath.valueOrEmpty()
        val title = prodAdapter?.getSelectedMovie(position)?.title.valueOrDashes()
        val releaseDate = prodAdapter?.getSelectedMovie(position)?.releaseDate.valueOrEmpty()
        val rating = prodAdapter?.getSelectedMovie(position)?.voteAverage?.div(2).valueOrEmpty()

        val direction: NavDirections? =
            movieId?.let {
                PersonDetailsScreenDirections.actionPersonDetailsScreenToMovieDetailsScreen(
                    selectedMovieId = it,
                    selectedMoviePoster = imagePoster,
                    selectedMovieTitle = title,
                    selectedReleaseDate = releaseDate,
                    selectedRating = rating
                )
            }
        val extras = FragmentNavigatorExtras(
            image to imagePoster
        )
        direction?.let { findNavController().navigate(it, extras) }
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
        private val TAG = PersonDetailsScreen::class.java.simpleName
    }
}