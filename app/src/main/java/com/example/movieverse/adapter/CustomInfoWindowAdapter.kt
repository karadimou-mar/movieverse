package com.example.movieverse.adapter

import com.google.android.gms.maps.model.Marker
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import com.example.movieverse.R
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import android.widget.TextView
import com.example.movieverse.databinding.CustomInfoWindowBinding

class CustomInfoWindowAdapter(private val context: Activity) : InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val binding = CustomInfoWindowBinding.inflate(LayoutInflater.from(context))
        binding.cinemaTitle.text = marker.title
        binding.cinemaAddress.text = marker.snippet
        return binding.root
    }
}

