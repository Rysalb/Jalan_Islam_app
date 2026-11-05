package com.jalanislam.app.data.adzan

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import com.jalanislam.app.data.model.PrayerName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

enum class AdzanSoundStatus {
    BUNDLED,      // Tersedia bawaan aplikasi (offline)
    DOWNLOADED,   // Sudah diunduh di perangkat (offline)
    CUSTOM_LOCAL, // Berkas kustom dari HP pengguna (offline)
    ONLINE_ONLY   // Belum diunduh, butuh unduhan
}

data class AdzanSoundItem(
    val id: String,
    val title: String,
    val origin: String,
    val description: String,
    val rawResName: String? = null,
    val customFilePath: String? = null,
    val downloadUrl: String? = null,
    val fileSizeFormatted: String = "1.2 MB"
) {
    val isCustom: Boolean get() = customFilePath != null

    fun getStatus(context: Context): AdzanSoundStatus {
        if (customFilePath != null && File(customFilePath).exists()) {
            return AdzanSoundStatus.CUSTOM_LOCAL
        }
        if (rawResName != null) {
            val rawId = context.resources.getIdentifier(rawResName, "raw", context.packageName)
            if (rawId != 0) return AdzanSoundStatus.BUNDLED
        }
        val downloadedFile = File(File(context.filesDir, "downloaded_adzan"), "$id.mp3")
        if (downloadedFile.exists() && downloadedFile.length() > 0) {
            return AdzanSoundStatus.DOWNLOADED
        }
        return AdzanSoundStatus.ONLINE_ONLY
    }

    val isAvailableOffline: (Context) -> Boolean = { ctx ->
        getStatus(ctx) != AdzanSoundStatus.ONLINE_ONLY
    }
}

class AdzanSoundManager(private val context: Context) {

    private var previewPlayer: MediaPlayer? = null
    var currentlyPlayingId: String? = null
        private set

    // Playlist Khusus Subuh (Dilengkapi lafadz: Ash-shalātu khairum minan na‘ūm)
    val subuhPlaylist: List<AdzanSoundItem> = listOf(
        AdzanSoundItem(
            id = "adzan_subuh",
            title = "Adzan Subuh Makkah (Masjidil Haram)",
            origin = "Makkah Al-Mukarramah",
            description = "Lafadz lengkap Ash-shalatu khairum minan naum (Tersedia Bawaan)",
            rawResName = "adzan_subuh",
            fileSizeFormatted = "644 KB"
        ),
        AdzanSoundItem(
            id = "adzan_subuh_madinah",
            title = "Adzan Subuh Madinah (Masjid Nabawi)",
            origin = "Madinah Al-Munawwarah",
            description = "Lantunan merdu syahdu muadzin Nabawi Sheikh Surayhi",
            downloadUrl = "https://www.islamcan.com/audio/adhan/azan14.mp3",
            fileSizeFormatted = "1.0 MB"
        ),
        AdzanSoundItem(
            id = "adzan_subuh_cairo",
            title = "Adzan Subuh Kairo (Mesir)",
            origin = "Al-Azhar Asy-Syarif",
            description = "Maqam Bayati vokal klasik legendaris Kairo",
            downloadUrl = "https://www.islamcan.com/audio/adhan/azan16.mp3",
            fileSizeFormatted = "1.1 MB"
        ),
        AdzanSoundItem(
            id = "adzan_subuh_syahdu",
            title = "Adzan Subuh Syahdu Khusyuk",
            origin = "Harmoni Islami",
            description = "Vokal syahdu lembut penggetar hati saat bangun tidur",
            downloadUrl = "https://www.islamcan.com/audio/adhan/azan21.mp3",
            fileSizeFormatted = "489 KB"
        )
    )

