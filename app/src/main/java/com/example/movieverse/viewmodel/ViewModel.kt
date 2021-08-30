package com.example.movieverse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <T: ViewModel> factory(creator: () -> T): ViewModelFactory<T> =
    object : ViewModelFactory<T>() {
        override fun create(): T = creator()
    }


@Suppress("UNCHECKED_CAST")
abstract class ViewModelFactory<T: ViewModel> : ViewModelProvider.Factory {
    abstract fun create(): T

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return create() as T
    }
}