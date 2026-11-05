# Jalan Islam 🕌

<p align="center">
  <img src="Logo%20Aplikasi.jpg" alt="Jalan Islam Logo" width="120" style="border-radius: 20%;" />
</p>

<p align="center">
  <strong>A Modern, Offline-First Islamic Companion Application for Android</strong><br>
  Accurate Prayer Schedules • Authentic Adzan Alarms • Al-Qur'an Tadarus & Memorization • Qibla Compass • Prayer & Wudhu Guide
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" alt="Platform" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white" alt="Compose" />
  <img src="https://img.shields.io/badge/Design-Material%203-7986CB" alt="Material 3" />
  <img src="https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-blue" alt="Min SDK" />
  <img src="https://img.shields.io/badge/Target%20SDK-35%20(Android%2015)-green" alt="Target SDK" />
</p>

---

## 📖 Overview

**Jalan Islam** is a native Android application built with Jetpack Compose and Material 3, crafted to provide Muslims with an elegant, battery-efficient, and comprehensive digital companion for daily worship. Designed with an **offline-first philosophy**, all core features—from astronomical prayer calculations to Holy Qur'an reading and qibla navigation—function seamlessly without an internet connection, while still leveraging online synchronization when available.

---

## ✨ Key Features

### 1. 🕒 Accurate Prayer Times & Live Countdown
* **Dual Calculation Engine**:
  * **Offline Astronomical Engine**: Uses solar position formulas following the standard Ministry of Religious Affairs of the Republic of Indonesia (Kemenag RI) criteria (Subuh 20°, Isya 18°, +2 minutes *Ihtiyath* / safety buffer).
  * **Online Sync Engine**: Real-time synchronization with reputable Islamic prayer APIs (AlAdhan API).
* **Live Countdown Hero**: Real-time ticking countdown to the next prayer time or tomorrow's Imsak with formatted 12h/24h display.
* **Imsak & Sahur Alerts**: Explicit Imsak schedule display and sahur guidelines for Ramadan and voluntary fasting.
* **Manual Minute Adjustment**: Granular fine-tuning (-30 to +30 minutes) for each prayer time to align with local mosque timings.
* **30-Day Monthly Calendar**: Full monthly view with Hijri date conversion and Gregorian date search.

### 2. 📢 Full Adzan Audio Suite & Custom Audio Uploads
* **Authentic Human Vocal Adzans**: High-fidelity recordings from renowned muazzins:
  * *Adzan Makkah (Masjidil Haram)*
  * *Adzan Madinah (Masjid An-Nabawi)*
  * *Adzan Khusus Subuh (with Tatswib: "Ash-shalatu khairum minan-naum")*
  * *Adzan Al-Aqsa (Jerusalem)*
  * *Adzan Mesir (Al-Azhar, Cairo)*
  * *Classic Adzan Rekomendasi (IslamCan)*
* **Device Storage Integration**: Users can import custom `.mp3`, `.m4a`, or `.wav` adzan or takbir recordings directly from their device storage.
* **Built-in Audio Downloader**: Non-bundled adzan tracks can be downloaded on-demand and saved locally for offline use.
* **Flexible Notification Modes**: Per-prayer toggle for:
  * 🔊 **Full Adzan**: Complete vocal recitation with interactive lock-screen dismissal.
  * 🔔 **Beep**: Subtle beep chime for office or quiet environments.
  * 🔕 **Mute**: Silent visual notification.
* **Batch Notification Switcher**: Change all 5 prayer notification modes in a single click.

### 3. ⏰ Reliable Alarm & Notification Architecture
* **WakeLock & Exact Alarms**: Powered by Android's `AlarmManager` with `setAlarmClock` (`RTC_WAKEUP`), guaranteeing alarms trigger even in Deep Sleep (Doze Mode).
* **Foreground Playback Service**: Dedicated `AdzanPlaybackService` with `PRIORITY_MAX` and `CATEGORY_ALARM` heads-up notification, featuring a one-tap **"Hentikan Adzan"** stop button.
* **In-App Alarm & Notification Tester**:
  * **"Tes Sekarang"**: Instant playback test to verify audio volume, vibration, and banner pop-up.
  * **"Tes 10 Detik (Kunci HP)"**: 10-second countdown allowing users to lock their phone and verify background wake-up.