    // Playlist Adzan Sholat Harian (Dzuhur, Ashar, Maghrib, Isya)
    val dailyPlaylist: List<AdzanSoundItem> = listOf(
        AdzanSoundItem(
            id = "adzan_islamcan",
            title = "Adzan Rekomendasi (IslamCan)",
            origin = "Vokal Asli Pilihan",
            description = "Lantunan vokal muadzin merdu & syahdu (azan20.mp3)",
            rawResName = "adzan_islamcan",
            fileSizeFormatted = "384 KB"
        ),
        AdzanSoundItem(
            id = "adzan_makkah",
            title = "Adzan Makkah (Masjidil Haram)",
            origin = "Makkah Al-Mukarramah",
            description = "Lantunan vokal muadzin penuh wibawa dan khidmat maqam Hijaz",
            rawResName = "adzan_makkah",
            fileSizeFormatted = "670 KB"
        ),
        AdzanSoundItem(
            id = "adzan_madinah",
            title = "Adzan Madinah (Masjid Nabawi)",
            origin = "Madinah Al-Munawwarah",
            description = "Lantunan vokal merdu dan menenangkan hati dari Kota Nabi",
            rawResName = "adzan_madinah",
            fileSizeFormatted = "558 KB"
        ),
        AdzanSoundItem(
            id = "adzan_alaqsa",
            title = "Adzan Al-Aqsa (Yerusalem)",
            origin = "Al-Quds, Palestina",
            description = "Karakter vokal syahdu khas Masjid Al-Aqsa",
            downloadUrl = "https://www.islamcan.com/audio/adhan/azan3.mp3",
            fileSizeFormatted = "981 KB"
        ),
        AdzanSoundItem(
            id = "adzan_egypt",
            title = "Adzan Mesir (Kairo)",
            origin = "Al-Azhar Asy-Syarif",
            description = "Lantunan vokal klasik legendaris Syaikh Abdul Basit",
            downloadUrl = "https://www.islamcan.com/audio/adhan/azan4.mp3",
            fileSizeFormatted = "1.0 MB"
        ),
        AdzanSoundItem(
            id = "adzan_turkey",
            title = "Adzan Turki (Istanbul)",
            origin = "Masjid Sultan Ahmed",
            description = "Melodi Utsmani Maqam Rast khas kubah Istanbul",
            downloadUrl = "https://www.islamcan.com/audio/adhan/azan6.mp3",
            fileSizeFormatted = "592 KB"
        ),
        AdzanSoundItem(
            id = "adzan_syria",
            title = "Adzan Syam (Damaskus)",
            origin = "Masjid Umayyah",
            description = "Lantunan merdu penuh penghayatan khas Syam",
            downloadUrl = "https://www.islamcan.com/audio/adhan/azan7.mp3",
            fileSizeFormatted = "864 KB"
        )
    )

    // Gabungan seluruh playlist untuk resolusi id playback
    val curatedPlaylist: List<AdzanSoundItem> = (subuhPlaylist + dailyPlaylist).distinctBy { it.id }

    fun getPlaylistForPrayer(prayer: PrayerName): List<AdzanSoundItem> {
        return if (prayer == PrayerName.FAJR) subuhPlaylist else dailyPlaylist
    }

