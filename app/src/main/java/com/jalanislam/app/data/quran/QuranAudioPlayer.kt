package com.jalanislam.app.data.quran

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuranAudioPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentAyahNumber = MutableStateFlow<Int?>(null)
    val currentAyahNumber: StateFlow<Int?> = _currentAyahNumber.asStateFlow()

    private var repeatCountTarget = 1
    private var currentRepeatIteration = 0
    private var currentAyahItem: AyahItem? = null
    private var onAyahCompletedListener: (() -> Unit)? = null

    fun playAyah(
        ayah: AyahItem,
        repeatTimes: Int = 1,
        onComplete: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        stop()

        currentAyahItem = ayah
        repeatCountTarget = repeatTimes
        currentRepeatIteration = 0
        onAyahCompletedListener = onComplete

        startPlaybackInternal(ayah.audioUrl, onError)
    }

    private fun startPlaybackInternal(url: String, onError: ((String) -> Unit)?) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener {
                    _isPlaying.value = true
                    _currentAyahNumber.value = currentAyahItem?.ayahNumber
                    it.start()
                }
                setOnCompletionListener {
                    currentRepeatIteration++
                    if (currentRepeatIteration < repeatCountTarget) {
                        // Loop again for memorization repetition
                        it.seekTo(0)
                        it.start()
                    } else {
                        _isPlaying.value = false
                        _currentAyahNumber.value = null
                        onAyahCompletedListener?.invoke()
                    }
                }
                setOnErrorListener { _, what, extra ->
                    _isPlaying.value = false
                    _currentAyahNumber.value = null
                    onError?.invoke("Error streaming audio ($what, $extra)")
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            _isPlaying.value = false
            _currentAyahNumber.value = null
            onError?.invoke("Tidak dapat memutar audio: ${e.localizedMessage}")
        }
    }

    fun stop() {
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
        _isPlaying.value = false
        _currentAyahNumber.value = null
    }
}

