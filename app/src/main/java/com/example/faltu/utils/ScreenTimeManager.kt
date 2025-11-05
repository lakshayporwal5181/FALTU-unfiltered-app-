package com.example.faltu.utils

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import com.example.faltu.data.models.AppUsage
import com.example.faltu.data.models.ScreenTimeData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ScreenTimeManager(private val context: Context) {

    private val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
    private val packageManager = context.packageManager

    /**
     * Check if usage access permission is granted
     */
    fun hasUsageStatsPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * Get today's screen time data
     */
    fun getTodayScreenTime(): ScreenTimeData {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        return getScreenTimeForPeriod(startTime, endTime)
    }

    /**
     * Get screen time data for a specific period
     */
    fun getScreenTimeForPeriod(startTime: Long, endTime: Long): ScreenTimeData {
        if (usageStatsManager == null || !hasUsageStatsPermission()) {
            return ScreenTimeData()
        }

        val usageStatsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )

        val appUsageList = mutableListOf<AppUsage>()
        var totalScreenTime = 0L
        var productiveTime = 0L
        var wastedTime = 0L

        usageStatsList?.forEach { usageStats ->
            val totalTime = usageStats.totalTimeInForeground
            if (totalTime > 0) {
                val appName = getAppName(usageStats.packageName)
                val category = categorizeApp(usageStats.packageName)

                appUsageList.add(
                    AppUsage(
                        packageName = usageStats.packageName,
                        appName = appName,
                        usageTimeMinutes = (totalTime / 1000 / 60).toInt(),
                        category = category
                    )
                )

                totalScreenTime += totalTime
                when (category) {
                    "productive" -> productiveTime += totalTime
                    else -> wastedTime += totalTime
                }
            }
        }

        // Sort by usage time and take top 10
        val topApps = appUsageList
            .sortedByDescending { it.usageTimeMinutes }
            .take(10)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return ScreenTimeData(
            date = dateFormat.format(Date(startTime)),
            totalScreenTimeMinutes = (totalScreenTime / 1000 / 60).toInt(),
            productiveTimeMinutes = (productiveTime / 1000 / 60).toInt(),
            wastedTimeMinutes = (wastedTime / 1000 / 60).toInt(),
            topApps = topApps
        )
    }

    /**
     * Get app name from package name
     */
    private fun getAppName(packageName: String): String {
        return try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName
        }
    }

    /**
     * Categorize app based on package name
     */
    private fun categorizeApp(packageName: String): String {
        return when {
            packageName.contains("productivity") ||
                    packageName.contains("office") ||
                    packageName.contains("docs") ||
                    packageName.contains("calendar") ||
                    packageName.contains("gmail") ||
                    packageName.contains("email") -> "productive"

            packageName.contains("facebook") ||
                    packageName.contains("instagram") ||
                    packageName.contains("twitter") ||
                    packageName.contains("tiktok") ||
                    packageName.contains("snapchat") ||
                    packageName.contains("whatsapp") ||
                    packageName.contains("telegram") -> "social"

            packageName.contains("youtube") ||
                    packageName.contains("netflix") ||
                    packageName.contains("spotify") ||
                    packageName.contains("game") ||
                    packageName.contains("play") -> "entertainment"

            else -> "other"
        }
    }
}