* **Reboot Resilience**: Automatically reschedules all alarms upon device restart via `BootReceiver`.

### 4. 📖 Al-Qur'anul Karim (Tadarus & Tahfidz)
* **Dual Study Modes**:
  * 📖 **Mode Tadarus**: Clean typography with Arabic script, transliteration, and translation.
  * 🎓 **Mode Hafalan (Memorization / Tahfidz)**: Interactive hide/reveal toggle to test memorization ayah by ayah.
* **Audio Recitations**: Play individual verses recited by renowned Qaris (Mishary Rashid Al-Afasy) with play/pause controls.
* **Progress Tracking & Bookmark**: Auto-preserves the last read Surah and Ayah with a one-click "Lanjutkan" resume button on the home screen.
* **Target Khatam Calculator**: Calculate personal daily verse reading quotas based on targeted completion days (e.g., 30 days for Ramadan).
* **Searchable Surahs**: Instant search by Latin name, Arabic name, or Surah number across all 114 Surahs.

### 5. 🧭 Precision Qibla Compass
* **Sensor Fusion Engine**: Combines device accelerometer and geomagnetic sensors with low-pass noise filtering.
* **Ka'bah Bearing & Distance**: Computes great-circle azimuth and geodesic distance to the Holy Ka'bah in Makkah, Saudi Arabia.
* **Visual Haptic Feedback**: Dynamic indicator highlights and vibrates when the device aligns directly with the Qibla (within ±2°).

### 6. 🧎 Sholat & Wudhu Guide (*Panduan Lengkap*)
* **Comprehensive Islamic Worship Guides**:
  * 5 Daily Fardhu Prayer Intentions (*Niat Sholat 5 Waktu*) with solo, makmum, and imam variants.
  * Complete Prayer Recitations (*Rukun Bacaan: Iftitah, Ruku', I'tidal, Sujud, Tahiyyat, Salam*).
  * Step-by-step Wudhu & Tayammum procedures with intentions.
  * After-prayer Dhikr & Sunnah Du'as (*Dzikir & Doa Sesudah Sholat*).
* **High-Contrast Typography**: Clear Arabic calligraphy paired with golden Latin transliteration and translations.

### 7. 🎨 Theming & Accessibility
* **3 Harmonized Islamic Color Palettes**:
  * 🌿 **Hijau Zamrud (Emerald)**: Classic Islamic forest green.
  * 🌌 **Biru Malam / Kiswah (Navy)**: Deep Ka'bah-inspired kiswah navy.
  * 🏜️ **Emas Padang Pasir (Amber)**: Warm desert sand gold.
* **Display Modes**: System Default, Light Mode, and True Dark Mode.
* **Senior-Friendly Text Resizing (Aksesibilitas Lansia)**: Choose between Small (0.85x), Normal (1.0x), Large (1.2x), and Extra (1.4x) font scaling for comfortable reading.
* **Multi-Language Support**: Indonesian, Malay (*Bahasa Melayu*), English, French (*Français*), and German (*Deutsch*).

---

## 🏗️ Architecture & Technology Stack

