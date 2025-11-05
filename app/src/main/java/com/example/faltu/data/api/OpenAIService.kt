package com.example.faltu.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIService {

    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun analyzeChatTone(@Body request: OpenAIRequest): Response<OpenAIResponse>

    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun generateRoast(@Body request: OpenAIRequest): Response<OpenAIResponse>
}

data class OpenAIRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
    val temperature: Float = 0.7f,
    val max_tokens: Int = 500
)

data class Message(
    val role: String, // "system", "user", "assistant"
    val content: String
)

data class OpenAIResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage
)

data class Choice(
    val index: Int,
    val message: Message,
    val finish_reason: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)
