package com.example.movieverse.adapter

import android.widget.ImageView

interface OnMovieListener {

    fun onMovieClick(position: Int, poster: ImageView)
}