    fun getDownloadDir(): File {
        val dir = File(context.filesDir, "downloaded_adzan")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun getCustomDir(): File {
        val dir = File(context.filesDir, "custom_adzan")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun getFileNameFromUri(uri: Uri): String {
        var name = "Audio Kustom HP"
        try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    val resolved = cursor.getString(nameIndex)
                    if (!resolved.isNullOrBlank()) {
                        name = resolved
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return name
    }

    fun saveCustomAudio(uri: Uri, displayName: String): AdzanSoundItem? {
        return try {
            val dir = getCustomDir()
            val fileName = "custom_${System.currentTimeMillis()}.mp3"
            val destFile = File(dir, fileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }

            AdzanSoundItem(
                id = "custom_${destFile.name}",
                title = displayName.ifBlank { "Audio Kustom HP" },
                origin = "Penyimpanan HP Anda",
                description = "File lokal: ${destFile.name}",
                customFilePath = destFile.absolutePath
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun listCustomAdzans(): List<AdzanSoundItem> {
        val dir = getCustomDir()
        val files = dir.listFiles() ?: return emptyList()
        return files.filter { it.isFile && (it.extension == "mp3" || it.extension == "wav" || it.extension == "ogg" || it.extension == "m4a") }
            .map { file ->
                AdzanSoundItem(
                    id = "custom_${file.name}",
                    title = file.nameWithoutExtension.replace("custom_", "Audio "),
                    origin = "Penyimpanan HP Anda",
                    description = "File audio lokal dari memori perangkat",
                    customFilePath = file.absolutePath
                )
            }
    }

    suspend fun downloadAdzan(
        item: AdzanSoundItem,
        onProgress: (Int) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        val urlStr = item.downloadUrl ?: return@withContext Result.failure(Exception("URL unduhan tidak tersedia"))
        val targetFile = File(getDownloadDir(), "${item.id}.mp3")

        try {
            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 15000
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")
            connection.connect()

            if (connection.responseCode !in 200..299) {
                return@withContext Result.failure(Exception("Server merespons kode: ${connection.responseCode}"))
            }

            val fileLength = connection.contentLength
            val input = connection.inputStream
            val output = FileOutputStream(targetFile)

            val data = ByteArray(4096)
            var total: Long = 0
            var count: Int

            while (input.read(data).also { count = it } != -1) {
                total += count
                if (fileLength > 0) {
                    val progress = ((total * 100) / fileLength).toInt()
                    withContext(Dispatchers.Main) {
                        onProgress(progress)
                    }
                }
                output.write(data, 0, count)
            }

            output.flush()
            output.close()
            input.close()

            Result.success(targetFile)
        } catch (e: Exception) {
            targetFile.delete()
            Result.failure(e)
        }
    }

    fun deleteDownloadedAdzan(item: AdzanSoundItem): Boolean {
        val file = File(getDownloadDir(), "${item.id}.mp3")
        return if (file.exists()) file.delete() else false
    }

    fun deleteCustomAudio(item: AdzanSoundItem): Boolean {
        val path = item.customFilePath ?: return false
        val file = File(path)
        return if (file.exists()) file.delete() else false
    }

    /**
     * Resolves playable local URI for an item.
     */
    fun resolvePlayableUri(item: AdzanSoundItem): Uri? {
        if (item.customFilePath != null && File(item.customFilePath).exists()) {
            return Uri.fromFile(File(item.customFilePath))
        }
        if (item.rawResName != null) {
            val rawId = context.resources.getIdentifier(item.rawResName, "raw", context.packageName)
            if (rawId != 0) {
                return Uri.parse("android.resource://${context.packageName}/$rawId")
            }
        }
        val downloaded = File(getDownloadDir(), "${item.id}.mp3")
        if (downloaded.exists()) {
            return Uri.fromFile(downloaded)
        }
        return null
    }

    /**
     * Plays preview of any sound item (bundled, downloaded, or custom).
     */
    fun playPreview(soundItem: AdzanSoundItem, onCompletion: () -> Unit, onError: (String) -> Unit) {
        stopPreview()

        val uri = resolvePlayableUri(soundItem)
        val onlineUrl = if (uri == null) soundItem.downloadUrl else null

        if (uri == null && onlineUrl == null) {
            onError("Audio belum diunduh ke perangkat. Silakan unduh terlebih dahulu.")
            return
        }

        try {
            previewPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                if (uri != null) {
                    setDataSource(context, uri)
                } else if (onlineUrl != null) {
                    setDataSource(onlineUrl)
                }

                setOnPreparedListener {
                    currentlyPlayingId = soundItem.id
                    it.start()
                }
                setOnCompletionListener {
                    currentlyPlayingId = null
                    onCompletion()
                }
                setOnErrorListener { _, what, extra ->
                    currentlyPlayingId = null
                    onError("Gagal memutar audio ($what, $extra)")
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            currentlyPlayingId = null
            onError("Error audio: ${e.localizedMessage}")
        }
    }

    fun stopPreview() {
        previewPlayer?.let {
            try {
                if (it.isPlaying) it.stop()
                it.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        previewPlayer = null
        currentlyPlayingId = null
    }
}
