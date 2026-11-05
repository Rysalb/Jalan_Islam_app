package com.jalanislam.app.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.jalanislam.app.data.cities.IndonesianCities
import java.util.Locale
import kotlin.math.*

data class DetectedLocation(
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val isGps: Boolean = true
)

class AppLocationManager(private val context: Context) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

    fun isGpsProviderEnabled(): Boolean {
        val lm = locationManager ?: return false
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    fun scanCurrentLocation(
        onSuccess: (DetectedLocation) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onError("Izin lokasi belum diberikan. Aktifkan izin lokasi untuk memindai otomatis.")
            return
        }

        val lm = locationManager
        if (lm == null || !isGpsProviderEnabled()) {
            onError("Layanan lokasi / GPS nonaktif di perangkat. Silakan aktifkan GPS.")
            return
        }

        // 1. Try to get best last known location first for immediate response
        val lastGps = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val lastNet = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val bestLast = when {
            lastGps != null && lastNet != null -> if (lastGps.time >= lastNet.time) lastGps else lastNet
            lastGps != null -> lastGps
            else -> lastNet
        }

        if (bestLast != null && (System.currentTimeMillis() - bestLast.time) < 15 * 60 * 1000L) {
            val detected = resolveLocationName(bestLast.latitude, bestLast.longitude)
            onSuccess(detected)
            return
        }

        // 2. Request single fresh location update with timeout
        val handler = Handler(Looper.getMainLooper())
        var isReceived = false

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (isReceived) return
                isReceived = true
                try {
                    lm.removeUpdates(this)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val detected = resolveLocationName(location.latitude, location.longitude)
                onSuccess(detected)
            }

            override fun onProviderDisabled(provider: String) {}
            override fun onProviderEnabled(provider: String) {}
            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        val timeoutRunnable = Runnable {
            if (!isReceived) {
                isReceived = true
                try {
                    lm.removeUpdates(listener)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // Fallback to bestLast if available
                if (bestLast != null) {
                    val detected = resolveLocationName(bestLast.latitude, bestLast.longitude)
                    onSuccess(detected)
                } else {
                    onError("Waktu pemindaian GPS habis. Menggunakan kota terdaftar terdekat.")
                }
            }
        }

        handler.postDelayed(timeoutRunnable, 10000L) // 10 seconds timeout

        try {
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 10f, listener, Looper.getMainLooper())
            }
            if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 10f, listener, Looper.getMainLooper())
            }
        } catch (e: Exception) {
            handler.removeCallbacks(timeoutRunnable)
            if (bestLast != null) {
                val detected = resolveLocationName(bestLast.latitude, bestLast.longitude)
                onSuccess(detected)
            } else {
                onError("Gagal memulai pemindaian lokasi: ${e.localizedMessage}")
            }
        }
    }

    fun resolveLocationName(latitude: Double, longitude: Double): DetectedLocation {
        // Attempt native Geocoder first
        try {
            if (Geocoder.isPresent()) {
                val geocoder = Geocoder(context, Locale("id", "ID"))
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val addr = addresses[0]
                    val city = addr.locality ?: addr.subAdminArea ?: addr.adminArea
                    if (!city.isNullOrBlank()) {
                        val cleanedCity = city.replace("Kota", "").replace("Kabupaten", "").trim()
                        return DetectedLocation(
                            latitude = latitude,
                            longitude = longitude,
                            cityName = "$cleanedCity (GPS)",
                            isGps = true
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Offline Fallback: Find nearest Indonesian city using Haversine distance
        val nearest = IndonesianCities.CITIES.minByOrNull { city ->
            haversineKm(latitude, longitude, city.latitude, city.longitude)
        }

        val name = if (nearest != null) {
            "${nearest.name} (GPS)"
        } else {
            "Lokasi Terdeteksi (GPS)"
        }

        return DetectedLocation(
            latitude = latitude,
            longitude = longitude,
            cityName = name,
            isGps = true
        )
    }

    private fun haversineKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}

