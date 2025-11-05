package com.example.faltu.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.faltu.MainActivity
import com.example.faltu.databinding.ActivityAuthBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Log.d("AuthActivity", "onCreate started")

            // Check if user is already logged in
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser != null) {
                Log.d("AuthActivity", "User already logged in, navigating to MainActivity")
                navigateToMain()
                return
            }

            binding = ActivityAuthBinding.inflate(layoutInflater)
            setContentView(binding.root)

            Log.d("AuthActivity", "View binding successful")

            setupViewPager()

            Log.d("AuthActivity", "ViewPager setup complete")

        } catch (e: Exception) {
            Log.e("AuthActivity", "Error in onCreate", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun setupViewPager() {
        try {
            val adapter = AuthPagerAdapter(this)
            binding.viewPager.adapter = adapter

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Sign In"
                    1 -> "Sign Up"
                    else -> ""
                }
            }.attach()

            Log.d("AuthActivity", "TabLayout and ViewPager configured")

        } catch (e: Exception) {
            Log.e("AuthActivity", "Error setting up ViewPager", e)
            Toast.makeText(this, "Error setting up authentication: ${e.message}", Toast.LENGTH_LONG)
                .show()
            e.printStackTrace()
        }
    }

    fun navigateToMain() {
        try {
            Log.d("AuthActivity", "Navigating to MainActivity")
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("AuthActivity", "Error navigating to MainActivity", e)
            Toast.makeText(this, "Error navigating: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    inner class AuthPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            Log.d("AuthActivity", "Creating fragment at position: $position")
            return when (position) {
                0 -> SignInFragment()
                1 -> SignUpFragment()
                else -> SignInFragment()
            }
        }
    }
}
