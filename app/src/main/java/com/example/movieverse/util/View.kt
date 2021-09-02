package com.example.movieverse.util

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

fun View.animHideDown() {
    if (this.visibility == View.VISIBLE) {
        this.animate()
            .translationX(-1f)
            .alpha(0f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(500)
            .withEndAction { this.visibility = View.GONE }
    }
}

fun View.animHideUp() {
    if (this.visibility == View.VISIBLE) {
        this.animate()
            .translationX(2f)
            .alpha(0f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(500)
            .withEndAction { this.visibility = View.GONE }
    }
}

fun View.animShowUp() {
    if (this.visibility != View.VISIBLE) {
        this.animate()
            .translationY(1f)
            .alpha(1f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(500)
            .withEndAction { this.visibility = View.VISIBLE }
    }
}

fun View.animShowDown() {
    if (this.visibility != View.VISIBLE) {
        this.animate()
            .translationX(0f)
            .alpha(1f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(500)
            .withEndAction { this.visibility = View.VISIBLE }
    }
}