package com.example.movieverse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.movieverse.databinding.ActivityNavigationBinding

class NavigationActivity : AppCompatActivity() {

    lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
        setSupportActionBar(binding.toolBar)
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(
            binding.bottomNavView,
            navHostFragment!!.navController
        )
    }

    companion object {
        private val TAG = NavigationActivity::class.java.simpleName
    }
}