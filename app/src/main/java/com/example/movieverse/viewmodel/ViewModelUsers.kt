package com.example.movieverse.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.movieverse.viewmodel.ViewModelFactories.castViewModelFactory
import com.example.movieverse.viewmodel.ViewModelFactories.cinemaViewModelFactory
import com.example.movieverse.viewmodel.ViewModelFactories.movieViewModelFactory
import com.example.movieverse.viewmodel.ViewModelFactories.searchViewModelFactory

interface SearchViewModelUser {
    val searchViewModel: SearchViewModel
}

interface MovieViewModelUser {
    val movieViewModel: MovieViewModel
}

interface CastViewModelUser {
    val castViewModel: CastViewModel
}

interface CinemaViewModelUser {
    val cinemaViewModel: CinemaViewModel
}

fun <T> T.activitySearchViewModel()
        where T : Fragment, T : SearchViewModelUser =
    activityViewModels<SearchViewModel> {
        searchViewModelFactory ?: defaultSearchViewModelFactory(requireContext())
    }

fun <T> T.activityMovieViewModel()
        where T : Fragment, T : MovieViewModelUser =
    activityViewModels<MovieViewModel> {
        movieViewModelFactory ?: defaultMovieViewModelFactory(requireContext())
    }

fun <T> T.activityCastViewModel()
        where T : Fragment, T : CastViewModelUser =
    activityViewModels<CastViewModel> {
        castViewModelFactory ?: defaultCastViewModelFactory(requireContext())
    }

fun <T> T.activityCinemaViewModel()
        where T : Fragment, T : CinemaViewModelUser =
    activityViewModels<CinemaViewModel> {
        cinemaViewModelFactory ?: defaultCinemaViewModelFactory()
    }

object ViewModelFactories {
    var searchViewModelFactory: ViewModelFactory<SearchViewModel>? = null
    var movieViewModelFactory: ViewModelFactory<MovieViewModel>? = null
    var castViewModelFactory: ViewModelFactory<CastViewModel>? = null
    var cinemaViewModelFactory: ViewModelFactory<CinemaViewModel>? = null
}