package com.jalanislam.app.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    targetPrayer: PrayerName,
    currentSoundId: String,
    onDismiss: () -> Unit,
    onSelectSound: (AdzanSoundItem) -> Unit
) {
    val context = LocalContext.current
    val soundManager = remember { AdzanSoundManager(context) }
    val scope = rememberCoroutineScope()

    val playlistItems = remember(targetPrayer) {
        soundManager.getPlaylistForPrayer(targetPrayer)
    }

    var customList by remember { mutableStateOf(soundManager.listCustomAdzans()) }
    var currentlyPlayingId by remember { mutableStateOf<String?>(null) }
    var downloadingId by remember { mutableStateOf<String?>(null) }
    var downloadProgress by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    // Trigger state change to refresh statuses
    var refreshTrigger by remember { mutableIntStateOf(0) }

    // Audio file picker launcher (SAF)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val savedItem = soundManager.saveCustomAudio(uri, "Adzan Kustom HP")
            if (savedItem != null) {
                customList = soundManager.listCustomAdzans()
                successMessage = "File audio berhasil ditambahkan dari perangkat Anda!"
                onSelectSound(savedItem)
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
                .fillMaxHeight(0.90f),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
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
                    Column {
                        val isSubuh = targetPrayer == PrayerName.FAJR
                        Text(
                            text = if (isSubuh) "Pilihan Suara Adzan Subuh" else "Pilihan Suara Adzan",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isSubuh) "Khusus Subuh • Dilengkapi lafadz Ash-shalātu khairum minan na‘ūm" else "Untuk Waktu: ${stringResource(id = targetPrayer.titleRes)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = IslamicGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

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
                        fontWeight = FontWeight.SemiBold,
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
                    Text("Pilih File Suara dari HP Anda")
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
                                    onSelectSound(item)
                                    soundManager.stopPreview()
                                    onDismiss()
                                },
                                onDownload = {},
                                onDelete = {}
                            )
                        }
                    }

                    // Curated Playlist section (dedicated Subuh vs Daily)
                    item {
                        val isSubuh = targetPrayer == PrayerName.FAJR
                        Text(
                            text = if (isSubuh) "Variasi Lantunan Adzan Subuh:" else "Playlist Adzan Sholat Harian:",
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
                                            successMessage = "${item.title} berhasil diunduh!"
                                            onSelectSound(item)
                                            onDismiss()
                                        } else {
                                            errorMessage = "Gagal mengunduh: ${res.exceptionOrNull()?.localizedMessage}"
                                        }
                                    }
                                } else {
                                    onSelectSound(item)
                                    soundManager.stopPreview()
                                    onDismiss()
                                }
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

                TextButton(
                    onClick = {
                        soundManager.stopPreview()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Tutup")
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
    onPlayToggle: () -> Unit,
    onSelect: () -> Unit,
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
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            if (isPlaying) MaterialTheme.colorScheme.primary
                            else if (status != AdzanSoundStatus.ONLINE_ONLY) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            else Color.Gray.copy(alpha = 0.1f)
                        )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = "Dengarkan",
                        tint = if (isPlaying) Color.White else if (status != AdzanSoundStatus.ONLINE_ONLY) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelect() }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = item.origin,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Status Badge (Offline / Downloaded / Online)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        when (status) {
                            AdzanSoundStatus.BUNDLED -> {
                                StatusBadge("Tersedia Bawaan (Offline)", StateSuccess)
                            }
                            AdzanSoundStatus.DOWNLOADED -> {
                                StatusBadge("Sudah Diunduh (Offline)", StateSuccess)
                            }
                            AdzanSoundStatus.CUSTOM_LOCAL -> {
                                StatusBadge("File HP Anda", IslamicGold)
                            }
                            AdzanSoundStatus.ONLINE_ONLY -> {
                                StatusBadge("Perlu Diunduh (${item.fileSizeFormatted})", MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                // Action Button: Download button or Checkmark
                if (status == AdzanSoundStatus.ONLINE_ONLY) {
                    if (isDownloading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 2.5.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        IconButton(onClick = onDownload) {
                            Icon(
                                imageVector = Icons.Filled.Download,
                                contentDescription = "Unduh",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                } else {
                    IconButton(onClick = onSelect) {
                        Icon(
                            imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                            contentDescription = "Pilih",
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            // Download Progress Bar (Rule 2 & 5 Agent.MD)
            AnimatedVisibility(visible = isDownloading) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    LinearProgressIndicator(
                        progress = { downloadProgress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Mengunduh file audio... $downloadProgress%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 9.5.sp
        )
    }
}
