package com.example.faltu.ui.lazylegend

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import com.example.faltu.data.repository.LazyLegendRepository
import com.example.faltu.databinding.ActivityLazylegendBinding
import com.example.faltu.utils.ScreenTimeManager
import kotlinx.coroutines.launch

class LazyLegendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLazylegendBinding
    private val repository = LazyLegendRepository()
    private lateinit var screenTimeManager: ScreenTimeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLazylegendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenTimeManager = ScreenTimeManager(this)

        setupUI()
        checkUsageStatsPermission()
        loadScreenTimeData()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "LazyLegend 🔥"

        binding.getRoastedButton.setOnClickListener {
            generateRoast()
        }

        binding.refreshButton.setOnClickListener {
            loadScreenTimeData()
        }

        binding.upgradeBossButton.setOnClickListener {
            showUpgradeDialog()
        }
    }

    private fun checkUsageStatsPermission() {
        if (!screenTimeManager.hasUsageStatsPermission()) {
            binding.usagePermissionWarning.visibility = View.VISIBLE
            binding.usagePermissionWarning.text = "⚠️ Grant Usage Access to track screen time"
            binding.usagePermissionWarning.setOnClickListener {
                openUsageAccessSettings()
            }
        }
    }

    private fun openUsageAccessSettings() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
    }

    private fun loadScreenTimeData() {
        if (!screenTimeManager.hasUsageStatsPermission()) {
            Toast.makeText(this, "Please grant Usage Access permission", Toast.LENGTH_LONG).show()
            openUsageAccessSettings()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val screenTimeData = screenTimeManager.getTodayScreenTime()

                binding.progressBar.visibility = View.GONE
                binding.statsCard.visibility = View.VISIBLE

                val hours = screenTimeData.totalScreenTimeMinutes / 60
                val minutes = screenTimeData.totalScreenTimeMinutes % 60

                binding.totalTimeText.text = "${hours}h ${minutes}m"
                binding.productiveTimeText.text = "${screenTimeData.productiveTimeMinutes} min"
                binding.wastedTimeText.text = "${screenTimeData.wastedTimeMinutes} min"

                // Display top apps
                val topAppsText = screenTimeData.topApps.take(5).joinToString("\n") { app ->
                    "📱 ${app.appName}: ${app.usageTimeMinutes} min"
                }
                binding.topAppsText.text = topAppsText.ifEmpty { "No data available" }

                // Save to Firestore
                repository.saveScreenTimeData(screenTimeData)

            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@LazyLegendActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun generateRoast() {
        if (!screenTimeManager.hasUsageStatsPermission()) {
            Toast.makeText(this, "Need usage permission to roast you! 😈", Toast.LENGTH_SHORT).show()
            return
        }

        binding.roastProgress.visibility = View.VISIBLE
        binding.roastCard.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val screenTimeData = screenTimeManager.getTodayScreenTime()
                val severity = when {
                    screenTimeData.totalScreenTimeMinutes > 360 -> "savage" // More than 6 hours
                    screenTimeData.totalScreenTimeMinutes > 180 -> "medium" // More than 3 hours
                    else -> "light"
                }

                repository.generateRoast(screenTimeData.totalScreenTimeMinutes, severity).fold(
                    onSuccess = { roast ->
                        binding.roastProgress.visibility = View.GONE
                        binding.roastCard.visibility = View.VISIBLE
                        binding.roastText.text = roast.roastText

                        Toast.makeText(this@LazyLegendActivity, "Roasted! 🔥", Toast.LENGTH_SHORT)
                            .show()
                    },
                    onFailure = { error ->
                        binding.roastProgress.visibility = View.GONE
                        Toast.makeText(
                            this@LazyLegendActivity,
                            "Error: ${error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            } catch (e: Exception) {
                binding.roastProgress.visibility = View.GONE
                Toast.makeText(this@LazyLegendActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showUpgradeDialog() {
        AlertDialog.Builder(this)
            .setTitle("Upgrade to Boss Mode 👑")
            .setMessage("₹79/month\n\n✓ Celebrity roast voices\n✓ Detailed analytics\n✓ Premium challenges\n✓ Custom roast levels")
            .setPositiveButton("Upgrade") { _, _ ->
                Toast.makeText(this, "Payment integration coming soon!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Maybe Later", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when user returns from settings
        if (screenTimeManager.hasUsageStatsPermission()) {
            binding.usagePermissionWarning.visibility = View.GONE
            loadScreenTimeData()
        }
    }
}
