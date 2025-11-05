package com.example.faltu.data.repository

import com.example.faltu.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun signUp(email: String, password: String, displayName: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("User creation failed")

            val user = User(
                uid = firebaseUser.uid,
                email = email,
                displayName = displayName,
                createdAt = System.currentTimeMillis()
            )

            // Save user to Firestore
            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(user)
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("Sign in failed")

            // Get user from Firestore
            val snapshot = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()

            val user = snapshot.toObject(User::class.java) ?: throw Exception("User not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun getCurrentUserData(): User? {
        val uid = currentUser?.uid ?: return null
        return try {
            val snapshot = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(user.uid)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
