package com.example.faltu.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiService {

    @Headers("Content-Type: application/json")
    @POST("v1/models/gemini-2.5-flash:generateContent")


    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}

data class GeminiRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig = GenerationConfig()
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

data class GenerationConfig(
    val temperature: Float = 0.7f,
    val topK: Int = 40,
    val topP: Float = 0.95f,
    val maxOutputTokens: Int = 1024
)

data class GeminiResponse(
    val candidates: List<Candidate>?,
    val promptFeedback: PromptFeedback? = null
)

data class Candidate(
    val content: Content,
    val finishReason: String?,
    val safetyRatings: List<SafetyRating>? = null
)

data class PromptFeedback(
    val safetyRatings: List<SafetyRating>? = null
)

data class SafetyRating(
    val category: String,
    val probability: String
)
