package com.example.movieverse.util

import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

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

fun ImageView.loadImage(url: String, errorImg: Int) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .placeholder(errorImg)
        .error(errorImg)
        .fallback(errorImg)
        .into(this)

}

fun View.showKeyboard(context: Context?) {
    this.requestFocus()
    val inputMethodManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(
        InputMethodManager.SHOW_IMPLICIT,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

fun View.hideKeyboard(context: Context?) {
    val inputMethodManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}