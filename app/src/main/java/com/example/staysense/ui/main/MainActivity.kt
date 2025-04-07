package com.example.staysense.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.staysense.R
import com.example.staysense.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavbar()

    }

    private fun setupBottomNavbar(){
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    navView.visibility = View.VISIBLE
                }
                R.id.navigation_predict-> {
                    navView.visibility = View.VISIBLE
                }
                R.id.navigation_profile-> {
                    navView.visibility = View.VISIBLE
                }
                R.id.navigation_wordcloud-> {
                    navView.visibility = View.VISIBLE
                }
                else -> {
                    navView.visibility = View.GONE
                }
            }
        }

    }

}