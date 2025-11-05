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
import com.example.faltu.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val authRepository = AuthRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("SignUpFragment", "onCreateView started")
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        Log.d("SignUpFragment", "View binding successful")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("SignUpFragment", "onViewCreated started")

        try {
            binding.signUpButton.setOnClickListener {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val displayName = binding.displayNameEditText.text.toString()

                if (email.isNotBlank() && password.isNotBlank() && displayName.isNotBlank()) {
                    signUp(email, password, displayName)
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            Log.d("SignUpFragment", "Button listener set successfully")

        } catch (e: Exception) {
            Log.e("SignUpFragment", "Error in onViewCreated", e)
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun signUp(email: String, password: String, displayName: String) {
        Log.d("SignUpFragment", "Sign up attempt for: $email")

        lifecycleScope.launch {
            try {
                authRepository.signUp(email, password, displayName).fold(
                    onSuccess = {
                        Log.d("SignUpFragment", "Sign up successful")
                        Toast.makeText(requireContext(), "Account created!", Toast.LENGTH_SHORT)
                            .show()
                        (activity as? AuthActivity)?.navigateToMain()
                    },
                    onFailure = { error ->
                        Log.e("SignUpFragment", "Sign up failed", error)
                        Toast.makeText(
                            requireContext(),
                            "Sign up failed: ${error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            } catch (e: Exception) {
                Log.e("SignUpFragment", "Unexpected error during sign up", e)
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
        Log.d("SignUpFragment", "onDestroyView called")
        _binding = null
    }
}
