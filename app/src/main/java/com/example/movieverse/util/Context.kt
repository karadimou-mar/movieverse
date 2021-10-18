package com.example.movieverse.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? =
    AppCompatResources.getDrawable(this, id)