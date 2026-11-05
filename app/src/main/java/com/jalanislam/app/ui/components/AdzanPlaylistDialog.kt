package com.jalanislam.app.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jalanislam.app.R
import com.jalanislam.app.data.adzan.AdzanSoundItem
import com.jalanislam.app.data.adzan.AdzanSoundManager
import com.jalanislam.app.data.adzan.AdzanSoundStatus
import com.jalanislam.app.data.model.PrayerName
import com.jalanislam.app.ui.theme.IslamicGold
import com.jalanislam.app.ui.theme.StateSuccess
import kotlinx.coroutines.launch

@Composable
fun AdzanPlaylistDialog(
    initialPrayer: PrayerName = PrayerName.FAJR,
    getSoundForPrayer: (PrayerName) -> String,
    onDismiss: () -> Unit,
    onSelectSound: (PrayerName, AdzanSoundItem) -> Unit,
    onApplyToAll: (AdzanSoundItem) -> Unit = {}
) {
    val context = LocalContext.current
    val soundManager = remember { AdzanSoundManager(context) }
    val scope = rememberCoroutineScope()

    // Active prayer being customized (Subuh, Dzuhur, Ashar, Maghrib, Isya)
    val obligatoryPrayers = remember { PrayerName.entries.filter { it.isObligatory } }
    var selectedPrayer by remember { mutableStateOf(if (initialPrayer.isObligatory) initialPrayer else PrayerName.FAJR) }

    val currentSoundId = getSoundForPrayer(selectedPrayer)

    val playlistItems = remember(selectedPrayer) {
        soundManager.getPlaylistForPrayer(selectedPrayer)
    }

    var customList by remember { mutableStateOf(soundManager.listCustomAdzans()) }
    var currentlyPlayingId by remember { mutableStateOf<String?>(null) }
    var downloadingId by remember { mutableStateOf<String?>(null) }
    var downloadProgress by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    // Audio file picker launcher (SAF)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val savedItem = soundManager.saveCustomAudio(uri, "Adzan Kustom HP")
            if (savedItem != null) {
                customList = soundManager.listCustomAdzans()
                successMessage = "Audio HP '${savedItem.title}' berhasil ditambahkan!"
                onSelectSound(selectedPrayer, savedItem)
            } else {
                errorMessage = "Gagal membaca berkas audio yang dipilih."
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            soundManager.stopPreview()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MusicNote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Pilihan Suara Adzan",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Kustomisasi suara unik per waktu sholat",
                            style = MaterialTheme.typography.labelSmall,
                            color = IslamicGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Tutup")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Prayer Selector Chips (Subuh, Dzuhur, Ashar, Maghrib, Isya)
                Text(
                    text = "Pilih Waktu Sholat yang Ingin Diatur:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    obligatoryPrayers.forEach { prayer ->
                        val isSelected = prayer == selectedPrayer
                        val prayerLabel = stringResource(id = prayer.titleRes)

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedPrayer = prayer
                                errorMessage = null
                                successMessage = null
                            },
                            label = {
                                Text(
                                    text = prayerLabel,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            leadingIcon = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Active sound banner for selected prayer
                val isSubuh = selectedPrayer == PrayerName.FAJR
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSubuh) {
                                "Mengatur Adzan Subuh (dengan lafadz Ash-shalātu khairum minan na'ūm)"
                            } else {
                                "Mengatur Adzan ${stringResource(id = selectedPrayer.titleRes)}"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Feedback states (Rule 3 & 10 Agent.MD)
                if (errorMessage != null) {
                    Text(
                        text = errorMessage.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                if (successMessage != null) {
                    Text(
                        text = successMessage.orEmpty(),
                        color = StateSuccess,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                // CTA to Pick from Device Storage
                OutlinedButton(
                    onClick = { filePickerLauncher.launch("audio/*") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Filled.FileOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pilih File Suara MP3 dari HP Anda")
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Custom user adzans section
                    if (customList.isNotEmpty()) {
                        item {
                            Text(
                                text = "Audio Kustom dari Perangkat:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                            )
                        }
                        items(customList) { item ->
                            AdzanSoundRow(
                                item = item,
                                status = item.getStatus(context),
                                isSelected = item.id == currentSoundId,
                                isPlaying = currentlyPlayingId == item.id,
                                isDownloading = downloadingId == item.id,
                                downloadProgress = downloadProgress,
                                targetPrayerName = stringResource(id = selectedPrayer.titleRes),
                                onPlayToggle = {
                                    if (currentlyPlayingId == item.id) {
                                        soundManager.stopPreview()
                                        currentlyPlayingId = null
                                    } else {
                                        soundManager.playPreview(
                                            item,
                                            onCompletion = { currentlyPlayingId = null },
                                            onError = { err ->
                                                currentlyPlayingId = null
                                                errorMessage = err
                                            }
                                        )
                                        currentlyPlayingId = item.id
                                    }
                                },
                                onSelect = {
                                    onSelectSound(selectedPrayer, item)
                                    soundManager.stopPreview()
                                    successMessage = "Suara ${item.title} dipilih untuk ${context.getString(selectedPrayer.titleRes)}!"
                                },
                                onApplyAll = {
                                    onApplyToAll(item)
                                    soundManager.stopPreview()
                                    successMessage = "Suara ${item.title} diterapkan untuk seluruh waktu sholat!"
                                },
                                onDownload = {},
                                onDelete = {}
                            )
                        }
                    }

                    // Curated Playlist section (dedicated Subuh vs Daily)
                    item {
                        Text(
                            text = if (isSubuh) "Variasi Lantunan Khusus Adzan Subuh:" else "Playlist Adzan Sholat Harian:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                        )
                    }

                    items(playlistItems) { item ->
                        val status = remember(refreshTrigger) { item.getStatus(context) }

                        AdzanSoundRow(
                            item = item,
                            status = status,
                            isSelected = item.id == currentSoundId,
                            isPlaying = currentlyPlayingId == item.id,
                            isDownloading = downloadingId == item.id,
                            downloadProgress = downloadProgress,
                            targetPrayerName = stringResource(id = selectedPrayer.titleRes),
                            onPlayToggle = {
                                if (currentlyPlayingId == item.id) {
                                    soundManager.stopPreview()
                                    currentlyPlayingId = null
                                } else {
                                    soundManager.playPreview(
                                        item,
                                        onCompletion = { currentlyPlayingId = null },
                                        onError = { err ->
                                            currentlyPlayingId = null
                                            errorMessage = err
                                        }
                                    )
                                    currentlyPlayingId = item.id
                                }
                            },
                            onSelect = {
                                if (status == AdzanSoundStatus.ONLINE_ONLY) {
                                    // Trigger download first
                                    downloadingId = item.id
                                    downloadProgress = 0
                                    scope.launch {
                                        val res = soundManager.downloadAdzan(item) { pct -> downloadProgress = pct }
                                        downloadingId = null
                                        if (res.isSuccess) {
                                            refreshTrigger++
                                            successMessage = "${item.title} berhasil diunduh dan dipilih untuk ${context.getString(selectedPrayer.titleRes)}!"
                                            onSelectSound(selectedPrayer, item)
                                        } else {
                                            errorMessage = "Gagal mengunduh: ${res.exceptionOrNull()?.localizedMessage}"
                                        }
                                    }
                                } else {
                                    onSelectSound(selectedPrayer, item)
                                    soundManager.stopPreview()
                                    successMessage = "Suara ${item.title} dipilih untuk ${context.getString(selectedPrayer.titleRes)}!"
                                }
                            },
                            onApplyAll = {
                                onApplyToAll(item)
                                soundManager.stopPreview()
                                successMessage = "Suara ${item.title} diterapkan untuk seluruh waktu sholat!"
                            },
                            onDownload = {
                                downloadingId = item.id
                                downloadProgress = 0
                                scope.launch {
                                    val res = soundManager.downloadAdzan(item) { pct -> downloadProgress = pct }
                                    downloadingId = null
                                    if (res.isSuccess) {
                                        refreshTrigger++
                                        successMessage = "${item.title} berhasil disimpan di perangkat!"
                                    } else {
                                        errorMessage = "Gagal mengunduh: ${res.exceptionOrNull()?.localizedMessage}"
                                    }
                                }
                            },
                            onDelete = {
                                soundManager.deleteDownloadedAdzan(item)
                                refreshTrigger++
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        soundManager.stopPreview()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Selesai")
                }
            }
        }
    }
}

@Composable
private fun AdzanSoundRow(
    item: AdzanSoundItem,
    status: AdzanSoundStatus,
    isSelected: Boolean,
    isPlaying: Boolean,
    isDownloading: Boolean,
    downloadProgress: Int,
    targetPrayerName: String,
    onPlayToggle: () -> Unit,
    onSelect: () -> Unit,
    onApplyAll: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 1.5.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Play / Pause preview button
                IconButton(
                    onClick = onPlayToggle,
                    enabled = status != AdzanSoundStatus.ONLINE_ONLY,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (status != AdzanSoundStatus.ONLINE_ONLY)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Stop" else "Putar Preview",
                        tint = if (status != AdzanSoundStatus.ONLINE_ONLY)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Title & Details
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        StatusBadge(status = status)
                    }

                    Text(
                        text = item.origin,
                        style = MaterialTheme.typography.labelSmall,
                        color = IslamicGold,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                        lineHeight = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Selection check indicator
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Terpilih",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Download Progress Bar
            if (isDownloading) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { downloadProgress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "Mengunduh file audio... $downloadProgress%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Actions row: Pilih untuk waktu sholat ini & Terapkan ke semua
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (status == AdzanSoundStatus.ONLINE_ONLY) {
                    OutlinedButton(
                        onClick = onDownload,
                        enabled = !isDownloading,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(Icons.Filled.CloudDownload, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Unduh (${item.fileSizeFormatted})", fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                } else if (status == AdzanSoundStatus.DOWNLOADED && !isSelected) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DeleteOutline,
                            contentDescription = "Hapus audio unduhan",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }

                if (!isSelected) {
                    FilledTonalButton(
                        onClick = onSelect,
                        enabled = !isDownloading,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Pilih untuk $targetPrayerName", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    TextButton(
                        onClick = onApplyAll,
                        enabled = !isDownloading && status != AdzanSoundStatus.ONLINE_ONLY,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Semua Waktu", fontSize = 11.sp)
                    }
                } else {
                    Text(
                        text = "Aktif untuk $targetPrayerName",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: AdzanSoundStatus) {
    val (label, containerColor, contentColor) = when (status) {
        AdzanSoundStatus.BUNDLED -> Triple("Offline", MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), MaterialTheme.colorScheme.primary)
        AdzanSoundStatus.DOWNLOADED -> Triple("Tersimpan", StateSuccess.copy(alpha = 0.12f), StateSuccess)
        AdzanSoundStatus.CUSTOM_LOCAL -> Triple("File HP", IslamicGold.copy(alpha = 0.15f), IslamicGold)
        AdzanSoundStatus.ONLINE_ONLY -> Triple("Online", MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = containerColor
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
        )
    }
}
