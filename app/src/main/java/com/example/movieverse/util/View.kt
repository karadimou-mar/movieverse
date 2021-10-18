package com.example.movieverse.util

import android.content.Context
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import java.util.*

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

fun RecyclerView.initRecyclerViewWithCallback(
    @DrawableRes drawableRes: Int? = null,
    customAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?,
    swipeCallBack: (Int) -> Unit
) {
    drawableRes?.let {
        addDivider(it)
    }
    layoutManager = LinearLayoutManager(context)
    addSwipeLogic(swipeCallBack)
    customAdapter?.let {
        adapter = it
    }
}

private fun RecyclerView.addDivider(@DrawableRes drawableRes: Int) {
    val divider = object : DividerItemDecoration(context, LinearLayoutManager.VERTICAL) {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position == state.itemCount -1) {
                outRect.setEmpty()
            } else {
                super.getItemOffsets(outRect, view, parent, state)
            }
        }
    }
    val drawable = ContextCompat.getDrawable(context, drawableRes)
    drawable?.let {
            divider.setDrawable(it)
            addItemDecoration(divider)
    }
}

private fun RecyclerView.addSwipeLogic(swipeCallback: (Int) -> Unit) {
    val swipeHandler = object : SwipeToDeleteCallback(context) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            swipeCallback(viewHolder.adapterPosition)
        }
    }
    ItemTouchHelper(swipeHandler).attachToRecyclerView(this)
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

fun View.showSnackbar(@StringRes messageRes: Int, param: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snackBar = Snackbar.make(this, resources.getString(messageRes, param), length)
    snackBar.f()
    snackBar.show()
}

