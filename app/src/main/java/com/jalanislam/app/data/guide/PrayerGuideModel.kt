package com.jalanislam.app.data.guide

enum class GuideCategory(val title: String) {
    ALL("Semua"),
    WUDHU("Wudhu"),
    NIAT("Niat Sholat"),
    BACAAN("Bacaan Sholat"),
    DZIKIR_DOA("Dzikir & Doa")
}

data class PrayerGuideItem(
    val id: String,
    val category: GuideCategory,
    val title: String,
    val arabic: String,
    val latin: String,
    val translation: String,
    val note: String? = null,
    val stepNumber: Int? = null
)

