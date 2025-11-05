package com.example.faltu.data.models

data class ScreenTimeData(
    val id: String = "",
    val userId: String = "",
    val date: String = "", // yyyy-MM-dd
    val totalScreenTimeMinutes: Int = 0,
    val productiveTimeMinutes: Int = 0,
    val wastedTimeMinutes: Int = 0,
    val topApps: List<AppUsage> = emptyList(),
    val roastCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

data class AppUsage(
    val packageName: String = "",
    val appName: String = "",
    val usageTimeMinutes: Int = 0,
    val category: String = "" // "productive", "social", "entertainment", "other"
)

data class Roast(
    val id: String = "",
    val userId: String = "",
    val roastText: String = "",
    val severity: String = "", // "light", "medium", "savage"
    val screenTimeMinutes: Int = 0,
    val voiceType: String = "default", // "default", "celebrity1", "celebrity2" (for Boss Mode)
    val timestamp: Long = System.currentTimeMillis()
)

data class ProductivityChallenge(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "", // "walk", "limit_usage", "focus_time"
    val targetValue: Int = 0, // minutes or meters
    val currentProgress: Int = 0,
    val isCompleted: Boolean = false,
    val reward: Int = 0, // productivity points
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long = 0
)
