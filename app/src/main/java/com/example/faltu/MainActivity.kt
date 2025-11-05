package com.example.faltu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.faltu.databinding.ActivityMainBinding
import com.example.faltu.ui.auth.AuthActivity
import com.example.faltu.ui.dategpt.DateGPTActivity
import com.example.faltu.ui.lazylegend.LazyLegendActivity
import com.example.faltu.ui.profile.ProfileActivity
import com.example.faltu.ui.stats.StatsActivity
import com.example.faltu.ui.missions.MissionsActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Log.d("MainActivity", "onCreate started")

            // Initialize Firebase Auth first
            auth = FirebaseAuth.getInstance()
            Log.d("MainActivity", "Firebase Auth initialized")

            // Check if user is logged in BEFORE setting content view
            if (auth.currentUser == null) {
                Log.d("MainActivity", "No user logged in, navigating to AuthActivity")
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
                return
            }

            Log.d("MainActivity", "User logged in: ${auth.currentUser?.email}")

            // Now set the content view
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            Log.d("MainActivity", "View binding successful")

            setupUI()

        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun setupUI() {
        try {
            // Set welcome message
            val displayName =
                auth.currentUser?.displayName ?: auth.currentUser?.email?.substringBefore("@")
                ?: "User"
            binding.welcomeText.text = "Welcome, $displayName!"

            // Setup toolbar
            setSupportActionBar(binding.toolbar)

            // DateGPT Card click
            binding.dateGptCard.setOnClickListener {
                try {
                    startActivity(Intent(this, DateGPTActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error starting DateGPT", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            // LazyLegend Card click
            binding.lazyLegendCard.setOnClickListener {
                try {
                    startActivity(Intent(this, LazyLegendActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error starting LazyLegend", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            // Bottom navigation
            binding.bottomNavigation.selectedItemId = R.id.nav_home
            binding.bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        // Already on home
                        true
                    }

                    R.id.nav_stats -> {
                        startActivity(Intent(this, StatsActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        true
                    }

                    R.id.nav_missions -> {
                        startActivity(Intent(this, MissionsActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        true
                    }

                    R.id.nav_profile -> {
                        startActivity(Intent(this, ProfileActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        true
                    }

                    else -> false
                }
            }

            Log.d("MainActivity", "UI setup complete")

        } catch (e: Exception) {
            Log.e("MainActivity", "Error in setupUI", e)
            Toast.makeText(this, "Error setting up UI: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}