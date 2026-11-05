package com.jalanislam.app.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jalanislam.app.data.cities.IndonesianCities
import com.jalanislam.app.data.model.PrayerName
import com.jalanislam.app.data.prayer.AstronomicalPrayerCalculator
import com.jalanislam.app.data.preferences.AppPreferences
import java.util.Calendar

object PrayerAlarmScheduler {

    const val EXTRA_PRAYER_NAME = "extra_prayer_name"
    const val EXTRA_LOCATION_NAME = "extra_location_name"
    const val EXTRA_IS_TEST = "extra_is_test"

    fun scheduleNextAlarm(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val prefs = AppPreferences(context)

            // Cancel previous alarms safely to prevent dual/orphan alarms
            cancelAllAlarms(context)

            // Determine active location: GPS if enabled and cached, otherwise manual city
            val (lat, lng, locName) = try {
                if (prefs.useGpsFlow.value) {
                    val gps = prefs.getGpsLocation()
                    if (gps != null) {
                        Triple(gps.first, gps.second, gps.third.ifBlank { "Lokasi GPS" })
                    } else {
                        val cityId = prefs.selectedCityIdFlow.value
                        val city = IndonesianCities.findById(cityId) ?: IndonesianCities.getDefaultCity()
                        Triple(city.latitude, city.longitude, city.name)
                    }
                } else {
                    val cityId = prefs.selectedCityIdFlow.value
                    val city = IndonesianCities.findById(cityId) ?: IndonesianCities.getDefaultCity()
                    Triple(city.latitude, city.longitude, city.name)
                }
            } catch (e: Exception) {
                val fallbackCity = IndonesianCities.getDefaultCity()
                Triple(fallbackCity.latitude, fallbackCity.longitude, fallbackCity.name)
            }

            val adjustments = prefs.loadAdjustments()
            val now = System.currentTimeMillis()
            val calendar = Calendar.getInstance()

            // Calculate today and tomorrow to find next upcoming prayer
            val todaySchedule = AstronomicalPrayerCalculator.calculateDaily(
                calendar, lat, lng, locName, adjustments
            )

            var nextItem = todaySchedule.toList().find { it.timeMillis > now && it.name != PrayerName.SUNRISE }

            if (nextItem == null) {
                // Check tomorrow
                val tomorrowCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
                val tomorrowSchedule = AstronomicalPrayerCalculator.calculateDaily(
                    tomorrowCal, lat, lng, locName, adjustments
                )
                nextItem = tomorrowSchedule.toList().find { it.name != PrayerName.SUNRISE }
            }

            if (nextItem == null) return

            val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                putExtra(EXTRA_PRAYER_NAME, nextItem.name.name)
                putExtra(EXTRA_LOCATION_NAME, locName)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                nextItem.name.ordinal,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val triggerTime = nextItem.timeMillis

            val showActivityIntent = Intent(context, com.jalanislam.app.MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val showPendingIntent = PendingIntent.getActivity(
                context,
                100 + nextItem.name.ordinal,
                showActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    val clockInfo = AlarmManager.AlarmClockInfo(triggerTime, showPendingIntent)
                    alarmManager.setAlarmClock(clockInfo, pendingIntent)
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val clockInfo = AlarmManager.AlarmClockInfo(triggerTime, showPendingIntent)
                alarmManager.setAlarmClock(clockInfo, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } catch (e: SecurityException) {
            android.util.Log.w("PrayerAlarmScheduler", "Exact alarm denied, fallback handled", e)
        } catch (t: Throwable) {
            android.util.Log.e("PrayerAlarmScheduler", "Non-fatal error in scheduleNextAlarm: ${t.localizedMessage}", t)
        }
    }

    /**
     * Trigger a mock/test notification and adzan sound for user testing.
     * @param delaySeconds 0 for immediate playback, or >0 (e.g. 10s) to test lock screen / sleep wake up.
     */
    fun triggerTestNotification(context: Context, delaySeconds: Int = 0) {
        val prefs = AppPreferences(context)
        val cityId = prefs.selectedCityIdFlow.value
        val city = IndonesianCities.findById(cityId) ?: IndonesianCities.getDefaultCity()

        if (delaySeconds <= 0) {
            val serviceIntent = Intent(context, AdzanPlaybackService::class.java).apply {
                action = AdzanPlaybackService.ACTION_PLAY
                putExtra(EXTRA_PRAYER_NAME, PrayerName.DHUHR.name)
                putExtra(EXTRA_LOCATION_NAME, city.name)
                putExtra(EXTRA_IS_TEST, true)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        } else {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val triggerTime = System.currentTimeMillis() + (delaySeconds * 1000L)
            val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                putExtra(EXTRA_PRAYER_NAME, PrayerName.DHUHR.name)
                putExtra(EXTRA_LOCATION_NAME, city.name)
                putExtra(EXTRA_IS_TEST, true)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                9999,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val showActivityIntent = Intent(context, com.jalanislam.app.MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val showPendingIntent = PendingIntent.getActivity(
                context,
                9998,
                showActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        val clockInfo = AlarmManager.AlarmClockInfo(triggerTime, showPendingIntent)
                        alarmManager.setAlarmClock(clockInfo, pendingIntent)
                    } else {
                        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val clockInfo = AlarmManager.AlarmClockInfo(triggerTime, showPendingIntent)
                    alarmManager.setAlarmClock(clockInfo, pendingIntent)
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                }
            } catch (e: Exception) {
                try {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                } catch (e2: Exception) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                }
            }
        }
    }

    /**
     * Stop currently playing test adzan playback.
     */
    fun stopTestNotification(context: Context) {
        AdzanPlaybackState.stopAdzan(context)
    }

    fun cancelAllAlarms(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            PrayerName.entries.forEach { prayer ->
                try {
                    val intent = Intent(context, PrayerAlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        prayer.ordinal,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    if (pendingIntent != null) {
                        alarmManager.cancel(pendingIntent)
                    }
                } catch (e: Exception) {
                    // Ignore individual cancel exception
                }
            }
        } catch (t: Throwable) {
            android.util.Log.w("PrayerAlarmScheduler", "cancelAllAlarms non-fatal: ${t.localizedMessage}")
        }
    }
}

