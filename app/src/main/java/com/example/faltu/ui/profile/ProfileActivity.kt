package com.example.faltu.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.faltu.MainActivity
import com.example.faltu.R
import com.example.faltu.databinding.ActivityProfileBinding
import com.example.faltu.ui.auth.AuthActivity
import com.example.faltu.ui.stats.StatsActivity
import com.example.faltu.ui.missions.MissionsActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
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

        // Load user data
        val user = auth.currentUser
        val displayName = user?.displayName ?: user?.email?.substringBefore("@") ?: "User"
        val email = user?.email ?: "No email"

        binding.userNameText.text = displayName
        binding.userEmailText.text = email

        // Set placeholder stats
        binding.totalMissionsText.text = "0"
        binding.confidenceScoreText.text = "0"
        binding.productivityScoreText.text = "0"

        // Setup button listeners
        binding.editProfileButton.setOnClickListener {
            Toast.makeText(this, "Edit Profile - Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.notificationsButton.setOnClickListener {
            Toast.makeText(this, "Notifications Settings - Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.aboutButton.setOnClickListener {
            showAboutDialog()
        }

        binding.logoutButton.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_profile
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
                    startActivity(Intent(this, MissionsActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                    true
                }

                R.id.nav_profile -> {
                    // Already here
                    true
                }

                else -> false
            }
        }
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("About FALTU")
            .setMessage(
                "FALTU - Fun, AI & Life Together, Unfiltered 😜\n\n" +
                        "Version 1.0\n\n" +
                        "Your AI-powered companion for dating confidence and productivity!"
            )
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun performLogout() {
        auth.signOut()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
