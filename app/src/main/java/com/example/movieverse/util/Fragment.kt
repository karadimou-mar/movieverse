package com.example.movieverse.util

import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(@IdRes destinationId: Int) {
    try {
        findNavController().navigate(destinationId)
    } catch (error: Throwable) {
        error.message?.let { Log.w("Fragment:", it) }
    }
}