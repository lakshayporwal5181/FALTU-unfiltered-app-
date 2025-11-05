package com.example.faltu.ui.missions

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.faltu.MainActivity
import com.example.faltu.R
import com.example.faltu.databinding.ActivityMissionsBinding
import com.example.faltu.ui.profile.ProfileActivity
import com.example.faltu.ui.stats.StatsActivity
import com.google.firebase.auth.FirebaseAuth

class MissionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMissionsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupUI()
        setupBottomNavigation()
    }

    private fun setupUI() {
        // Set toolbar
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // Missions are displayed statically in the layout for now
        // In a real app, you would load missions from Firebase/database
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_missions
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                    true
                }

                R.id.nav_stats -> {
                    startActivity(Intent(this, StatsActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                    true
                }

                R.id.nav_missions -> {
                    // Already here
                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                    true
                }

                else -> false
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
