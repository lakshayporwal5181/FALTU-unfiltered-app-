package com.example.faltu.ui.stats

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.faltu.MainActivity
import com.example.faltu.R
import com.example.faltu.databinding.ActivityStatsBinding
import com.example.faltu.ui.profile.ProfileActivity
import com.example.faltu.ui.missions.MissionsActivity
import com.google.firebase.auth.FirebaseAuth

class StatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
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

        // Load stats data (placeholder values for now)
        loadStats()
    }

    private fun loadStats() {
        // Overall stats
        binding.totalMissionsValue.text = "0"
        binding.daysActiveValue.text = "1"
        binding.currentStreakValue.text = "0 🔥"

        // DateGPT stats
        binding.confidenceLevelValue.text = "0%"
        binding.chatsAnalyzedValue.text = "0"
        binding.confidenceMissionsValue.text = "0"

        // LazyLegend stats
        binding.productivityLevelValue.text = "0%"
        binding.screenTimeValue.text = "0h 0m"
        binding.roastsReceivedValue.text = "0"
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_stats
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                    true
                }

                R.id.nav_stats -> {
                    // Already here
                    true
                }

                R.id.nav_missions -> {
                    startActivity(Intent(this, MissionsActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
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
