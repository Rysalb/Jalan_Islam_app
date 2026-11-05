package com.jalanislam.app.alarm

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.jalanislam.app.JalanIslamApplication
import com.jalanislam.app.MainActivity
import com.jalanislam.app.R
import com.jalanislam.app.data.model.PrayerName

class AdzanPlaybackService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        if (action == ACTION_STOP) {
            stopPlaybackAndService()
            return START_NOT_STICKY
        }

        val prayerNameStr = intent?.getStringExtra(PrayerAlarmScheduler.EXTRA_PRAYER_NAME)
        val locationName = intent?.getStringExtra(PrayerAlarmScheduler.EXTRA_LOCATION_NAME) ?: "Indonesia"
        val prayerName = PrayerName.fromString(prayerNameStr)

        acquireWakeLock()
        startForegroundNotification(prayerName, locationName)
        startAudioPlayback(prayerName)

        return START_NOT_STICKY
    }

    private fun startForegroundNotification(prayerName: PrayerName, locationName: String) {
        val prayerTitle = getString(prayerName.titleRes)
        val title = getString(R.string.it_is_prayer_time, prayerTitle)
        val body = getString(R.string.prayer_time_body, prayerTitle, locationName)

        // Intent to open app
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent to stop adzan
        val stopIntent = Intent(this, AdzanPlaybackService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, JalanIslamApplication.CHANNEL_ADZAN_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(openPendingIntent)
            .addAction(android.R.drawable.ic_media_pause, getString(R.string.stop_adzan), stopPendingIntent)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun startAudioPlayback(prayerName: PrayerName) {
        stopMediaPlayer()

        try {
            val prefs = com.jalanislam.app.data.preferences.AppPreferences(this)
            val soundId = prefs.getSelectedAdzanSound(prayerName)
            val soundManager = com.jalanislam.app.data.adzan.AdzanSoundManager(this)

            // 1. Search in custom files
            val allSounds = soundManager.curatedPlaylist + soundManager.listCustomAdzans()
            val chosenSound = allSounds.find { it.id == soundId }

            var audioUri: android.net.Uri? = chosenSound?.let { soundManager.resolvePlayableUri(it) }

            // 2. Fallback to bundled sound if chosen is not found or not downloaded
            if (audioUri == null) {
                val fallbackItem = if (prayerName == PrayerName.FAJR) {
                    soundManager.curatedPlaylist.find { it.id == "adzan_subuh" }
                } else {
                    soundManager.curatedPlaylist.find { it.id == "adzan_islamcan" }
                        ?: soundManager.curatedPlaylist.find { it.id == "adzan_makkah" }
                } ?: soundManager.curatedPlaylist.first()

                audioUri = soundManager.resolvePlayableUri(fallbackItem)
            }

            // 3. Final safety fallback to system notification/alarm
            val finalUri = audioUri ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            if (finalUri != null) {
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build()
                    )
                    if (finalUri.scheme == "android.resource") {
                        val rawId = finalUri.lastPathSegment?.toIntOrNull()
                        if (rawId != null && rawId != 0) {
                            val afd = resources.openRawResourceFd(rawId)
                            if (afd != null) {
                                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                                afd.close()
                            } else {
                                setDataSource(applicationContext, finalUri)
                            }
                        } else {
                            setDataSource(applicationContext, finalUri)
                        }
                    } else {
                        setDataSource(applicationContext, finalUri)
                    }
                    isLooping = false
                    setOnCompletionListener { stopPlaybackAndService() }
                    setOnErrorListener { _, _, _ ->
                        stopPlaybackAndService()
                        true
                    }
                    prepare()
                    start()
                }
            } else {
                stopPlaybackAndService()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopPlaybackAndService()
        }
    }

    private fun acquireWakeLock() {
        if (wakeLock == null) {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "JalanIslam:AdzanPlaybackWakeLock"
            ).apply {
                acquire(3 * 60 * 1000L) // 3 minutes timeout safety
            }
        }
    }

    private fun releaseWakeLock() {
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
        wakeLock = null
    }

    private fun stopMediaPlayer() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mediaPlayer = null
    }

    private fun stopPlaybackAndService() {
        stopMediaPlayer()
        releaseWakeLock()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        stopPlaybackAndService()
        super.onDestroy()
    }

    companion object {
        const val ACTION_PLAY = "com.jalanislam.app.ACTION_PLAY_ADZAN"
        const val ACTION_STOP = "com.jalanislam.app.ACTION_STOP_ADZAN"
        private const val NOTIFICATION_ID = 1001
    }
}

