package com.jalanislam.app.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import com.jalanislam.app.data.model.PrayerName
import com.jalanislam.app.data.preferences.AppPreferences
import com.jalanislam.app.data.preferences.NotificationMode

class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prayerNameStr = intent.getStringExtra(PrayerAlarmScheduler.EXTRA_PRAYER_NAME)
        val locationName = intent.getStringExtra(PrayerAlarmScheduler.EXTRA_LOCATION_NAME) ?: "Indonesia"
        val isTest = intent.getBooleanExtra(PrayerAlarmScheduler.EXTRA_IS_TEST, false)
        val prayerName = PrayerName.fromString(prayerNameStr)

        val prefs = AppPreferences(context)
        val mode = prefs.getNotificationMode(prayerName)

        // Acquire temporary WakeLock to keep CPU awake while ForegroundService boots up
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val wakeLock = powerManager?.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "JalanIslam:PrayerAlarmReceiverWakeLock"
        )
        try {
            wakeLock?.acquire(15_000L) // 15 seconds safety timeout
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (isTest || mode != NotificationMode.MUTE) {
            val serviceIntent = Intent(context, AdzanPlaybackService::class.java).apply {
                action = AdzanPlaybackService.ACTION_PLAY
                putExtra(PrayerAlarmScheduler.EXTRA_PRAYER_NAME, prayerName.name)
                putExtra(PrayerAlarmScheduler.EXTRA_LOCATION_NAME, locationName)
                putExtra(PrayerAlarmScheduler.EXTRA_IS_TEST, isTest)
            }

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (!isTest) {
            // Reschedule for next upcoming prayer time
            PrayerAlarmScheduler.scheduleNextAlarm(context)
        }
    }
}

