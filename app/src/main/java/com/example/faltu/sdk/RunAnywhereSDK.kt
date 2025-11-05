package com.example.faltu.sdk

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * RunAnywhere SDK Integration
 * Provides location-based tracking for missions and challenges
 */
class RunAnywhereSDK private constructor(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    companion object {
        @Volatile
        private var instance: RunAnywhereSDK? = null

        fun getInstance(context: Context): RunAnywhereSDK {
            return instance ?: synchronized(this) {
                instance ?: RunAnywhereSDK(context.applicationContext).also { instance = it }
            }
        }
    }

    /**
     * Check if location permission is granted
     */
    fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Get current location
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        if (!hasLocationPermission()) return null

        return try {
            fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Track location updates for missions
     */
    @SuppressLint("MissingPermission")
    fun trackLocationUpdates(): Flow<Location> = callbackFlow {
        if (!hasLocationPermission()) {
            close()
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L // Update every 5 seconds
        ).apply {
            setMinUpdateIntervalMillis(2000L)
        }.build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(location)
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                context.mainLooper
            )
        } catch (e: SecurityException) {
            close()
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    /**
     * Calculate distance between two locations in meters
     */
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    /**
     * Check if user is within mission radius
     */
    suspend fun isWithinMissionRadius(
        targetLat: Double,
        targetLon: Double,
        radiusMeters: Int
    ): Boolean {
        val currentLocation = getCurrentLocation() ?: return false
        val distance = calculateDistance(
            currentLocation.latitude,
            currentLocation.longitude,
            targetLat,
            targetLon
        )
        return distance <= radiusMeters
    }

    /**
     * Track distance walked for challenges
     */
    data class DistanceTracker(
        var totalDistance: Float = 0f,
        var lastLocation: Location? = null
    )

    fun createDistanceTracker(): DistanceTracker = DistanceTracker()

    fun updateDistanceTracker(tracker: DistanceTracker, newLocation: Location): Float {
        tracker.lastLocation?.let { lastLoc ->
            val distance = lastLoc.distanceTo(newLocation)
            tracker.totalDistance += distance
        }
        tracker.lastLocation = newLocation
        return tracker.totalDistance
    }
}
