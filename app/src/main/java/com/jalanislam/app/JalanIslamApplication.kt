package com.jalanislam.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.os.Build

class JalanIslamApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Channel for Adzan playback
            val adzanChannel = NotificationChannel(
                CHANNEL_ADZAN_ID,
                "Alarm Adzan & Waktu Sholat",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi dan suara adzan ketika waktu sholat tiba"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                setSound(null, null) // Audio is actively managed by ForegroundService
            }

            // Channel for generic updates / reminders
            val infoChannel = NotificationChannel(
                CHANNEL_INFO_ID,
                "Informasi & Imsak",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Pengingat waktu Imsak dan persiapan sholat"
            }

            notificationManager.createNotificationChannel(adzanChannel)
            notificationManager.createNotificationChannel(infoChannel)
        }
    }

    companion object {
        const val CHANNEL_ADZAN_ID = "jalan_islam_adzan_channel"
        const val CHANNEL_INFO_ID = "jalan_islam_info_channel"
    }
}

