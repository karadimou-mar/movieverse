package com.example.movieverse.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.movieverse.viewmodel.ViewModelFactories.actorViewModelFactory
import com.example.movieverse.viewmodel.ViewModelFactories.movieViewModelFactory
import com.example.movieverse.viewmodel.ViewModelFactories.searchViewModelFactory

interface SearchViewModelUser {
    val searchViewModel: SearchViewModel
}

interface MovieViewModelUser {
    val movieViewModel: MovieViewModel
}

interface ActorViewModelUser {
    val actorViewModel: ActorViewModel
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

fun <T> T.activityActorViewModel()
        where T : Fragment, T : ActorViewModelUser =
    activityViewModels<ActorViewModel> {
        actorViewModelFactory ?: defaultActorViewModelFactory(requireContext())
    }

object ViewModelFactories {
    var searchViewModelFactory: ViewModelFactory<SearchViewModel>? = null
    var movieViewModelFactory: ViewModelFactory<MovieViewModel>? = null
    var actorViewModelFactory: ViewModelFactory<ActorViewModel>? = null
}