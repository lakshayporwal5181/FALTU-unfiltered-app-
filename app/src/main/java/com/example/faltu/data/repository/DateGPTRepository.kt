package com.example.faltu.data.repository

import com.example.faltu.data.api.Content
import com.example.faltu.data.api.GeminiRequest
import com.example.faltu.data.api.Part
import com.example.faltu.data.api.RetrofitClient
import com.example.faltu.data.models.ChatAnalysis
import com.example.faltu.data.models.ConfidenceMission
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import android.util.Log

class DateGPTRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val geminiService = RetrofitClient.geminiService

    companion object {
        private const val TAG = "DateGPTRepository"
    }

    suspend fun analyzeChatTone(chatText: String): Result<ChatAnalysis> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Not authenticated")

            Log.d(TAG, "Starting chat analysis for user: $uid")

            val prompt = """
                You are a fun and witty dating coach AI. Analyze the following chat message and provide feedback.
                The message can be in any language (Hindi, English, or any other language).
                
                Analyze the following chat message and provide:
                1. Tone (flirty/awkward/confident/boring/cringe)
                2. Confidence level (0-100)
                3. Constructive feedback (respond in the same language as the input)
                4. Funny advice to improve (respond in the same language as the input)

                Chat message: "$chatText"

                Respond in JSON format:
                {
                  "tone": "string",
                  "confidenceLevel": number,
                  "feedback": "string",
                  "funnyAdvice": "string"
                }
            """.trimIndent()

            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(prompt))
                    )
                )
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

            // Enhanced error handling for empty responses
            val responseBody = response.body()
            if (responseBody == null) {
                Log.e(TAG, "Response body is null")
                throw Exception("Empty response from server. Please try again.")
            }

            if (responseBody.candidates.isNullOrEmpty()) {
                Log.e(TAG, "No candidates in response")
                throw Exception("Could not generate analysis. Please try again with different text.")
            }
            val firstCandidate = responseBody.candidates.firstOrNull()
            if (firstCandidate == null) {
                Log.e(TAG, "First candidate is null")
                throw Exception("Could not generate analysis. Please try again.")
            }

            val content = firstCandidate.content.parts.firstOrNull()?.text
            if (content.isNullOrBlank()) {
                Log.e(TAG, "Response text is null or empty")
                throw Exception("Received empty response from AI. Please try again.")
            }

            Log.d(TAG, "Gemini Response: $content")

            // Parse JSON response
            val analysis = parseAnalysisResponse(content, chatText, uid)

            // Save to Firestore
            firestore.collection("chat_analyses")
                .document(analysis.id)
                .set(analysis)
                .await()

            Log.d(TAG, "Analysis saved successfully")
            Result.success(analysis)
        } catch (e: Exception) {
            Log.e(TAG, "Error analyzing chat: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun parseAnalysisResponse(
        content: String,
        chatText: String,
        uid: String
    ): ChatAnalysis {
        // Simplified parsing - extract values from JSON-like response
        val tone = extractValue(content, "tone") ?: "awkward"
        val confidenceLevel = extractValue(content, "confidenceLevel")?.toIntOrNull() ?: 50
        val feedback = extractValue(content, "feedback") ?: "Keep being yourself!"
        val funnyAdvice = extractValue(content, "funnyAdvice") ?: "Maybe add some emojis? 😉"

        return ChatAnalysis(
            id = System.currentTimeMillis().toString(),
            userId = uid,
            chatText = chatText,
            tone = tone,
            confidenceLevel = confidenceLevel,
            feedback = feedback,
            funnyAdvice = funnyAdvice
        )
    }

    private fun extractValue(json: String, key: String): String? {
        // Try to extract value from JSON format
        val regex = """"$key"\s*:\s*"([^"]*)"""".toRegex()
        val match = regex.find(json)
        if (match != null) {
            return match.groupValues.get(1)
        }

        // Try to extract numeric value
        val numRegex = """"$key"\s*:\s*(\d+)""".toRegex()
        val numMatch = numRegex.find(json)
        return numMatch?.groupValues?.get(1)
    }

    suspend fun getChatAnalysisHistory(limit: Int = 10): List<ChatAnalysis> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        return try {
            val snapshot = firestore.collection("chat_analyses")
                .whereEqualTo("userId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            snapshot.toObjects(ChatAnalysis::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createConfidenceMission(mission: ConfidenceMission): Result<ConfidenceMission> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Not authenticated")
            val missionWithUser = mission.copy(
                userId = uid,
                id = System.currentTimeMillis().toString()
            )

            firestore.collection("confidence_missions")
                .document(missionWithUser.id)
                .set(missionWithUser)
                .await()

            Result.success(missionWithUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getActiveMissions(): List<ConfidenceMission> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        return try {
            val snapshot = firestore.collection("confidence_missions")
                .whereEqualTo("userId", uid)
                .whereEqualTo("isCompleted", false)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.toObjects(ConfidenceMission::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateMissionProgress(
        missionId: String,
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

            firestore.collection("confidence_missions")
                .document(missionId)
                .update(updates)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
