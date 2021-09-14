package com.example.movieverse.util

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

fun visibilityGone(vararg layout: ConstraintLayout) {
    for (i in layout.indices)
        layout[i].visibility = View.GONE
}

fun visibilityNotGone(vararg layout: ConstraintLayout) {
    for (i in layout.indices)
        layout[i].visibility = View.VISIBLE
}

fun toggleVisibilities(vararg layout: ConstraintLayout) {
    layout[0].visibility = View.VISIBLE
    layout[1].visibility = View.GONE
}