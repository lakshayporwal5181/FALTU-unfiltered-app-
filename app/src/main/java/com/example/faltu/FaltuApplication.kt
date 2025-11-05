package com.example.faltu

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FaltuApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            // Initialize Firebase
            FirebaseApp.initializeApp(this)
            Log.d("FaltuApp", "Firebase initialized successfully")

            // Pre-initialize Firebase services
            FirebaseAuth.getInstance()
            FirebaseFirestore.getInstance()
            Log.d("FaltuApp", "Firebase services pre-initialized")

        } catch (e: Exception) {
            Log.e("FaltuApp", "Error initializing Firebase", e)
            e.printStackTrace()
        }
    }
}
