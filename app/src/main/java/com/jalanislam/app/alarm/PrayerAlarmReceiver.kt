package com.jalanislam.app.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jalanislam.app.data.model.PrayerName
import com.jalanislam.app.data.preferences.AppPreferences
import com.jalanislam.app.data.preferences.NotificationMode

class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prayerNameStr = intent.getStringExtra(PrayerAlarmScheduler.EXTRA_PRAYER_NAME)
        val locationName = intent.getStringExtra(PrayerAlarmScheduler.EXTRA_LOCATION_NAME) ?: "Indonesia"
        val prayerName = PrayerName.fromString(prayerNameStr)

        val prefs = AppPreferences(context)
        val mode = prefs.getNotificationMode(prayerName)

        if (mode != NotificationMode.MUTE) {
            val serviceIntent = Intent(context, AdzanPlaybackService::class.java).apply {
                action = AdzanPlaybackService.ACTION_PLAY
                putExtra(PrayerAlarmScheduler.EXTRA_PRAYER_NAME, prayerName.name)
                putExtra(PrayerAlarmScheduler.EXTRA_LOCATION_NAME, locationName)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }

        // Reschedule for next upcoming prayer time
        PrayerAlarmScheduler.scheduleNextAlarm(context)
    }
}

