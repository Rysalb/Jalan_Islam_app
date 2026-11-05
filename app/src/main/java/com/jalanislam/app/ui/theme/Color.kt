package com.jalanislam.app.ui.theme

import androidx.compose.ui.graphics.Color

// Default Islamic Emerald Palette
val EmeraldPrimary = Color(0xFF0A5C36)
val EmeraldLight = Color(0xFF10B981)
val EmeraldDark = Color(0xFF053B22)

// Gold Accents
val IslamicGold = Color(0xFFD4AF37)
val IslamicGoldLight = Color(0xFFF6D860)
val IslamicGoldDark = Color(0xFFA6851F)

// Navy Palette (Biru Malam / Kiswah)
val NavyPrimary = Color(0xFF1E3A8A)
val NavyLight = Color(0xFF38BDF8)
val NavyDark = Color(0xFF0B132B)
val NavySurfaceDark = Color(0xFF1C2541)

// Amber Palette (Emas Padang Pasir)
val AmberPrimary = Color(0xFF92400E)
val AmberLight = Color(0xFFF59E0B)
val AmberDark = Color(0xFF17120A)
val AmberSurfaceDark = Color(0xFF261D10)

// Surfaces & Backgrounds
val CardBackgroundLight = Color(0xFFFFFFFF)
val BackgroundLight = Color(0xFFF7FAF7)

val DarkBackground = Color(0xFF0F1A15)
val DarkSurface = Color(0xFF182921)
val DarkCard = Color(0xFF21362C)

val TextPrimaryLight = Color(0xFF111827)
val TextSecondaryLight = Color(0xFF4B5563)

val TextPrimaryDark = Color(0xFFF9FAFB)
val TextSecondaryDark = Color(0xFFD1D5DB)

// 10 Core UI States Semantic Colors (Agent.MD)
val StateError = Color(0xFFDC2626)
val StateWarning = Color(0xFFF59E0B)
val StateSuccess = Color(0xFF10B981)
val StateInfo = Color(0xFF0EA5E9)
val StateOffline = Color(0xFF64748B)

enum class AppColorPalette(val id: String, val title: String, val colorCode: Long) {
    EMERALD("emerald", "Hijau Zamrud", 0xFF0A5C36),
    NAVY("navy", "Biru Malam (Kiswah)", 0xFF1E3A8A),
    AMBER("amber", "Emas Padang Pasir", 0xFF92400E)
}

enum class AppThemeMode(val id: String, val title: String) {
    SYSTEM("system", "Ikuti Sistem"),
    DARK("dark", "Mode Gelap (Malam)"),
    LIGHT("light", "Mode Terang (Siang)")
}
