package com.example.movieverse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movieverse.databinding.ActivityNavigationBinding

class NavigationActivity : AppCompatActivity() {

    lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        private val TAG = NavigationActivity::class.java.simpleName
    }
}