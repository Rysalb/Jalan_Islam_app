package com.jalanislam.app.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jalanislam.app.ui.theme.IslamicGold

data class TourStepItem(
    val stepNumber: Int,
    val title: String,
    val targetArea: String,
    val description: String,
    val tip: String,
    val icon: ImageVector
)

@Composable
fun InteractiveFeatureTourDialog(
    onDismiss: () -> Unit
) {
    val steps = remember {
        listOf(
            TourStepItem(
                stepNumber = 1,
                title = "Hitung Mundur & Waktu Sholat",
                targetArea = "Bagian Atas Beranda",
                description = "Kartu utama menampilkan hitung mundur waktu sholat berikutnya secara real-time, jadwal Imsak untuk persiapan puasa, dan wilayah lokasi GPS aktif.",
                tip = "Jadwal dihitung menggunakan rumus astronomis presisi tanpa memerlukan kuota internet.",
                icon = Icons.Filled.HourglassTop
            ),
            TourStepItem(
                stepNumber = 2,
                title = "Tombol Cepat Mode Suara Sholat",
                targetArea = "Ikon Speaker di Sebelah Jadwal Sholat",
                description = "Setiap jadwal sholat memiliki tombol suara cepat. Cukup ketuk ikon speaker untuk berganti mode pengingat seketika:",
                tip = "• Hijau = Adzan Penuh berkumandang\n• Oranye = Bunyi Singkat (Beep)\n• Abu-abu = Hening / Getar",
                icon = Icons.AutoMirrored.Filled.VolumeUp
            ),
            TourStepItem(
                stepNumber = 3,
                title = "Panduan Sholat, Wudhu & Doa",
                targetArea = "Kartu Hijau di Bawah Terakhir Dibaca",
                description = "Akses cepat ke panduan ibadah lengkap: Niat sholat 5 waktu & sunnah, rukun bacaan, tata cara wudhu bergambar, serta dzikir dan doa harian dengan pencarian instan.",
                tip = "Tersedia teks Arab, transliterasi Latin yang jelas, dan terjemahan lengkap.",
                icon = Icons.AutoMirrored.Filled.MenuBook
            ),
            TourStepItem(
                stepNumber = 4,
                title = "Navigasi Bawah Modern (Glass Navbar)",
                targetArea = "Floating Navbar di Bagian Bawah",
                description = "Akses mudah ke 5 fitur utama kapan saja: Beranda, Al-Qur'anul Karim, Jadwal 30 Hari, Kompas Kiblat AR, dan Setelan Konfigurasi.",
                tip = "Desain kaca floating navbar modern dan nyaman digunakan dengan satu tangan.",
                icon = Icons.Filled.Navigation
            ),
            TourStepItem(
                stepNumber = 5,
                title = "Al-Qur'anul Karim & Murottal Per Ayat",
                targetArea = "Menu Al-Qur'an (Tab ke-2)",
                description = "Lengkap 30 Juz & 114 Surah. Dilengkapi pemutar audio murottal per ayat Syaikh Mishary Rashid Alafasy, mode hafalan (Tahfidz) dengan pengulangan, serta target khatam harian.",
                tip = "Terjemahan ayat otomatis beradaptasi dengan bahasa yang Anda pilih di setelan.",
                icon = Icons.Filled.AutoStories
            ),
            TourStepItem(
                stepNumber = 6,
                title = "Kustomisasi Audio Adzan Bebas",
                targetArea = "Menu Setelan (Tab ke-5)",
                description = "Anda bebas memilih suara adzan yang berbeda di setiap waktu sholat (misal: Subuh khas Makkah, Dzuhur khas Madinah), atau menggunakan berkas audio MP3 dari memori HP Anda.",
                tip = "Tersedia pula 13 bahasa dunia, tema warna islami, dan fitur pembesar huruf ramah lansia.",
                icon = Icons.Filled.Settings
            )
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    val currentStep = steps[currentIndex]

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "PANDUAN FITUR (${currentStep.stepNumber} dari ${steps.size})",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    TextButton(onClick = onDismiss) {
                        Text("Lewati Tour", style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Animated Card Content
                AnimatedContent(
                    targetState = currentStep,
                    label = "TourStepContent"
                ) { step ->
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = step.icon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = step.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Lokasi: ${step.targetArea}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = IslamicGold,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = step.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = step.tip,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Progress Dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    steps.forEachIndexed { index, _ ->
                        val isCurrent = index == currentIndex
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .height(6.dp)
                                .width(if (isCurrent) 20.dp else 6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (isCurrent) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Nav Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentIndex > 0) {
                        OutlinedButton(
                            onClick = { currentIndex-- },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Sebelumnya")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Button(
                        onClick = {
                            if (currentIndex < steps.size - 1) {
                                currentIndex++
                            } else {
                                onDismiss()
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = if (currentIndex < steps.size - 1) "Lanjut" else "Selesai",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

