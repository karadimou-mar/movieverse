package com.example.movieverse.util

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

fun visibilityGone(vararg view: View) {
    for (i in view.indices)
        view[i].visibility = View.GONE
}

fun visibilityVisible(vararg view: View) {
    for (i in view.indices)
        view[i].visibility = View.VISIBLE
}

fun toggleVisibilities(vararg layout: ConstraintLayout) {
    layout[0].visibility = View.VISIBLE
    layout[1].visibility = View.GONE
}