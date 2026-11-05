package com.jalanislam.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jalanislam.app.R
import com.jalanislam.app.data.model.PrayerAdjustment
import com.jalanislam.app.data.model.PrayerName
import com.jalanislam.app.ui.theme.IslamicGold

@Composable
fun PrayerAdjustmentDialog(
    initialAdjustment: PrayerAdjustment,
    onDismiss: () -> Unit,
    onSave: (PrayerAdjustment) -> Unit,
    onReset: () -> Unit
) {
    var currentAdjustment by remember { mutableStateOf(initialAdjustment) }
    var validationError by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.manual_adjustment_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.manual_adjustment_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(14.dp))
                DraftPreservationNotice(message = stringResource(R.string.draft_preserved_notice))

                Spacer(modifier = Modifier.height(14.dp))

                // List of prayers to adjust
                PrayerName.entries.forEach { prayer ->
                    val offset = currentAdjustment.getOffsetFor(prayer)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = prayer.titleRes),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    if (offset > -10) {
                                        currentAdjustment = currentAdjustment.copyWithOffset(prayer, offset - 1)
                                        validationError = null
                                    } else {
                                        validationError = "Batas minimum adalah -10 menit"
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Filled.Remove, contentDescription = "Kurang")
                            }

                            val offsetLabel = if (offset > 0) "+$offset" else "$offset"
                            Text(
                                text = "$offsetLabel m",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (offset != 0) IslamicGold else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            IconButton(
                                onClick = {
                                    if (offset < 10) {
                                        currentAdjustment = currentAdjustment.copyWithOffset(prayer, offset + 1)
                                        validationError = null
                                    } else {
                                        validationError = "Batas maksimum adalah +10 menit"
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Tambah")
                            }
                        }
                    }
                }

                // 9. Inline Form Validation Error
                InlineValidationError(errorMessage = validationError)

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        currentAdjustment = PrayerAdjustment()
                        onReset()
                    }) {
                        Text(stringResource(R.string.reset_default))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSave(currentAdjustment) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(stringResource(R.string.save_changes))
                    }
                }
            }
        }
    }
}

