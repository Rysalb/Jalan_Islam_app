package com.jalanislam.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalanislam.app.R
import com.jalanislam.app.data.quran.QuranReadingProgress
import com.jalanislam.app.data.quran.QuranRepository
import com.jalanislam.app.data.quran.SurahInfo
import com.jalanislam.app.ui.components.DraftPreservationNotice
import com.jalanislam.app.ui.components.InlineValidationError
import com.jalanislam.app.ui.components.SuccessStateFeedback
import com.jalanislam.app.ui.components.ZeroSearchResultsView
import com.jalanislam.app.ui.theme.IslamicGold
import com.jalanislam.app.ui.theme.StateSuccess

enum class QuranTab(val title: String) {
    SURAHS("Daftar Surah"),
    HAFALAN("Hafalan (Tahfidz)"),
    KHATAM("Target Khatam")
}

@Composable
fun QuranScreen(
    quranRepo: QuranRepository,
    onSelectSurah: (SurahInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(QuranTab.SURAHS) }
    var searchQuery by remember { mutableStateOf("") }
    val progress by quranRepo.progressFlow.collectAsState()

    val surahs = remember(searchQuery) { quranRepo.searchSurah(searchQuery) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Header with Logo Aplikasi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "Logo Jalan Islam",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Al-Qur'anul Karim",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Tadarus, Hafalan & Target Khatam",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search field (shown on Surah list tab)
                if (selectedTab == QuranTab.SURAHS) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama surah atau nomor...") },
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Filled.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Tab Selector
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = Color.White.copy(alpha = 0.15f),
                    contentColor = Color.White,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    QuranTab.entries.forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = { Text(tab.title, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            QuranTab.SURAHS -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Last Read Card (Shortcut to resume reading)
                    item {
                        LastReadCard(
                            progress = progress,
                            onClick = {
                                val surah = quranRepo.getSurah(progress.lastReadSurahNumber)
                                if (surah != null) onSelectSurah(surah)
                            }
                        )
                    }

                    // Zero Search Results State (Agent.md rule 6)
                    if (surahs.isEmpty() && searchQuery.isNotEmpty()) {
                        item {
                            ZeroSearchResultsView(
                                query = searchQuery,
                                onResetQuery = { searchQuery = "" },
                                onFallbackAction = { searchQuery = "" }
                            )
                        }
                    }

                    items(surahs) { surah ->
                        SurahItemCard(
                            surah = surah,
                            onClick = { onSelectSurah(surah) }
                        )
                    }
                }
            }

            QuranTab.HAFALAN -> {
                HafalanDashboardTab(
                    progress = progress,
                    quranRepo = quranRepo,
                    onSelectSurah = onSelectSurah,
                    modifier = modifier.padding(innerPadding)
                )
            }

            QuranTab.KHATAM -> {
                KhatamTargetTab(
                    progress = progress,
                    quranRepo = quranRepo,
                    modifier = modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun LastReadCard(
    progress: QuranReadingProgress,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(IslamicGold.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = null,
                    tint = IslamicGold,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Terakhir Dibaca",
                    style = MaterialTheme.typography.labelSmall,
                    color = IslamicGold,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${progress.lastReadSurahName} : Ayat ${progress.lastReadAyahNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Lanjutkan", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SurahItemCard(
    surah: SurahInfo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Surah Number Diamond/Circle
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${surah.number}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = surah.nameLatin,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${surah.revelationType} • ${surah.totalVerses} Ayat",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Text(
                text = surah.nameArabic,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun HafalanDashboardTab(
    progress: QuranReadingProgress,
    quranRepo: QuranRepository,
    onSelectSurah: (SurahInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Ayat Dihafal",
                        style = MaterialTheme.typography.labelSmall,
                        color = IslamicGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${progress.totalMemorized}",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 44.sp),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "dari total 6.236 ayat Al-Qur'an",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        item {
            Text(
                text = "Surah Paling Sering Dihafal (Juz 'Amma & Populer):",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Recommend Juz 30 and popular surahs
        val popularSurahNumbers = listOf(1, 67, 36, 18, 112, 113, 114, 108, 103)
        val popularSurahs = popularSurahNumbers.mapNotNull { quranRepo.getSurah(it) }

        items(popularSurahs) { surah ->
            val memorizedCountInSurah = progress.memorizedAyahKeys.count { it.startsWith("${surah.number}:") }
            val isComplete = memorizedCountInSurah >= surah.totalVerses

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectSurah(surah) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "${surah.number}. ${surah.nameLatin}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Hafal: $memorizedCountInSurah / ${surah.totalVerses} Ayat",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isComplete) StateSuccess else IslamicGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (isComplete) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(StateSuccess.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Khatam Hafal", color = StateSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KhatamTargetTab(
    progress: QuranReadingProgress,
    quranRepo: QuranRepository,
    modifier: Modifier = Modifier
) {
    var targetDaysInput by remember { mutableStateOf("${progress.targetKhatamDays}") }
    var validationError by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Kalkulator Target Khatam",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tentukan target berapa hari Anda ingin mengkhatamkan Al-Qur'an (misal: 30 hari selama bulan Ramadhan).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = targetDaysInput,
                    onValueChange = {
                        targetDaysInput = it
                        val intVal = it.toIntOrNull()
                        if (intVal == null || intVal !in 1..365) {
                            validationError = "Target harus berupa angka antara 1 sampai 365 hari"
                        } else {
                            validationError = null
                        }
                    },
                    label = { Text("Target Hari Khatam") },
                    suffix = { Text("Hari") },
                    singleLine = true,
                    isError = validationError != null,
                    modifier = Modifier.fillMaxWidth()
                )

                // 9. Inline Form Validation Error
                InlineValidationError(errorMessage = validationError)

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val intVal = targetDaysInput.toIntOrNull()
                        if (intVal != null && intVal in 1..365) {
                            quranRepo.setKhatamTargetDays(intVal)
                            showSuccess = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Hitung & Simpan Target")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 10. Success State Feedback
        if (showSuccess) {
            SuccessStateFeedback(
                title = "Target Berhasil Diperbarui!",
                description = "Komitmen harian Anda telah disesuaikan.",
                nextActionLabel = "Tutup",
                onNextAction = { showSuccess = false }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Daily Quota Result Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Target Membaca Harian Anda:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IslamicGold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${progress.dailyVerseTarget} Ayat / Hari",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                val pagesEstimate = (progress.dailyVerseTarget / 10).coerceAtLeast(1)
                Text(
                    text = "atau setara dengan ± $pagesEstimate lembar mushaf per hari",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

