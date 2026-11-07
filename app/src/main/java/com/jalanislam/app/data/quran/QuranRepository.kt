package com.jalanislam.app.data.quran

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuranRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("jalan_islam_quran_prefs", Context.MODE_PRIVATE)

    private val _progressFlow = MutableStateFlow(loadProgress())
    val progressFlow: StateFlow<QuranReadingProgress> = _progressFlow.asStateFlow()

    fun loadProgress(): QuranReadingProgress {
        val surahNum = prefs.getInt("last_surah_num", 1)
        val surahName = prefs.getString("last_surah_name", "Al-Fatihah") ?: "Al-Fatihah"
        val ayahNum = prefs.getInt("last_ayah_num", 1)
        val timestamp = prefs.getLong("last_timestamp", System.currentTimeMillis())
        val targetDays = prefs.getInt("khatam_target_days", 30)
        val memorized = prefs.getStringSet("memorized_ayahs", emptySet()) ?: emptySet()
        val readAyahs = prefs.getStringSet("read_ayahs", emptySet()) ?: emptySet()
        val todayCount = prefs.getInt("today_read_count", 0)
        val todayDate = prefs.getString("today_date_str", "") ?: ""

        return QuranReadingProgress(
            lastReadSurahNumber = surahNum,
            lastReadSurahName = surahName,
            lastReadAyahNumber = ayahNum,
            lastReadTimestamp = timestamp,
            targetKhatamDays = targetDays,
            memorizedAyahKeys = memorized,
            readAyahKeys = readAyahs,
            todayReadCount = todayCount,
            todayDateString = todayDate
        )
    }

    fun saveLastRead(surahNumber: Int, surahName: String, ayahNumber: Int) {
        val now = System.currentTimeMillis()
        prefs.edit()
            .putInt("last_surah_num", surahNumber)
            .putString("last_surah_name", surahName)
            .putInt("last_ayah_num", ayahNumber)
            .putLong("last_timestamp", now)
            .apply()

        _progressFlow.value = _progressFlow.value.copy(
            lastReadSurahNumber = surahNumber,
            lastReadSurahName = surahName,
            lastReadAyahNumber = ayahNumber,
            lastReadTimestamp = now
        )
    }

    /**
     * Mark verses 1..ayahNumber in this surah as read (or toggle current ayah),
     * automatically updating reading progress and last read bookmark.
     */
    fun markAyahsReadUpTo(surahNumber: Int, surahName: String, ayahNumber: Int): Boolean {
        val currentRead = _progressFlow.value.readAyahKeys.toMutableSet()
        val targetKey = "$surahNumber:$ayahNumber"
        val isCurrentlyRead = currentRead.contains(targetKey)

        val isNowRead = if (isCurrentlyRead) {
            currentRead.remove(targetKey)
            false
        } else {
            for (i in 1..ayahNumber) {
                currentRead.add("$surahNumber:$i")
            }
            true
        }

        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val storedDate = prefs.getString("today_date_str", "")
        val currentTodayCount = if (storedDate == todayStr) prefs.getInt("today_read_count", 0) else 0
        val updatedTodayCount = if (isNowRead) currentTodayCount + 1 else (currentTodayCount - 1).coerceAtLeast(0)

        val now = System.currentTimeMillis()
        prefs.edit()
            .putStringSet("read_ayahs", currentRead)
            .putInt("last_surah_num", surahNumber)
            .putString("last_surah_name", surahName)
            .putInt("last_ayah_num", ayahNumber)
            .putLong("last_timestamp", now)
            .putString("today_date_str", todayStr)
            .putInt("today_read_count", updatedTodayCount)
            .apply()

        _progressFlow.value = _progressFlow.value.copy(
            readAyahKeys = currentRead,
            lastReadSurahNumber = surahNumber,
            lastReadSurahName = surahName,
            lastReadAyahNumber = ayahNumber,
            lastReadTimestamp = now,
            todayReadCount = updatedTodayCount,
            todayDateString = todayStr
        )

        return isNowRead
    }

    fun toggleMemorized(surahNumber: Int, ayahNumber: Int): Boolean {
        val key = "$surahNumber:$ayahNumber"
        val currentKeys = _progressFlow.value.memorizedAyahKeys.toMutableSet()
        val isNowMemorized = if (currentKeys.contains(key)) {
            currentKeys.remove(key)
            false
        } else {
            currentKeys.add(key)
            true
        }

        prefs.edit().putStringSet("memorized_ayahs", currentKeys).apply()
        _progressFlow.value = _progressFlow.value.copy(memorizedAyahKeys = currentKeys)
        return isNowMemorized
    }

    fun setKhatamTargetDays(days: Int) {
        val clamped = days.coerceIn(1, 365)
        prefs.edit().putInt("khatam_target_days", clamped).apply()
        _progressFlow.value = _progressFlow.value.copy(targetKhatamDays = clamped)
    }

    fun getSurahReadCount(surahNumber: Int): Int {
        return _progressFlow.value.readAyahKeys.count { it.startsWith("$surahNumber:") }
    }

    fun getSurahReadPercentage(surahNumber: Int): Float {
        val surah = getSurah(surahNumber) ?: return 0f
        val count = getSurahReadCount(surahNumber)
        return (count.toFloat() / surah.totalVerses.toFloat() * 100f).coerceIn(0f, 100f)
    }

    fun getSurahMemorizedCount(surahNumber: Int): Int {
        return _progressFlow.value.memorizedAyahKeys.count { it.startsWith("$surahNumber:") }
    }

    // Complete catalog of 114 Surahs
    val surahs: List<SurahInfo> = listOf(
        SurahInfo(1, "الفاتحة", "Al-Fatihah", "Pembukaan", 7, "Makkiyyah", 1),
        SurahInfo(2, "البقرة", "Al-Baqarah", "Sapi Betina", 286, "Madaniyyah", 1),
        SurahInfo(3, "آل عمران", "Ali 'Imran", "Keluarga Imran", 200, "Madaniyyah", 3),
        SurahInfo(4, "النساء", "An-Nisa'", "Wanita", 176, "Madaniyyah", 4),
        SurahInfo(5, "المائدة", "Al-Ma'idah", "Jamuan (Hidangan)", 120, "Madaniyyah", 6),
        SurahInfo(6, "الأنعام", "Al-An'am", "Binatang Ternak", 165, "Makkiyyah", 7),
        SurahInfo(7, "الأعراف", "Al-A'raf", "Tempat-Tempat Tertinggi", 206, "Makkiyyah", 8),
        SurahInfo(8, "الأنفال", "Al-Anfal", "Rampasan Perang", 75, "Madaniyyah", 9),
        SurahInfo(9, "التوبة", "At-Taubah", "Pengampunan", 129, "Madaniyyah", 10),
        SurahInfo(10, "يونس", "Yunus", "Nabi Yunus", 109, "Makkiyyah", 11),
        SurahInfo(11, "هود", "Hud", "Nabi Hud", 123, "Makkiyyah", 11),
        SurahInfo(12, "يوسف", "Yusuf", "Nabi Yusuf", 111, "Makkiyyah", 12),
        SurahInfo(13, "الرعد", "Ar-Ra'd", "Guruh", 43, "Madaniyyah", 13),
        SurahInfo(14, "إبراهيم", "Ibrahim", "Nabi Ibrahim", 52, "Makkiyyah", 13),
        SurahInfo(15, "الحجر", "Al-Hijr", "Daerah Berbatu", 99, "Makkiyyah", 14),
        SurahInfo(16, "النحل", "An-Nahl", "Lebah", 128, "Makkiyyah", 14),
        SurahInfo(17, "الإسراء", "Al-Isra'", "Perjalanan Malam", 111, "Makkiyyah", 15),
        SurahInfo(18, "الكهف", "Al-Kahf", "Gua", 110, "Makkiyyah", 15),
        SurahInfo(19, "مريم", "Maryam", "Siti Maryam", 98, "Makkiyyah", 16),
        SurahInfo(20, "طه", "Taha", "Tha-Ha", 135, "Makkiyyah", 16),
        SurahInfo(21, "الأنبياء", "Al-Anbiya'", "Para Nabi", 112, "Makkiyyah", 17),
        SurahInfo(22, "الحج", "Al-Hajj", "Ibadah Haji", 78, "Madaniyyah", 17),
        SurahInfo(23, "المؤمنون", "Al-Mu'minun", "Orang-Orang Mukmin", 118, "Makkiyyah", 18),
        SurahInfo(24, "النور", "An-Nur", "Cahaya", 64, "Madaniyyah", 18),
        SurahInfo(25, "الفرقان", "Al-Furqan", "Pembeda", 77, "Makkiyyah", 18),
        SurahInfo(26, "الشعراء", "Asy-Syu'ara'", "Para Penyair", 227, "Makkiyyah", 19),
        SurahInfo(27, "النمل", "An-Naml", "Semut", 93, "Makkiyyah", 19),
        SurahInfo(28, "القصص", "Al-Qasas", "Kisah-Kisah", 88, "Makkiyyah", 20),
        SurahInfo(29, "العنكبوت", "Al-'Ankabut", "Laba-Laba", 69, "Makkiyyah", 20),
        SurahInfo(30, "الروم", "Ar-Rum", "Bangsa Romawi", 60, "Makkiyyah", 21),
        SurahInfo(31, "لقمان", "Luqman", "Keluarga Luqman", 34, "Makkiyyah", 21),
        SurahInfo(32, "السجدة", "As-Sajdah", "Sujud", 30, "Makkiyyah", 21),
        SurahInfo(33, "الأحزاب", "Al-Ahzab", "Golongan Yang Bersekutu", 73, "Madaniyyah", 21),
        SurahInfo(34, "سبإ", "Saba'", "Kaum Saba'", 54, "Makkiyyah", 22),
        SurahInfo(35, "فاطر", "Fatir", "Pencipta", 45, "Makkiyyah", 22),
        SurahInfo(36, "يس", "Yasin", "Yaasiin", 83, "Makkiyyah", 22),
        SurahInfo(37, "الصافات", "As-Saffat", "Barisan-Barisan", 182, "Makkiyyah", 23),
        SurahInfo(38, "ص", "Sad", "Shaad", 88, "Makkiyyah", 23),
        SurahInfo(39, "الزمر", "Az-Zumar", "Rombongan-Rombongan", 75, "Makkiyyah", 23),
        SurahInfo(40, "غافر", "Gafir", "Yang Mengampuni", 85, "Makkiyyah", 24),
        SurahInfo(41, "فصلت", "Fussilat", "Yang Dijelaskan", 54, "Makkiyyah", 24),
        SurahInfo(42, "الشورى", "Asy-Syura", "Musyawarah", 53, "Makkiyyah", 25),
        SurahInfo(43, "الزخرف", "Az-Zukhruf", "Perhiasan", 89, "Makkiyyah", 25),
        SurahInfo(44, "الدخان", "Ad-Dukhan", "Kabut", 59, "Makkiyyah", 25),
        SurahInfo(45, "الجاثية", "Al-Jasiyah", "Yang Berlutut", 37, "Makkiyyah", 25),
        SurahInfo(46, "الأحقاف", "Al-Ahqaf", "Bukit-Bukit Pasir", 35, "Makkiyyah", 26),
        SurahInfo(47, "محمد", "Muhammad", "Nabi Muhammad", 38, "Madaniyyah", 26),
        SurahInfo(48, "الفتح", "Al-Fath", "Kemenangan", 29, "Madaniyyah", 26),
        SurahInfo(49, "الحجرات", "Al-Hujurat", "Kamar-Kamar", 18, "Madaniyyah", 26),
        SurahInfo(50, "ق", "Qaf", "Qaaf", 45, "Makkiyyah", 26),
        SurahInfo(51, "الذاريات", "Az-Zariyat", "Angin Yang Menerbangkan", 60, "Makkiyyah", 26),
        SurahInfo(52, "الطور", "At-Tur", "Bukit Tur", 49, "Makkiyyah", 27),
        SurahInfo(53, "النجم", "An-Najm", "Bintang", 62, "Makkiyyah", 27),
        SurahInfo(54, "القمر", "Al-Qamar", "Bulan", 55, "Makkiyyah", 27),
        SurahInfo(55, "الرحمن", "Ar-Rahman", "Yang Maha Pemurah", 78, "Madaniyyah", 27),
        SurahInfo(56, "الواقعة", "Al-Waqi'ah", "Hari Kiamat", 96, "Makkiyyah", 27),
        SurahInfo(57, "الحديد", "Al-Hadid", "Besi", 29, "Madaniyyah", 27),
        SurahInfo(58, "المجادلة", "Al-Mujadilah", "Wanita Yang Mengajukan Gugatan", 22, "Madaniyyah", 28),
        SurahInfo(59, "الحشر", "Al-Hasyr", "Pengusiran", 24, "Madaniyyah", 28),
        SurahInfo(60, "الممتحنة", "Al-Mumtahanah", "Wanita Yang Diuji", 13, "Madaniyyah", 28),
        SurahInfo(61, "الصف", "As-Saff", "Satu Barisan", 14, "Madaniyyah", 28),
        SurahInfo(62, "الجمعة", "Al-Jumu'ah", "Hari Jum'at", 11, "Madaniyyah", 28),
        SurahInfo(63, "المنافقون", "Al-Munafiqun", "Orang-Orang Munafik", 11, "Madaniyyah", 28),
        SurahInfo(64, "التغابن", "At-Tagabun", "Hari Dinampakkan Kesalahan", 18, "Madaniyyah", 28),
        SurahInfo(65, "الطلاق", "At-Talaq", "Talak (Perceraian)", 12, "Madaniyyah", 28),
        SurahInfo(66, "التحريم", "At-Tahrim", "Mengharamkan", 12, "Madaniyyah", 28),
        SurahInfo(67, "الملك", "Al-Mulk", "Kerajaan", 30, "Makkiyyah", 29),
        SurahInfo(68, "القلم", "Al-Qalam", "Pena", 52, "Makkiyyah", 29),
        SurahInfo(69, "الحاقة", "Al-Haqqah", "Hari Kiamat", 52, "Makkiyyah", 29),
        SurahInfo(70, "المعارج", "Al-Ma'arij", "Tempat Naik", 44, "Makkiyyah", 29),
        SurahInfo(71, "نوح", "Nuh", "Nabi Nuh", 28, "Makkiyyah", 29),
        SurahInfo(72, "الجن", "Al-Jinn", "Jin", 28, "Makkiyyah", 29),
        SurahInfo(73, "المزمل", "Al-Muzzammil", "Orang Yang Berselimut", 20, "Makkiyyah", 29),
        SurahInfo(74, "المدثر", "Al-Muddassir", "Orang Yang Berkemul", 56, "Makkiyyah", 29),
        SurahInfo(75, "القيامة", "Al-Qiyamah", "Hari Berbangkit", 40, "Makkiyyah", 29),
        SurahInfo(76, "الإنسان", "Al-Insan", "Manusia", 31, "Madaniyyah", 29),
        SurahInfo(77, "المرسلات", "Al-Mursalat", "Malaikat-Malaikat Yang Diutus", 50, "Makkiyyah", 29),
        SurahInfo(78, "النبإ", "An-Naba'", "Berita Besar", 40, "Makkiyyah", 30),
        SurahInfo(79, "النازعات", "An-Nazi'at", "Malaikat-Malaikat Yang Mencabut", 46, "Makkiyyah", 30),
        SurahInfo(80, "عبس", "'Abasa", "Ia Bermuka Masam", 42, "Makkiyyah", 30),
        SurahInfo(81, "التكوير", "At-Takwir", "Menggulung", 29, "Makkiyyah", 30),
        SurahInfo(82, "الانفطار", "Al-Infitar", "Terbelah", 19, "Makkiyyah", 30),
        SurahInfo(83, "المطففين", "Al-Mutaffifin", "Orang-Orang Curang", 36, "Makkiyyah", 30),
        SurahInfo(84, "الانشقاق", "Al-Insyiqaq", "Terbelah", 25, "Makkiyyah", 30),
        SurahInfo(85, "البروج", "Al-Buruj", "Gugusan Bintang", 22, "Makkiyyah", 30),
        SurahInfo(86, "الطارق", "At-Tariq", "Yang Datang Di Malam Hari", 17, "Makkiyyah", 30),
        SurahInfo(87, "الأعلى", "Al-A'la", "Yang Paling Tinggi", 19, "Makkiyyah", 30),
        SurahInfo(88, "الغاشية", "Al-Gasyiyah", "Hari Pembalasan", 26, "Makkiyyah", 30),
        SurahInfo(89, "الفجر", "Al-Fajr", "Fajar", 30, "Makkiyyah", 30),
        SurahInfo(90, "البلد", "Al-Balad", "Negeri", 20, "Makkiyyah", 30),
        SurahInfo(91, "الشمس", "Asy-Syams", "Matahari", 15, "Makkiyyah", 30),
        SurahInfo(92, "الليل", "Al-Lail", "Malam", 21, "Makkiyyah", 30),
        SurahInfo(93, "الضحى", "Ad-Duha", "Waktu Duha", 11, "Makkiyyah", 30),
        SurahInfo(94, "الشرح", "Asy-Syarh", "Kelapangan", 8, "Makkiyyah", 30),
        SurahInfo(95, "التين", "At-Tin", "Buah Tin", 8, "Makkiyyah", 30),
        SurahInfo(96, "العلق", "Al-'Alaq", "Segumpal Darah", 19, "Makkiyyah", 30),
        SurahInfo(97, "القدر", "Al-Qadr", "Kemuliaan", 5, "Makkiyyah", 30),
        SurahInfo(98, "البينة", "Al-Bayyinah", "Bukti Nyata", 8, "Madaniyyah", 30),
        SurahInfo(99, "الزلزلة", "Az-Zalzalah", "Kegoncangan", 8, "Madaniyyah", 30),
        SurahInfo(100, "العاديات", "Al-'Adiyat", "Kuda Perang Yang Berlari Kencang", 11, "Makkiyyah", 30),
        SurahInfo(101, "القارعة", "Al-Qari'ah", "Hari Kiamat", 11, "Makkiyyah", 30),
        SurahInfo(102, "التكاثر", "At-Takasur", "Bermegah-Megahan", 8, "Makkiyyah", 30),
        SurahInfo(103, "العصر", "Al-'Asr", "Masa / Waktu Sore", 3, "Makkiyyah", 30),
        SurahInfo(104, "الهمزة", "Al-Humazah", "Pengumpat", 9, "Makkiyyah", 30),
        SurahInfo(105, "الفيل", "Al-Fil", "Gajah", 5, "Makkiyyah", 30),
        SurahInfo(106, "قريش", "Quraisy", "Suku Quraisy", 4, "Makkiyyah", 30),
        SurahInfo(107, "الماعون", "Al-Ma'un", "Barang-Barang Berguna", 7, "Makkiyyah", 30),
        SurahInfo(108, "الكوثر", "Al-Kausar", "Nikmat Yang Berlimpah", 3, "Makkiyyah", 30),
        SurahInfo(109, "الكافرون", "Al-Kafirun", "Orang-Orang Kafir", 6, "Makkiyyah", 30),
        SurahInfo(110, "النصر", "An-Nasr", "Pertolongan", 3, "Madaniyyah", 30),
        SurahInfo(111, "اللهب", "Al-Lahab", "Gejolak Api", 5, "Makkiyyah", 30),
        SurahInfo(112, "الإخلاص", "Al-Ikhlas", "Kemurnian Keesaan Allah", 4, "Makkiyyah", 30),
        SurahInfo(113, "الفلق", "Al-Falaq", "Waktu Subuh", 5, "Makkiyyah", 30),
        SurahInfo(114, "الناس", "An-Nas", "Manusia", 6, "Makkiyyah", 30)
    )

    fun getSurah(number: Int): SurahInfo? = surahs.find { it.number == number }

    fun searchSurah(query: String): List<SurahInfo> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return surahs
        return surahs.filter {
            it.nameLatin.lowercase().contains(q) ||
                    it.translation.lowercase().contains(q) ||
                    it.number.toString() == q
        }
    }

    /**
     * Checks local disk cache and curated verses synchronously.
     * Returns emptyList() if not cached yet (to trigger async network download).
     */
    fun getCachedAyahsForSurah(surahNumber: Int, langCode: String = "id"): List<AyahItem> {
        val memorizedKeys = _progressFlow.value.memorizedAyahKeys
        val readKeys = _progressFlow.value.readAyahKeys

        // 1. Check local disk cache
        val cacheDir = File(context.filesDir, "quran_cache")
        val cacheFile = File(cacheDir, "surah_${surahNumber}_${langCode.lowercase()}.json")
        if (cacheFile.exists() && cacheFile.length() > 0) {
            try {
                val type = object : TypeToken<List<AyahItem>>() {}.type
                val list: List<AyahItem> = Gson().fromJson(cacheFile.readText(), type)
                if (!list.isNullOrEmpty()) {
                    return list.map { item ->
                        val key = "${item.surahNumber}:${item.ayahNumber}"
                        item.copy(
                            isMemorized = memorizedKeys.contains(key),
                            isRead = readKeys.contains(key)
                        )
                    }
                }
            } catch (e: Exception) {
                cacheFile.delete()
            }
        }

        // 2. Check built-in curated verses
        val curated = CuratedQuranVerses.getVerses(surahNumber, langCode)
        if (curated.isNotEmpty()) {
            return curated.map { item ->
                val key = "${item.surahNumber}:${item.ayahNumber}"
                item.copy(
                    isMemorized = memorizedKeys.contains(key),
                    isRead = readKeys.contains(key)
                )
            }
        }

        return emptyList()
    }

    /**
     * Asynchronously loads verses for surah from disk cache or official API,
     * saving to disk cache for 100% offline access.
     */
    suspend fun fetchAyahsForSurah(surahNumber: Int, langCode: String = "id"): Result<List<AyahItem>> {
        val memorizedKeys = _progressFlow.value.memorizedAyahKeys
        val readKeys = _progressFlow.value.readAyahKeys

        val result = QuranApiClient.loadVerses(context, surahNumber, langCode)
        return result.map { list ->
            list.map { item ->
                val key = "${item.surahNumber}:${item.ayahNumber}"
                item.copy(
                    isMemorized = memorizedKeys.contains(key),
                    isRead = readKeys.contains(key)
                )
            }
        }
    }
}
