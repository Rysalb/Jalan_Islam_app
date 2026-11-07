package com.jalanislam.app.data.quran

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

object QuranApiClient {

    private val gson = Gson()

    /**
     * Maps app language code to AlQuran Cloud translation edition identifier.
     */
    private fun getEditionForLanguage(langCode: String): String {
        return when (langCode.lowercase()) {
            "en" -> "en.sahih"
            "ms" -> "ms.basmeih"
            "th" -> "th.thai"
            "ja" -> "ja.japanese"
            "hi" -> "hi.hindi"
            "bn" -> "bn.bengali"
            "ur" -> "ur.jalandhry"
            "zh" -> "zh.jian"
            "tr" -> "tr.diyanet"
            "fr" -> "fr.hamidullah"
            "de" -> "de.aburida"
            "ar" -> "quran-uthmani"
            else -> "id.indonesian"
        }
    }

    /**
     * Load verses for a surah. Checks local disk cache first, then curated fallback,
     * then downloads from official verified Quran APIs and caches to disk.
     */
    suspend fun loadVerses(
        context: Context,
        surahNumber: Int,
        langCode: String = "id"
    ): Result<List<AyahItem>> = withContext(Dispatchers.IO) {
        val cacheDir = File(context.filesDir, "quran_cache").apply { mkdirs() }
        val cacheFile = File(cacheDir, "surah_${surahNumber}_${langCode.lowercase()}.json")

        // 1. Try local disk cache
        if (cacheFile.exists() && cacheFile.length() > 0) {
            try {
                val type = object : TypeToken<List<AyahItem>>() {}.type
                val cached: List<AyahItem> = gson.fromJson(cacheFile.readText(), type)
                if (!cached.isNullOrEmpty()) {
                    return@withContext Result.success(cached)
                }
            } catch (e: Exception) {
                cacheFile.delete()
            }
        }

        // 2. Try curated offline verses if available
        val curated = CuratedQuranVerses.getVerses(surahNumber, langCode)
        if (curated.isNotEmpty()) {
            try {
                cacheFile.writeText(gson.toJson(curated))
            } catch (ignored: Exception) {}
            return@withContext Result.success(curated)
        }

        // 3. Fetch from remote API
        val isIndonesianOrMalay = langCode.lowercase() == "id" || langCode.lowercase() == "ms"

        val downloadedList = if (isIndonesianOrMalay) {
            fetchFromEQuran(surahNumber) ?: fetchFromAlQuranCloud(surahNumber, getEditionForLanguage(langCode))
        } else {
            fetchFromAlQuranCloud(surahNumber, getEditionForLanguage(langCode)) ?: fetchFromEQuran(surahNumber)
        }

        if (downloadedList != null && downloadedList.isNotEmpty()) {
            try {
                cacheFile.writeText(gson.toJson(downloadedList))
            } catch (ignored: Exception) {}
            return@withContext Result.success(downloadedList)
        }

        // 4. Return failure if network unreachable and no cache exists
        Result.failure(Exception("Tidak dapat memuat ayat Surah $surahNumber. Silakan periksa koneksi internet Anda."))
    }

    /**
     * Fetch authentic Indonesian Kemenag verses from EQuran.id API.
     */
    private fun fetchFromEQuran(surahNumber: Int): List<AyahItem>? {
        return try {
            val url = URL("https://equran.id/api/v2/surat/$surahNumber")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 8_000
                readTimeout = 12_000
                requestMethod = "GET"
                setRequestProperty("User-Agent", "JalanIslamApp/1.0")
                setRequestProperty("Accept", "application/json")
            }

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val jsonText = conn.inputStream.bufferedReader().use { it.readText() }
                conn.disconnect()

                val root = gson.fromJson(jsonText, EQuranRoot::class.java)
                val surahData = root?.data ?: return null
                val ayahs = surahData.ayat ?: return null

                ayahs.map { item ->
                    val audioUrl = item.audio?.get("05")
                        ?: item.audio?.get("01")
                        ?: "https://everyayah.com/data/Alafasy_128kbps/${String.format("%03d%03d", surahNumber, item.nomorAyat)}.mp3"

                    AyahItem(
                        surahNumber = surahNumber,
                        ayahNumber = item.nomorAyat,
                        textArabic = item.teksArab,
                        textLatin = item.teksLatin?.trim() ?: "Ayat ke-${item.nomorAyat}",
                        textTranslation = item.teksIndonesia?.trim() ?: "",
                        audioUrl = audioUrl,
                        isRevealed = true
                    )
                }
            } else {
                conn.disconnect()
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Fetch verses from AlQuran.cloud API with verified Uthmani script and language edition.
     */
    private fun fetchFromAlQuranCloud(surahNumber: Int, edition: String): List<AyahItem>? {
        return try {
            val url = URL("https://api.alquran.cloud/v1/surah/$surahNumber/editions/quran-uthmani,$edition")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 8_000
                readTimeout = 12_000
                requestMethod = "GET"
                setRequestProperty("User-Agent", "JalanIslamApp/1.0")
                setRequestProperty("Accept", "application/json")
            }

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val jsonText = conn.inputStream.bufferedReader().use { it.readText() }
                conn.disconnect()

                val root = gson.fromJson(jsonText, AlQuranCloudRoot::class.java)
                val data = root?.data ?: return null
                if (data.size < 2) return null

                val arabicEd = data[0].ayahs ?: return null
                val translationEd = data[1].ayahs ?: return null

                arabicEd.mapIndexed { index, arabicAyah ->
                    val transText = if (index < translationEd.size) translationEd[index].text else ""
                    val ayahNum = arabicAyah.numberInSurah
                    val audioUrl = "https://everyayah.com/data/Alafasy_128kbps/${String.format("%03d%03d", surahNumber, ayahNum)}.mp3"

                    AyahItem(
                        surahNumber = surahNumber,
                        ayahNumber = ayahNum,
                        textArabic = arabicAyah.text,
                        textLatin = "Ayat ke-$ayahNum",
                        textTranslation = transText,
                        audioUrl = audioUrl,
                        isRevealed = true
                    )
                }
            } else {
                conn.disconnect()
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // JSON DTOs for EQuran
    private data class EQuranRoot(val code: Int, val message: String?, val data: EQuranSurahData?)
    private data class EQuranSurahData(val nomor: Int, val ayat: List<EQuranAyahItem>?)
    private data class EQuranAyahItem(
        val nomorAyat: Int,
        val teksArab: String,
        val teksLatin: String?,
        val teksIndonesia: String?,
        val audio: Map<String, String>?
    )

    // JSON DTOs for AlQuran Cloud
    private data class AlQuranCloudRoot(val code: Int, val status: String?, val data: List<AlQuranCloudData>?)
    private data class AlQuranCloudData(val number: Int, val ayahs: List<AlQuranCloudAyahItem>?)
    private data class AlQuranCloudAyahItem(val numberInSurah: Int, val text: String)
}