| Layer | Technologies / Libraries |
| :--- | :--- |
| **Language** | Kotlin 1.9+ |
| **UI Framework** | Jetpack Compose, Material 3, Material Icons Extended |
| **Architecture** | Clean MVVM (Model-View-ViewModel) with Unidirectional Data Flow (UDF) |
| **State Management** | StateFlow, SharedFlow, Jetpack Lifecycle |
| **Asynchronous** | Kotlin Coroutines, Kotlin Flow |
| **Local Storage** | Jetpack DataStore Preferences, SharedPreferences |
| **Media & Audio** | Android `MediaPlayer`, AudioAttributes (`USAGE_ALARM`), Foreground Service |
| **Hardware / Sensors**| SensorManager (TYPE_ACCELEROMETER, TYPE_MAGNETIC_FIELD), LocationManager, FusedLocationProviderClient |
| **Background Tasks** | AlarmManager (`setAlarmClock`, `RTC_WAKEUP`), BroadcastReceivers, WakeLock |
| **Network** | ConnectivityManager NetworkCapabilities, OkHttp / Android HTTP Engine |

---

## 📚 Data Sources & References

1. **Prayer Calculations**:
   * Formulas: Spherical Trigonometry / Astronomical Solar Equations.
   * Criteria: Ministry of Religious Affairs of the Republic of Indonesia (Kemenag RI) standard conventions.
   * Coordinate Database: BPS / Kemendagri Indonesian regency coordinate matrix.
2. **Qur'an Data**:
   * Text: Standard Indonesian Ministry of Religion Mushaf (Kemenag RI) and Tanzil Project.
   * Verse Recitations: Audio CDN curated from Sheikh Mishary Rashid Al-Afasy via EveryAyah.
3. **Adzan Audio**:
   * IslamCan Audio Archive (`https://www.islamcan.com/audio/adhan/`).
   * Haramain Audio Archives (Makkah Al-Mukarramah & Madinah Al-Munawwarah).
4. **Qibla Geometry**:
   * Great-Circle Forward Azimuth formula calculated against Holy Ka'bah coordinates ($21.4225^\circ \text{ N}, 39.8262^\circ \text{ E}$).

---

## 🚀 Getting Started

### Prerequisites
* **Android Studio**: Iguana (2023.2.1), Jellyfish, or newer.
* **JDK**: OpenJDK 17 or 21 (bundled with Android Studio JBR).
* **Android SDK**:
  * Minimum SDK: `API 26` (Android 8.0 Oreo)
  * Target SDK: `API 35` (Android 15)

### Build Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Rysalb/Jalan_Islam_app.git
   cd Jalan_Islam_app
   ```

2. **Build Debug APK via Gradle**:
   * On Windows (PowerShell / Command Prompt):
     ```powershell
     .\gradlew.bat assembleDebug
     ```
   * On macOS / Linux:
     ```bash
     ./gradlew assembleDebug
     ```

3. **Locate Generated APK**:
   ```text
   app/build/outputs/apk/debug/app-debug.apk
   ```

4. **Install to Device / Emulator**:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 🛡️ Permissions & Privacy

Jalan Islam is designed to respect user privacy:
* `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION`: Used solely on-device to determine local prayer times and Qibla direction. Location data is never stored on external servers.
* `POST_NOTIFICATIONS`: Required on Android 13+ to display prayer call banners and ongoing alarm playback controls.
* `SCHEDULE_EXACT_ALARM` / `USE_EXACT_ALARM`: Required to sound the adzan at the precise prayer minute.
* `FOREGROUND_SERVICE` & `FOREGROUND_SERVICE_MEDIA_PLAYBACK`: Ensures uninterrupted adzan audio playback when the screen is locked.
* `READ_MEDIA_AUDIO` / `READ_EXTERNAL_STORAGE`: Optional; requested only when selecting a custom adzan audio file from phone storage.

---

## 🤝 Contributing

Contributions, feature suggestions, and bug reports are warmly welcome!
1. Fork the Project.
2. Create your Feature Branch (`git checkout -b feature/NewIslamicFeature`).
3. Commit your Changes (`git commit -m "feat: Add new prayer notification setting"`).
4. Push to the Branch (`git push origin feature/NewIslamicFeature`).
5. Open a Pull Request.

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

<p align="center">
  Crafted with ❤️ for the Muslim Ummah. May it bring barakah and ease in worship.
</p>
