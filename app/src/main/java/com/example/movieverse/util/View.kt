package com.example.movieverse.util

import android.content.Context
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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

fun RecyclerView.initRecyclerView(
    @DrawableRes drawableRes: Int? = null,
    customAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?
) {
    // TODO: maybe add the divider here and remove from layout
    layoutManager = LinearLayoutManager(context)
    customAdapter?.let {
        adapter = it
    }
}

fun RecyclerView.initHorizontalRecyclerView(
    @DrawableRes drawableRes: Int? = null,
    customAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?
) {
    // TODO: maybe add the divider here and remove from layout
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    customAdapter?.let {
        adapter = it
    }
}

fun View.changeTouchableAreaOfView(clickableArea: View, extraSpace: Int) {
    //val parent = view?. as View
    this.post {
        val touchableArea = Rect()
        clickableArea.getHitRect(touchableArea)
        touchableArea.top -= extraSpace
        touchableArea.bottom += extraSpace
        touchableArea.left -= extraSpace
        touchableArea.right += extraSpace
        this.touchDelegate = TouchDelegate(touchableArea, clickableArea)
    }
}
