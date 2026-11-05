package com.jalanislam.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalanislam.app.data.model.QiblaInfo
import com.jalanislam.app.ui.theme.EmeraldLight
import com.jalanislam.app.ui.theme.IslamicGold
import com.jalanislam.app.ui.theme.StateSuccess
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CompassWidget(
    qiblaInfo: QiblaInfo,
    modifier: Modifier = Modifier
) {
    val animatedHeading by animateFloatAsState(
        targetValue = qiblaInfo.deviceHeading,
        animationSpec = tween(durationMillis = 150),
        label = "HeadingAnimation"
    )

    val isFacingKabah = abs(qiblaInfo.qiblaRelativeAngle) < 3.5f ||
            abs(qiblaInfo.qiblaRelativeAngle - 360f) < 3.5f

    val accentColor by animateColorAsState(
        targetValue = if (isFacingKabah) StateSuccess else IslamicGold,
        label = "AccentColorAnimation"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(4.dp, accentColor.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Rotating Compass Rose
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .rotate(-animatedHeading)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2

                // Outer circle
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.2f),
                    radius = radius,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Degree ticks
                for (deg in 0 until 360 step 15) {
                    val angleRad = Math.toRadians((deg - 90).toDouble())
                    val isMajor = deg % 90 == 0
                    val tickLen = if (isMajor) 16.dp.toPx() else 8.dp.toPx()
                    val strokeW = if (isMajor) 3.dp.toPx() else 1.5.dp.toPx()
                    val color = if (deg == 0) Color.Red else Color.Gray.copy(alpha = 0.6f)

                    val startX = center.x + (radius - tickLen) * cos(angleRad).toFloat()
                    val startY = center.y + (radius - tickLen) * sin(angleRad).toFloat()
                    val endX = center.x + radius * cos(angleRad).toFloat()
                    val endY = center.y + radius * sin(angleRad).toFloat()

                    drawLine(
                        color = color,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = strokeW
                    )
                }

                // Qibla Indicator on Dial
                val qiblaAngleRad = Math.toRadians((qiblaInfo.qiblaBearing - 90).toDouble())
                val qiblaIndicatorX = center.x + (radius - 28.dp.toPx()) * cos(qiblaAngleRad).toFloat()
                val qiblaIndicatorY = center.y + (radius - 28.dp.toPx()) * sin(qiblaAngleRad).toFloat()

                drawCircle(
                    color = IslamicGold,
                    radius = 9.dp.toPx(),
                    center = Offset(qiblaIndicatorX, qiblaIndicatorY)
                )
            }

            // Central Needle pointing to Ka'bah relative to phone
            Icon(
                imageVector = Icons.Filled.Navigation,
                contentDescription = "Jarum Kiblat",
                tint = accentColor,
                modifier = Modifier
                    .size(90.dp)
                    .rotate(qiblaInfo.qiblaRelativeAngle)
            )

            // Ka'bah Central Icon
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isFacingKabah) StateSuccess else MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Mosque,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Heading & Bearing Status
        Text(
            text = if (isFacingKabah) "TEPAT MENGHADAP KA'BAH" else "Putar ponsel hingga jarum mengarah ke atas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isFacingKabah) StateSuccess else MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Sudut Kiblat: ${qiblaInfo.qiblaBearing.toInt()}°  |  Arah Hadap: ${animatedHeading.toInt()}°",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

