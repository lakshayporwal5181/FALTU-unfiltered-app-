package com.example.faltu.ui.dategpt

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.faltu.R
import com.example.faltu.data.repository.DateGPTRepository
import com.example.faltu.databinding.ActivityDategptBinding
import kotlinx.coroutines.launch

class DateGPTActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDategptBinding
    private val repository = DateGPTRepository()
    private val LOCATION_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDategptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        checkLocationPermission()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "DateGPT 💘"

        binding.analyzeButton.setOnClickListener {
            val chatText = binding.chatInput.text.toString()
            if (chatText.isNotBlank()) {
                analyzeChatTone(chatText)
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }

        binding.createMissionButton.setOnClickListener {
            createSampleMission()
        }

        binding.upgradeProButton.setOnClickListener {
            showUpgradeDialog()
        }
    }

    private fun analyzeChatTone(chatText: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.resultCard.visibility = View.GONE

        lifecycleScope.launch {
            repository.analyzeChatTone(chatText).fold(
                onSuccess = { analysis ->
                    binding.progressBar.visibility = View.GONE
                    binding.resultCard.visibility = View.VISIBLE

                    binding.toneText.text = "Tone: ${analysis.tone.uppercase()}"
                    binding.confidenceText.text = "Confidence Level: ${analysis.confidenceLevel}%"
                    binding.feedbackText.text = analysis.feedback
                    binding.adviceText.text = analysis.funnyAdvice

                    // Set tone color
                    val toneColor = when (analysis.tone.lowercase()) {
                        "flirty" -> ContextCompat.getColor(this@DateGPTActivity, R.color.neon_pink)
                        "confident" -> ContextCompat.getColor(
                            this@DateGPTActivity,
                            R.color.neon_blue
                        )

                        "awkward" -> ContextCompat.getColor(this@DateGPTActivity, R.color.warning)
                        else -> ContextCompat.getColor(this@DateGPTActivity, R.color.text_secondary)
                    }
                    binding.toneText.setTextColor(toneColor)
                },
                onFailure = { error ->
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@DateGPTActivity,
                        "Error: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    private fun createSampleMission() {
        if (!hasLocationPermission()) {
            requestLocationPermission()
            return
        }

        Toast.makeText(this, "Creating confidence mission... 💪", Toast.LENGTH_SHORT).show()
        // TODO: Implement mission creation with current location
    }

    private fun checkLocationPermission() {
        if (!hasLocationPermission()) {
            binding.locationWarning.visibility = View.VISIBLE
            binding.locationWarning.text = "⚠️ Location permission needed for missions"
            binding.locationWarning.setOnClickListener {
                requestLocationPermission()
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.locationWarning.visibility = View.GONE
                Toast.makeText(this, "Location permission granted!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showUpgradeDialog() {
        AlertDialog.Builder(this)
            .setTitle("Upgrade to Pro 💎")
            .setMessage("₹99/month\n\n✓ Unlimited tone analysis\n✓ Advanced AI feedback\n✓ Priority support\n✓ Exclusive missions")
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
}
