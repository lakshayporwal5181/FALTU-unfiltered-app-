package com.example.faltu.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Gemini API Configuration
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"

    /**
     * Google Gemini API Key
     * Your API Key: AIzaSyBAF1EcjEit1qEz5JAcLkoICJdwApOq3Gk
     *
     * Gemini is free for reasonable usage (up to 60 requests per minute)
     * To manage your API key, visit: https://aistudio.google.com/app/apikey
     *
     * API Documentation: https://ai.google.dev/gemini-api/docs
     */
    const val GEMINI_API_KEY = "AIzaSyBAF1EcjEit1qEz5JAcLkoICJdwApOq3Gk"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(GEMINI_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val geminiService: GeminiService by lazy {
        retrofit.create(GeminiService::class.java)
    }

    // Keep OpenAI service for backward compatibility (not used anymore)
    @Deprecated("Use geminiService instead")
    val openAIService: OpenAIService by lazy {
        retrofit.create(OpenAIService::class.java)
    }
}
