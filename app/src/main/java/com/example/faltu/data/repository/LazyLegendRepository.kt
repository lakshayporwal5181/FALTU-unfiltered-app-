package com.example.faltu.data.repository

import com.example.faltu.data.api.Content
import com.example.faltu.data.api.GeminiRequest
import com.example.faltu.data.api.GenerationConfig
import com.example.faltu.data.api.Part
import com.example.faltu.data.api.RetrofitClient
import com.example.faltu.data.models.ProductivityChallenge
import com.example.faltu.data.models.Roast
import com.example.faltu.data.models.ScreenTimeData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import android.util.Log

class LazyLegendRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val geminiService = RetrofitClient.geminiService

    companion object {
        private const val TAG = "LazyLegendRepository"
    }

    suspend fun generateRoast(screenTimeMinutes: Int, severity: String = "medium"): Result<Roast> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Not authenticated")

            Log.d(
                TAG,
                "Starting roast generation for user: $uid, screenTime: $screenTimeMinutes minutes, severity: $severity"
            )

            val prompt = when (severity) {
                "light" -> """
                    You are a playful AI that gives light, funny roasts about screen time. Keep it fun and motivating.
                    You can respond in Hindi, English, or mix of both (Hinglish) to make it more relatable.
                    
                    Roast someone who spent $screenTimeMinutes minutes on their phone today. 
                    Make it funny but encouraging!
                """.trimIndent()

                "savage" -> """
                    You are a savage roast master. Deliver brutal but hilarious roasts about excessive screen time. 
                    Be creative and harsh but never mean-spirited.
                    You can respond in Hindi, English, or mix of both (Hinglish) to make it more relatable.
                    
                    Roast someone who spent $screenTimeMinutes minutes on their phone today. 
                    Go all out with the roast!
                """.trimIndent()

                else -> """
                    You are a witty AI that roasts people about their screen time usage. Mix humor with motivation.
                    You can respond in Hindi, English, or mix of both (Hinglish) to make it more relatable.
                    
                    Roast someone who spent $screenTimeMinutes minutes on their phone today. 
                    Make it funny and motivating!
                """.trimIndent()
            }

            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(prompt))
                    )
                ),
                generationConfig = GenerationConfig(temperature = 0.9f)
            )

            Log.d(TAG, "Calling Gemini API...")
            val response = geminiService.generateContent(RetrofitClient.GEMINI_API_KEY, request)

            Log.d(TAG, "API Response Code: ${response.code()}")

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "API Error: ${response.code()} - ${response.message()}")
                Log.e(TAG, "Error Body: $errorBody")
                throw Exception("API call failed: ${response.code()} - ${response.message()}. Error: $errorBody")
            }

            val responseBody = response.body()
            if (responseBody == null) {
                Log.e(TAG, "Response body is null")
                throw Exception("Empty response from server. Please try again.")
            }

            if (responseBody.candidates.isNullOrEmpty()) {
                Log.e(TAG, "No candidates in response")
                throw Exception("Could not generate roast. Please try again.")
            }

            val firstCandidate = responseBody.candidates.firstOrNull()
            if (firstCandidate == null) {
                Log.e(TAG, "First candidate is null")
                throw Exception("Could not generate roast. Please try again.")
            }

            val roastText = firstCandidate.content.parts.firstOrNull()?.text
            if (roastText.isNullOrBlank()) {
                Log.e(TAG, "Roast text is null or empty")
                throw Exception("Received empty response from AI. Please try again.")
            }

            Log.d(TAG, "Gemini Response: $roastText")

            val roast = Roast(
                id = System.currentTimeMillis().toString(),
                userId = uid,
                roastText = roastText,
                severity = severity,
                screenTimeMinutes = screenTimeMinutes
            )

            // Save to Firestore
            firestore.collection("roasts")
                .document(roast.id)
                .set(roast)
                .await()

            Log.d(TAG, "Roast saved successfully")
            Result.success(roast)
        } catch (e: Exception) {
            Log.e(TAG, "Error generating roast: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun saveScreenTimeData(data: ScreenTimeData): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Not authenticated")
            val dataWithUser = data.copy(userId = uid)

            firestore.collection("screen_time")
                .document("${uid}_${data.date}")
                .set(dataWithUser)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving screen time data: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getScreenTimeHistory(limit: Int = 7): List<ScreenTimeData> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        return try {
            val snapshot = firestore.collection("screen_time")
                .whereEqualTo("userId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            snapshot.toObjects(ScreenTimeData::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting screen time history: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getRoastHistory(limit: Int = 10): List<Roast> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        return try {
            val snapshot = firestore.collection("roasts")
                .whereEqualTo("userId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            snapshot.toObjects(Roast::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting roast history: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun createProductivityChallenge(challenge: ProductivityChallenge): Result<ProductivityChallenge> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Not authenticated")
            val challengeWithUser = challenge.copy(
                userId = uid,
                id = System.currentTimeMillis().toString()
            )

            firestore.collection("productivity_challenges")
                .document(challengeWithUser.id)
                .set(challengeWithUser)
                .await()

            Result.success(challengeWithUser)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating productivity challenge: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getActiveChallenges(): List<ProductivityChallenge> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        return try {
            val snapshot = firestore.collection("productivity_challenges")
                .whereEqualTo("userId", uid)
                .whereEqualTo("isCompleted", false)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.toObjects(ProductivityChallenge::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting active challenges: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun updateChallengeProgress(
        challengeId: String,
        progress: Int,
        isCompleted: Boolean
    ): Result<Unit> {
        return try {
            val updates = mutableMapOf<String, Any>(
                "currentProgress" to progress,
                "isCompleted" to isCompleted
            )

            if (isCompleted) {
                updates["completedAt"] = System.currentTimeMillis()
            }

            firestore.collection("productivity_challenges")
                .document(challengeId)
                .update(updates)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating challenge progress: ${e.message}", e)
            Result.failure(e)
        }
    }
}
