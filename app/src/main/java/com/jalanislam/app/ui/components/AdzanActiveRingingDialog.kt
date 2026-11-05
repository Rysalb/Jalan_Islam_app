package com.jalanislam.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jalanislam.app.R
import com.jalanislam.app.data.model.PrayerName
import com.jalanislam.app.ui.theme.IslamicGold
import com.jalanislam.app.ui.theme.StateError
import com.jalanislam.app.ui.theme.StateSuccess

@Composable
fun AdzanActiveRingingDialog(
    activePrayer: PrayerName?,
    locationName: String,
    soundTitle: String,
    isTest: Boolean,
    onStopAdzan: () -> Unit,
    onDismiss: () -> Unit
) {
    var hasStoppedAudio by remember { mutableStateOf(false) }
    var showDoaDetails by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val dialogMaxHeight = (configuration.screenHeightDp * 0.88f).dp

    // Pulsing audio wave animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val prayerTitle = if (activePrayer != null) {
        stringResource(activePrayer.titleRes)
    } else {
        "Sholat"
    }

    Dialog(
        onDismissRequest = {
            if (hasStoppedAudio) {
                onDismiss()
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = hasStoppedAudio,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(horizontal = 20.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.5.dp,
                    color = if (hasStoppedAudio) StateSuccess.copy(alpha = 0.6f) else IslamicGold.copy(alpha = 0.7f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = dialogMaxHeight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Pulsing Header Icon
                    Box(
                        modifier = Modifier.size(90.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!hasStoppedAudio) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(IslamicGold.copy(alpha = pulseAlpha))
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(
                                    if (hasStoppedAudio) StateSuccess.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                )
                                .border(
                                    width = 2.dp,
                                    color = if (hasStoppedAudio) StateSuccess else IslamicGold,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (hasStoppedAudio) Icons.Filled.CheckCircle
                                              else if (isTest) Icons.Filled.NotificationsActive
                                              else Icons.Filled.Mosque,
                                contentDescription = null,
                                tint = if (hasStoppedAudio) StateSuccess else IslamicGold,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Arabic Basmalah / Takbir
                    Text(
                        text = if (hasStoppedAudio) "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ"
                               else "اللهُ أَكْبَرُ اللهُ أَكْبَرُ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicGold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    if (isTest) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = IslamicGold.copy(alpha = 0.16f),
                            modifier = Modifier.padding(bottom = 6.dp)
                        ) {
                            Text(
                                text = "MODE UJI COBA ALARM ADZAN (+10 DETIK)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Main Title
                    Text(
                        text = if (hasStoppedAudio) {
                            "Suara Adzan Telah Dihentikan"
                        } else if (isTest) {
                            "Uji Coba Alarm Adzan: $prayerTitle"
                        } else {
                            "Waktu Sholat $prayerTitle Telah Masuk!"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Location & Sound details
                    Text(
                        text = "Wilayah: ${locationName.ifBlank { "Indonesia" }}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = soundTitle.ifBlank { "Lantunan Adzan" },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ACTION BUTTON: BIG RED STOP BUTTON
                    if (!hasStoppedAudio) {
                        Button(
                            onClick = {
                                onStopAdzan()
                                hasStoppedAudio = true
                                showDoaDetails = true
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StateError,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.StopCircle,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "HENTIKAN SUARA ADZAN",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Ketuk tombol merah di atas untuk mematikan suara adzan seketika tanpa perlu ke Setelan.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        // Success confirmation & Close button
                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Alhamdulillah, Tutup Dialog",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // COLLAPSIBLE / PROMINENT: Doa Setelah Adzan Section
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { showDoaDetails = !showDoaDetails }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Mosque,
                                        contentDescription = null,
                                        tint = IslamicGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Doa Setelah Adzan",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Icon(
                                    imageVector = if (showDoaDetails) Icons.Filled.KeyboardArrowUp
                                                  else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }

                            AnimatedVisibility(visible = showDoaDetails || hasStoppedAudio) {
                                Column(modifier = Modifier.padding(top = 10.dp)) {
                                    // Arabic
                                    Text(
                                        text = "اللَّهُمَّ رَبَّ هَذِهِ الدَّعْوَةِ التَّامَّةِ، وَالصَّلَاةِ القَائِمَةِ، آتِ مُحَمَّدًا الوَسِيلَةَ وَالفَضِيلَةَ، وَابْعَثْهُ مَقَامًا مَحْمُودًا الَّذِي وَعَدْتَهُ",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        lineHeight = 28.sp,
                                        textAlign = TextAlign.Right,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Latin
                                    Text(
                                        text = "Allahumma rabba hadzihid-da'watit-tammah, wash-sholatil-qo-imah, ati Muhammadanil-wasilata wal-fadhilah, wab'atshu maqomam-mahmudanil-ladzi wa'adtah.",
                                        fontSize = 11.5.sp,
                                        fontStyle = FontStyle.Italic,
                                        color = IslamicGold,
                                        lineHeight = 17.sp
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Meaning
                                    Text(
                                        text = "\"Ya Allah, Pemilik seruan yang sempurna ini dan sholat yang didirikan, berikanlah kepada Nabi Muhammad wasilah dan keutamaan, serta tempatkanlah beliau pada kedudukan terpuji yang telah Engkau janjikan.\"",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

