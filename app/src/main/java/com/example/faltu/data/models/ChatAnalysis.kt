package com.example.faltu.data.models

data class ChatAnalysis(
    val id: String = "",
    val userId: String = "",
    val chatText: String = "",
    val tone: String = "", // "flirty", "awkward", "confident", "boring", "cringe"
    val confidenceLevel: Int = 0, // 0-100
    val feedback: String = "",
    val funnyAdvice: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class ConfidenceMission(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "", // "talk", "location", "social"
    val targetCount: Int = 0,
    val currentProgress: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val radiusMeters: Int = 0,
    val isCompleted: Boolean = false,
    val reward: Int = 0, // confidence points
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long = 0
)
