package com.example.movieverse.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieverse.databinding.CinemasScreenBinding
import com.google.android.gms.maps.*
import com.example.movieverse.R
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.example.movieverse.adapter.CustomInfoWindowAdapter
import com.example.movieverse.viewmodel.CinemaViewModelUser
import com.example.movieverse.viewmodel.activityCinemaViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.movieverse.NavigationActivity
import com.google.android.gms.maps.model.BitmapDescriptor

class CinemasScreen : Fragment(), OnMapReadyCallback, CinemaViewModelUser {

    private var mMap: GoogleMap? = null

    // creating array list for adding all cinemas locations(lat,lng)
    private val locationsMap = mutableMapOf<Double, Double>()

    private var _binding: CinemasScreenBinding? = null
    private val binding
        get() = _binding!!

    override val cinemaViewModel by activityCinemaViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CinemasScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        subscribeObservers()
        cinemaViewModel.getCinemasNearby()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // MovieGlu API is offering sandbox testing environment for unlimited requests
        // only for dummy cinemas on South Africa(-22.0;14.0), so we are going to use those for development
        // Showcase will perform with real data (Germany territory)
        val demoRegion = LatLng(-22.0, 14.0)

        mMap?.setInfoWindowAdapter(CustomInfoWindowAdapter(requireActivity()))
        mMap?.addMarker(
            MarkerOptions()
                .icon(bitmapFromVector(binding.root.context, R.drawable.ic_pin))
                .position(demoRegion)
                .title("You are here!")
        )
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(demoRegion, 7.0f))

        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.mas_retro_style
            )
        )
    }

    private fun subscribeObservers() {
        cinemaViewModel.cinemaResult.observe(viewLifecycleOwner, { cinemas ->
            for (i in cinemas.indices) {
                locationsMap[cinemas[i].lat] = cinemas[i].lng
                mMap?.addMarker(
                    MarkerOptions()
                        .icon(bitmapFromVector(binding.root.context, R.drawable.ic_cinema_map))
                        .position(LatLng(cinemas[i].lat, cinemas[i].lng))
                        .title(cinemas[i].cinema_name)
                        .snippet(cinemas[i].address)
                )
            }
        })

        cinemaViewModel.showProgressBar.observe(viewLifecycleOwner, {
            (activity as NavigationActivity).showProgressBar(it)
        })
    }

    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = CinemasScreen::class.java.simpleName
    }
}