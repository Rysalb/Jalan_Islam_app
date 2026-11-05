package com.jalanislam.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 1. EMERALD PALETTE
private val EmeraldLightScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF064E3B),
    secondary = Color(0xFF854D0E), // Rich dark gold for high contrast on light backgrounds
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),
    background = BackgroundLight,
    surface = CardBackgroundLight,
    surfaceVariant = Color(0xFFF0FDF4),
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextSecondaryLight,
    error = StateError
)

private val EmeraldDarkScheme = darkColorScheme(
    primary = EmeraldLight,
    onPrimary = Color(0xFF06281E),
    primaryContainer = DarkCard,
    onPrimaryContainer = IslamicGoldLight,
    secondary = IslamicGoldLight,
    onSecondary = Color(0xFF06281E),
    secondaryContainer = Color(0xFF78350F),
    onSecondaryContainer = IslamicGoldLight,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkCard,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    error = StateError
)

// 2. NAVY PALETTE
private val NavyLightScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = Color(0xFFD97706),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),
    background = Color(0xFFF8FAFC),
    surface = Color.White,
    surfaceVariant = Color(0xFFF1F5F9),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF475569),
    error = StateError
)

private val NavyDarkScheme = darkColorScheme(
    primary = NavyLight,
    onPrimary = Color(0xFF0B132B),
    primaryContainer = NavySurfaceDark,
    onPrimaryContainer = Color(0xFF93C5FD),
    secondary = Color(0xFFFBBF24),
    onSecondary = Color(0xFF0B132B),
    secondaryContainer = Color(0xFF451A03),
    onSecondaryContainer = Color(0xFFFDE68A),
    background = NavyDark,
    surface = NavySurfaceDark,
    surfaceVariant = Color(0xFF283655),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFFCBD5E1),
    error = StateError
)

// 3. AMBER PALETTE
private val AmberLightScheme = lightColorScheme(
    primary = AmberPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF3C7),
    onPrimaryContainer = Color(0xFF78350F),
    secondary = Color(0xFFD97706),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFDE68A),
    onSecondaryContainer = Color(0xFF78350F),
    background = Color(0xFFFFFBEB),
    surface = Color.White,
    surfaceVariant = Color(0xFFFEF3C7),
    onBackground = Color(0xFF1C1304),
    onSurface = Color(0xFF1C1304),
    onSurfaceVariant = Color(0xFF5A3C08),
    error = StateError
)

private val AmberDarkScheme = darkColorScheme(
    primary = AmberLight,
    onPrimary = Color(0xFF17120A),
    primaryContainer = AmberSurfaceDark,
    onPrimaryContainer = Color(0xFFFDE68A),
    secondary = Color(0xFFFDE68A),
    onSecondary = Color(0xFF17120A),
    secondaryContainer = Color(0xFF451A03),
    onSecondaryContainer = Color(0xFFFEF3C7),
    background = AmberDark,
    surface = AmberSurfaceDark,
    surfaceVariant = Color(0xFF382914),
    onBackground = Color(0xFFFEF3C7),
    onSurface = Color(0xFFFEF3C7),
    onSurfaceVariant = Color(0xFFD4B483),
    error = StateError
)

fun getAppColorScheme(paletteId: String, isDark: Boolean): ColorScheme {
    return when (paletteId.lowercase()) {
        "navy" -> if (isDark) NavyDarkScheme else NavyLightScheme
        "amber" -> if (isDark) AmberDarkScheme else AmberLightScheme
        else -> if (isDark) EmeraldDarkScheme else EmeraldLightScheme
    }
}

@Composable
fun JalanIslamTheme(
    paletteId: String = "emerald",
    themeMode: String = "system",
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode.lowercase()) {
        "dark" -> true
        "light" -> false
        else -> systemDark
    }

    val colorScheme = getAppColorScheme(paletteId, isDark)
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
