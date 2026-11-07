package com.jalanislam.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalanislam.app.data.quran.AyahItem
import com.jalanislam.app.data.quran.QuranAudioPlayer
import com.jalanislam.app.data.quran.QuranReadingProgress
import com.jalanislam.app.data.quran.QuranRepository
import com.jalanislam.app.data.quran.SurahInfo
import com.jalanislam.app.ui.components.DraftPreservationNotice
import com.jalanislam.app.ui.theme.IslamicGold
import com.jalanislam.app.ui.theme.StateError
import com.jalanislam.app.ui.theme.StateSuccess
import com.jalanislam.app.ui.theme.getThemedTopBarColor
import kotlinx.coroutines.launch

enum class QuranStudyMode {
    TADARUS,
    HAFALAN
}

@Composable
fun SurahDetailScreen(
    surah: SurahInfo,
    quranRepo: QuranRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    currentLanguage: String = "id"
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val audioPlayer = remember { QuranAudioPlayer(context) }
    val listState = rememberLazyListState()

    var studyMode by remember { mutableStateOf(QuranStudyMode.TADARUS) }
    var repeatTimes by remember { mutableIntStateOf(3) }
    var hideTextInHafalan by remember { mutableStateOf(false) }
    var arabicFontSizeSp by remember { mutableFloatStateOf(26f) }
    var showTargetKhatamDialog by remember { mutableStateOf(false) }

    // Verses loading state
    val cachedAyahs = remember(surah.number, currentLanguage) {
        quranRepo.getCachedAyahsForSurah(surah.number, currentLanguage)
    }
    var ayahs by remember { mutableStateOf(cachedAyahs) }
    var isLoading by remember { mutableStateOf(cachedAyahs.isEmpty()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun loadAyahs() {
        coroutineScope.launch {
            isLoading = true
            errorMessage = null
            val result = quranRepo.fetchAyahsForSurah(surah.number, currentLanguage)
            if (result.isSuccess) {
                ayahs = result.getOrNull().orEmpty()
                isLoading = false
            } else {
                isLoading = false
                errorMessage = result.exceptionOrNull()?.localizedMessage
                    ?: "Gagal mengunduh ayat Al-Qur'an. Silakan periksa koneksi internet Anda."
            }
        }
    }

    LaunchedEffect(surah.number, currentLanguage) {
        if (cachedAyahs.isEmpty()) {
            loadAyahs()
        } else {
            ayahs = cachedAyahs
            isLoading = false
        }
    }

    val progress by quranRepo.progressFlow.collectAsState()
    val currentPlayingAyah by audioPlayer.currentAyahNumber.collectAsState()
    var savedNotice by remember { mutableStateOf<String?>(null) }

    val readCountInSurah = remember(ayahs, progress.readAyahKeys) {
        quranRepo.getSurahReadCount(surah.number)
    }
    val surahReadPercentage = remember(readCountInSurah, surah.totalVerses) {
        (readCountInSurah.toFloat() / surah.totalVerses.toFloat() * 100f).coerceIn(0f, 100f)
    }

    val memorizedCountInSurah = remember(ayahs, progress.memorizedAyahKeys) {
        quranRepo.getSurahMemorizedCount(surah.number)
    }
    val surahMemorizedPercentage = remember(memorizedCountInSurah, surah.totalVerses) {
        (memorizedCountInSurah.toFloat() / surah.totalVerses.toFloat() * 100f).coerceIn(0f, 100f)
    }

    // Daily target calculations
    val dailyTarget = progress.dailyVerseTarget
    val remainingToday = (dailyTarget - progress.todayReadCount).coerceAtLeast(0)
    val isDailyTargetAchieved = progress.todayReadCount >= dailyTarget

    // Calculate which ayah serves as today's milestone finish line in this surah
    val targetMilestoneAyah = remember(progress.lastReadAyahNumber, remainingToday, surah.totalVerses) {
        if (remainingToday <= 0) {
            progress.lastReadAyahNumber.coerceIn(1, surah.totalVerses)
        } else {
            (progress.lastReadAyahNumber + remainingToday).coerceIn(1, surah.totalVerses)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.stop()
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(getThemedTopBarColor())
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${surah.number}. ${surah.nameLatin}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${surah.translation} • ${surah.totalVerses} Ayat • ${surah.revelationType}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                    Text(
                        text = surah.nameArabic,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = IslamicGold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = {
                            arabicFontSizeSp = when (arabicFontSizeSp) {
                                26f -> 32f
                                32f -> 38f
                                else -> 26f
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FormatSize,
                            contentDescription = "Ukuran Tulisan Arab",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Mode Selector Tabs: Tadarus vs Hafalan
                TabRow(
                    selectedTabIndex = studyMode.ordinal,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    contentColor = MaterialTheme.colorScheme.primary,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[studyMode.ordinal]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = studyMode == QuranStudyMode.TADARUS,
                        onClick = { studyMode = QuranStudyMode.TADARUS },
                        text = {
                            Text(
                                "Mode Tadarus",
                                fontWeight = FontWeight.Bold,
                                color = if (studyMode == QuranStudyMode.TADARUS) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        },
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (studyMode == QuranStudyMode.TADARUS) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    )
                    Tab(
                        selected = studyMode == QuranStudyMode.HAFALAN,
                        onClick = { studyMode = QuranStudyMode.HAFALAN },
                        text = {
                            Text(
                                "Mode Hafalan",
                                fontWeight = FontWeight.Bold,
                                color = if (studyMode == QuranStudyMode.HAFALAN) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        },
                        icon = {
                            Icon(
                                Icons.Filled.School,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (studyMode == QuranStudyMode.HAFALAN) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Memuat ayat Surah ${surah.nameLatin}...",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Mengambil data resmi Kemenag RI & Al-Quran Utsmani",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        } else if (errorMessage != null && ayahs.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, StateError.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.WifiOff,
                            contentDescription = null,
                            tint = StateError,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Belum Tersimpan di Memori HP",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Surah ${surah.nameLatin} belum sempat diunduh. Silakan aktifkan internet sebentar untuk mengunduh, setelah itu dapat dibaca selamanya secara offline tanpa kuota.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { loadAyahs() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Coba Unduh Sekarang")
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top Progress & Target Khatam Dashboard Card
                item {
                    TargetKhatamProgressCard(
                        studyMode = studyMode,
                        progress = progress,
                        surah = surah,
                        readCountInSurah = readCountInSurah,
                        surahReadPercentage = surahReadPercentage,
                        memorizedCountInSurah = memorizedCountInSurah,
                        surahMemorizedPercentage = surahMemorizedPercentage,
                        dailyTarget = dailyTarget,
                        isDailyTargetAchieved = isDailyTargetAchieved,
                        onOpenTargetDialog = { showTargetKhatamDialog = true },
                        onJumpToLastRead = {
                            coroutineScope.launch {
                                val targetIndex = (progress.lastReadAyahNumber - 1).coerceIn(0, ayahs.size - 1)
                                listState.animateScrollToItem(targetIndex + 2) // offset for headers
                            }
                        }
                    )
                }

                // Mode Hafalan Controls
                if (studyMode == QuranStudyMode.HAFALAN) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Pengaturan Menghafal (Tahfidz)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                // Hide/Reveal Toggle
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (hideTextInHafalan) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Tutup Ayat (Uji Ingatan)",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White
                                        )
                                    }
                                    Switch(
                                        checked = hideTextInHafalan,
                                        onCheckedChange = { hideTextInHafalan = it }
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Repetition selector
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Ulang Audio per Ayat:",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        listOf(1, 3, 5, 10).forEach { count ->
                                            FilterChip(
                                                selected = repeatTimes == count,
                                                onClick = { repeatTimes = count },
                                                label = { Text("${count}x") }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Bismillah Header (except At-Taubah and Al-Fatihah)
                if (surah.number != 9 && surah.number != 1) {
                    item {
                        Text(
                            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        )
                    }
                }

                // Notice if bookmark/progress saved
                if (savedNotice != null) {
                    item {
                        DraftPreservationNotice(message = savedNotice.orEmpty())
                    }
                }

                // Verses List with In-Line Milestone Target Indicator
                items(ayahs) { ayah ->
                    val isPlayingThis = currentPlayingAyah == ayah.ayahNumber
                    val isLastRead = progress.lastReadSurahNumber == surah.number && progress.lastReadAyahNumber == ayah.ayahNumber
                    val isMemorized = progress.memorizedAyahKeys.contains("${surah.number}:${ayah.ayahNumber}")
                    val isRead = progress.readAyahKeys.contains("${surah.number}:${ayah.ayahNumber}")

                    AyahCardItem(
                        ayah = ayah,
                        studyMode = studyMode,
                        hideText = hideTextInHafalan,
                        arabicFontSizeSp = arabicFontSizeSp,
                        isPlaying = isPlayingThis,
                        isLastRead = isLastRead,
                        isMemorized = isMemorized,
                        isRead = isRead,
                        onPlayClick = {
                            if (isPlayingThis) {
                                audioPlayer.stop()
                            } else {
                                audioPlayer.playAyah(
                                    ayah = ayah,
                                    repeatTimes = if (studyMode == QuranStudyMode.HAFALAN) repeatTimes else 1
                                )
                            }
                        },
                        onBookmarkClick = {
                            quranRepo.saveLastRead(surah.number, surah.nameLatin, ayah.ayahNumber)
                            savedNotice = "Penanda bacaan tersimpan: Ayat ke-${ayah.ayahNumber}"
                        },
                        onToggleRead = {
                            val result = quranRepo.markAyahsReadUpTo(surah.number, surah.nameLatin, ayah.ayahNumber)
                            savedNotice = if (result) {
                                "✓ Ayat 1 s/d ${ayah.ayahNumber} ditandai sudah dibaca! Progres tersimpan."
                            } else {
                                "Ayat ke-${ayah.ayahNumber} dihapus dari daftar sudah dibaca."
                            }
                        },
                        onToggleMemorized = {
                            val result = quranRepo.toggleMemorized(surah.number, ayah.ayahNumber)
                            savedNotice = if (result) {
                                "Ayat ke-${ayah.ayahNumber} ditandai hafal!"
                            } else {
                                "Ayat ke-${ayah.ayahNumber} dihapus dari hafalan."
                            }
                        }
                    )

                    // Milestone Finish Line Divider placed right at today's target verse
                    if (studyMode == QuranStudyMode.TADARUS && ayah.ayahNumber == targetMilestoneAyah) {
                        Spacer(modifier = Modifier.height(4.dp))
                        MilestoneTargetDivider(
                            targetAyah = targetMilestoneAyah,
                            targetDays = progress.targetKhatamDays,
                            dailyTarget = dailyTarget,
                            isReached = isDailyTargetAchieved || isRead
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }

    // Dialog for Quick Khatam Commitment Selection
    if (showTargetKhatamDialog) {
        KhatamCommitmentDialog(
            currentDays = progress.targetKhatamDays,
            onSelectDays = { days ->
                quranRepo.setKhatamTargetDays(days)
                showTargetKhatamDialog = false
            },
            onDismiss = { showTargetKhatamDialog = false }
        )
    }
}

@Composable
private fun TargetKhatamProgressCard(
    studyMode: QuranStudyMode,
    progress: QuranReadingProgress,
    surah: SurahInfo,
    readCountInSurah: Int,
    surahReadPercentage: Float,
    memorizedCountInSurah: Int,
    surahMemorizedPercentage: Float,
    dailyTarget: Int,
    isDailyTargetAchieved: Boolean,
    onOpenTargetDialog: () -> Unit,
    onJumpToLastRead: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            width = 1.dp,
            color = if (isDailyTargetAchieved) StateSuccess.copy(alpha = 0.5f) else IslamicGold.copy(alpha = 0.35f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Commitment & Settings
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (studyMode == QuranStudyMode.HAFALAN) Icons.Filled.School else Icons.Filled.TrackChanges,
                        contentDescription = null,
                        tint = IslamicGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (studyMode == QuranStudyMode.HAFALAN) "Target Hafalan Tahfidz"
                        else "Target Khatam: ${progress.targetKhatamDays} Hari",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (studyMode == QuranStudyMode.TADARUS) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IslamicGold.copy(alpha = 0.15f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onOpenTargetDialog() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ubah", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = IslamicGold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (studyMode == QuranStudyMode.TADARUS) {
                // Surah Read Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progres Surah ${surah.nameLatin}:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                    Text(
                        text = "$readCountInSurah / ${surah.totalVerses} Ayat (${surahReadPercentage.toInt()}%)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { surahReadPercentage / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Daily Khatam Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Target Hari Ini (±$dailyTarget ayat/hari):",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                    Text(
                        text = "${progress.todayReadCount} / $dailyTarget Ayat",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDailyTargetAchieved) StateSuccess else IslamicGold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                val dailyProgressFraction = (progress.todayReadCount.toFloat() / dailyTarget.toFloat()).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { dailyProgressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (isDailyTargetAchieved) StateSuccess else IslamicGold,
                    trackColor = IslamicGold.copy(alpha = 0.15f)
                )

                if (isDailyTargetAchieved) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = StateSuccess.copy(alpha = 0.15f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = StateSuccess, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Maa Syaa Allah! Target membaca hari ini telah tuntas tercapai.",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = StateSuccess
                            )
                        }
                    }
                }

                // Jump to last read button
                if (progress.lastReadSurahNumber == surah.number && progress.lastReadAyahNumber > 1) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = onJumpToLastRead,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Filled.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Lanjut ke Ayat Terakhir Dibaca (Ayat ${progress.lastReadAyahNumber})",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // Mode Hafalan Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hafalan Surah Ini:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                    Text(
                        text = "$memorizedCountInSurah / ${surah.totalVerses} Ayat ($surahMemorizedPercentage%)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StateSuccess
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { surahMemorizedPercentage / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = StateSuccess,
                    trackColor = StateSuccess.copy(alpha = 0.15f)
                )
            }
        }
    }
}

@Composable
private fun MilestoneTargetDivider(
    targetAyah: Int,
    targetDays: Int,
    dailyTarget: Int,
    isReached: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isReached) StateSuccess.copy(alpha = 0.12f) else IslamicGold.copy(alpha = 0.12f)
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (isReached) StateSuccess else IslamicGold
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (isReached) Icons.Filled.CheckCircle else Icons.Filled.EmojiEvents,
                contentDescription = null,
                tint = if (isReached) StateSuccess else IslamicGold,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isReached) "🎉 Target Khatam Hari Ini Tercapai di Sini!"
                else "🏁 Batas Target Harian Anda Hari Ini: Ayat ke-$targetAyah",
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
                color = if (isReached) StateSuccess else IslamicGold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (isReached) {
                    "Alhamdulillah! Anda telah membaca ayat ke-$targetAyah dan mencukupi komitmen $dailyTarget ayat/hari."
                } else {
                    "Selesaikan membaca sampai ayat ini untuk memenuhi target khatam dalam $targetDays hari."
                },
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun AyahCardItem(
    ayah: AyahItem,
    studyMode: QuranStudyMode,
    hideText: Boolean,
    arabicFontSizeSp: Float,
    isPlaying: Boolean,
    isLastRead: Boolean,
    isMemorized: Boolean,
    isRead: Boolean,
    onPlayClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onToggleRead: () -> Unit,
    onToggleMemorized: () -> Unit
) {
    var isRevealedLocally by remember { mutableStateOf(false) }

    val cardBg = if (isPlaying) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else if (isLastRead) {
        IslamicGold.copy(alpha = 0.1f)
    } else if (isRead) {
        StateSuccess.copy(alpha = 0.04f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isPlaying || isLastRead) 1.5.dp else if (isRead) 1.dp else 0.dp,
                color = if (isPlaying) MaterialTheme.colorScheme.primary
                else if (isLastRead) IslamicGold
                else if (isRead) StateSuccess.copy(alpha = 0.45f)
                else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Verse Header (Number badge, Read Checkbox, Play Audio, Bookmark, Memorized Check)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Verse number badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isRead) StateSuccess else MaterialTheme.colorScheme.primary
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${ayah.ayahNumber}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    if (isRead) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = StateSuccess.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Sudah Dibaca",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = StateSuccess,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 1-Click Interactive Read Checkmark Button
                    if (studyMode == QuranStudyMode.TADARUS) {
                        IconButton(
                            onClick = onToggleRead,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = if (isRead) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                                contentDescription = "Tandai Selesai Dibaca",
                                tint = if (isRead) StateSuccess else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // Play audio button
                    IconButton(
                        onClick = onPlayClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = "Putar Ayat",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Bookmark button
                    IconButton(
                        onClick = onBookmarkClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isLastRead) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Tandai Bacaan",
                            tint = if (isLastRead) IslamicGold else Color.Gray
                        )
                    }

                    // Hafalan Checkbox
                    if (studyMode == QuranStudyMode.HAFALAN) {
                        IconButton(
                            onClick = onToggleMemorized,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isMemorized) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                                contentDescription = "Sudah Hafal",
                                tint = if (isMemorized) StateSuccess else Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic text (With Hide/Reveal for Tahfidz)
            if (studyMode == QuranStudyMode.HAFALAN && hideText && !isRevealedLocally) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Gray.copy(alpha = 0.15f))
                        .clickable { isRevealedLocally = true }
                        .padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔒 Ketuk untuk membuka teks & mengecek hafalan",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            } else {
                Text(
                    text = ayah.textArabic,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = arabicFontSizeSp.sp,
                        lineHeight = (arabicFontSizeSp * 1.6f).sp
                    ),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Latin Transliteration
                Text(
                    text = ayah.textLatin,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Translation
                Text(
                    text = ayah.textTranslation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun KhatamCommitmentDialog(
    currentDays: Int,
    onSelectDays: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var customDaysText by remember { mutableStateOf("$currentDays") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Pilih Target Khatam Al-Qur'an", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "Tentukan target berapa hari Anda ingin mengkhatamkan Al-Qur'an (6.236 ayat):",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )

                val quickOptions = listOf(
                    30 to "30 Hari (Ramadhan • ~208 ayat/hari)",
                    60 to "60 Hari (2 Bulan • ~104 ayat/hari)",
                    90 to "90 Hari (3 Bulan • ~70 ayat/hari)",
                    180 to "180 Hari (6 Bulan • ~35 ayat/hari)",
                    365 to "365 Hari (1 Tahun • ~17 ayat/hari)"
                )

                quickOptions.forEach { (days, label) ->
                    val isSelected = currentDays == days
                    OutlinedButton(
                        onClick = { onSelectDays(days) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = if (isSelected) ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                        else ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = customDaysText,
                    onValueChange = { customDaysText = it },
                    label = { Text("Atau Isi Jumlah Hari Kustom") },
                    suffix = { Text("Hari") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val days = customDaysText.toIntOrNull()?.coerceIn(1, 365) ?: 30
                    onSelectDays(days)
                }
            ) {
                Text("Simpan Target")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
