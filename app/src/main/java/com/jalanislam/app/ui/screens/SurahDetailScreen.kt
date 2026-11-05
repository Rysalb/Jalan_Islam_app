package com.jalanislam.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import com.jalanislam.app.data.quran.QuranRepository
import com.jalanislam.app.data.quran.SurahInfo
import com.jalanislam.app.ui.components.DraftPreservationNotice
import com.jalanislam.app.ui.theme.IslamicGold
import com.jalanislam.app.ui.theme.StateSuccess

enum class QuranStudyMode {
    TADARUS,
    HAFALAN
}

@Composable
fun SurahDetailScreen(
    surah: SurahInfo,
    quranRepo: QuranRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val audioPlayer = remember { QuranAudioPlayer(context) }

    var studyMode by remember { mutableStateOf(QuranStudyMode.TADARUS) }
    var repeatTimes by remember { mutableIntStateOf(3) }
    var hideTextInHafalan by remember { mutableStateOf(false) }
    var arabicFontSizeSp by remember { mutableFloatStateOf(26f) }

    val rawAyahs = remember(surah.number) { quranRepo.getAyahsForSurah(surah.number) }
    var ayahs by remember { mutableStateOf(rawAyahs) }
    val progress by quranRepo.progressFlow.collectAsState()

    val currentPlayingAyah by audioPlayer.currentAyahNumber.collectAsState()
    var savedNotice by remember { mutableStateOf<String?>(null) }

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
                    .background(MaterialTheme.colorScheme.primary)
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
                    containerColor = Color.White.copy(alpha = 0.15f),
                    contentColor = Color.White,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = studyMode == QuranStudyMode.TADARUS,
                        onClick = { studyMode = QuranStudyMode.TADARUS },
                        text = { Text("Mode Tadarus", fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = studyMode == QuranStudyMode.HAFALAN,
                        onClick = { studyMode = QuranStudyMode.HAFALAN },
                        text = { Text("Mode Hafalan", fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Filled.School, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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

            // Bismillah Header (except At-Taubah)
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

            // Notice if bookmark saved
            if (savedNotice != null) {
                item {
                    DraftPreservationNotice(message = savedNotice.orEmpty())
                }
            }

            // Verses List
            items(ayahs) { ayah ->
                val isPlayingThis = currentPlayingAyah == ayah.ayahNumber
                val isLastRead = progress.lastReadSurahNumber == surah.number && progress.lastReadAyahNumber == ayah.ayahNumber
                val isMemorized = progress.memorizedAyahKeys.contains("${surah.number}:${ayah.ayahNumber}")

                AyahCardItem(
                    ayah = ayah,
                    studyMode = studyMode,
                    hideText = hideTextInHafalan,
                    arabicFontSizeSp = arabicFontSizeSp,
                    isPlaying = isPlayingThis,
                    isLastRead = isLastRead,
                    isMemorized = isMemorized,
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
                    onToggleMemorized = {
                        val result = quranRepo.toggleMemorized(surah.number, ayah.ayahNumber)
                        ayahs = ayahs.map {
                            if (it.ayahNumber == ayah.ayahNumber) it.copy(isMemorized = result) else it
                        }
                    }
                )
            }
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
    onPlayClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onToggleMemorized: () -> Unit
) {
    var isRevealedLocally by remember { mutableStateOf(false) }

    val cardBg = if (isPlaying) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else if (isLastRead) {
        IslamicGold.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isPlaying || isLastRead) 1.5.dp else 0.dp,
                color = if (isPlaying) MaterialTheme.colorScheme.primary else if (isLastRead) IslamicGold else Color.Transparent,
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
            // Verse Header (Number, Play, Bookmark, Memorized Check)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Verse number badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${ayah.ayahNumber}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
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

                // Latin Transliteration (High contrast gold/amber in dark mode, bronze in light mode)
                Text(
                    text = ayah.textLatin,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Indonesian Translation
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
