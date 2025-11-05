package com.example.faltu.data.models

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val isPro: Boolean = false,
    val isBossMode: Boolean = false,
    val subscriptionType: String = "", // "none", "dategpt_pro", "lazylegend_boss", "both"
    val createdAt: Long = System.currentTimeMillis(),
    val totalMissionsCompleted: Int = 0,
    val confidenceScore: Int = 0,
    val productivityScore: Int = 0
)
