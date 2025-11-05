package com.example.faltu.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.faltu.data.repository.AuthRepository
import com.example.faltu.databinding.FragmentSignInBinding
import kotlinx.coroutines.launch

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val authRepository = AuthRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("SignInFragment", "onCreateView started")
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        Log.d("SignInFragment", "View binding successful")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("SignInFragment", "onViewCreated started")

        try {
            binding.signInButton.setOnClickListener {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                if (email.isNotBlank() && password.isNotBlank()) {
                    signIn(email, password)
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            Log.d("SignInFragment", "Button listener set successfully")

        } catch (e: Exception) {
            Log.e("SignInFragment", "Error in onViewCreated", e)
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun signIn(email: String, password: String) {
        Log.d("SignInFragment", "Sign in attempt for: $email")

        lifecycleScope.launch {
            try {
                authRepository.signIn(email, password).fold(
                    onSuccess = {
                        Log.d("SignInFragment", "Sign in successful")
                        (activity as? AuthActivity)?.navigateToMain()
                    },
                    onFailure = { error ->
                        Log.e("SignInFragment", "Sign in failed", error)
                        Toast.makeText(
                            requireContext(),
                            "Sign in failed: ${error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            } catch (e: Exception) {
                Log.e("SignInFragment", "Unexpected error during sign in", e)
                Toast.makeText(
                    requireContext(),
                    "Unexpected error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("SignInFragment", "onDestroyView called")
        _binding = null
    }
}
