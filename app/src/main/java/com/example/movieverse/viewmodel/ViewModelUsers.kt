package com.example.movieverse.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.movieverse.viewmodel.ViewModelFactories.searchViewModelFactory

interface SearchViewModelUser {
    val searchViewModel: SearchViewModel
}

fun <T> T.activitySearchViewModel()
        where T : Fragment, T : SearchViewModelUser =
    activityViewModels<SearchViewModel> {
        searchViewModelFactory ?: defaultMovieViewModelFactory()
    }

object ViewModelFactories {
    var searchViewModelFactory: ViewModelFactory<SearchViewModel>? = null
}