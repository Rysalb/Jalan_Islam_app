package com.jalanislam.app.alarm

import android.content.Context
import android.content.Intent
import com.jalanislam.app.data.model.PrayerName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Global reactive singleton holding the live playback state of Adzan audio.
 * Allows in-app Compose UI (such as AdzanActiveRingingDialog) to immediately react,
 * display active prayer information, and provide a 1-click Stop button.
 */
object AdzanPlaybackState {
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _activePrayer = MutableStateFlow<PrayerName?>(null)
    val activePrayer = _activePrayer.asStateFlow()

    private val _activeLocation = MutableStateFlow("")
    val activeLocation = _activeLocation.asStateFlow()

    private val _activeSoundTitle = MutableStateFlow("")
    val activeSoundTitle = _activeSoundTitle.asStateFlow()

    private val _isTest = MutableStateFlow(false)
    val isTest = _isTest.asStateFlow()

    fun updatePlaybackStarted(
        prayer: PrayerName,
        location: String,
        soundTitle: String,
        isTest: Boolean = false
    ) {
        _activePrayer.value = prayer
        _activeLocation.value = location
        _activeSoundTitle.value = soundTitle
        _isTest.value = isTest
        _isPlaying.value = true
    }

    fun updatePlaybackStopped() {
        _isPlaying.value = false
    }

    /**
     * Stop currently active adzan sound and dismiss ongoing foreground notification.
     */
    fun stopAdzan(context: Context) {
        try {
            val stopIntent = Intent(context, AdzanPlaybackService::class.java).apply {
                action = AdzanPlaybackService.ACTION_STOP
            }
            context.startService(stopIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        updatePlaybackStopped()
    }
}

