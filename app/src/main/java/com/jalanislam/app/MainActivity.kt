package com.jalanislam.app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import com.jalanislam.app.alarm.PrayerAlarmScheduler
import com.jalanislam.app.ui.MainScreen
import com.jalanislam.app.ui.MainViewModel
import com.jalanislam.app.ui.theme.JalanIslamTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val appPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        val isLocationGranted = fineLocationGranted || coarseLocationGranted
        viewModel.setLocationPermissionGranted(isLocationGranted)
        if (isLocationGranted && viewModel.uiState.value.useGps) {
            viewModel.scanCurrentLocation()
        }
    }

    fun requestAppPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        appPermissionLauncher.launch(permissions.toTypedArray())
    }

    private var onAudioFilePicked: ((Uri) -> Unit)? = null
    private val audioPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onAudioFilePicked?.invoke(uri)
        }
    }

    fun openAudioPicker(callback: (Uri) -> Unit) {
        onAudioFilePicked = callback
        audioPickerLauncher.launch("audio/*")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // Only auto-trigger permissions if user has already completed onboarding
            if (!viewModel.uiState.value.showOnboarding) {
                checkPermissions()
            }
        } catch (t: Throwable) {
            android.util.Log.w("MainActivity", "Permission check on launch non-fatal: ${t.localizedMessage}")
        }

        try {
            // Ensure next upcoming alarm is active
            PrayerAlarmScheduler.scheduleNextAlarm(this)
        } catch (t: Throwable) {
            android.util.Log.w("MainActivity", "Schedule alarm on launch non-fatal: ${t.localizedMessage}")
        }

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val currentDensity = LocalDensity.current
            val scaledDensity = Density(
                density = currentDensity.density,
                fontScale = currentDensity.fontScale * uiState.fontScale
            )

            val locale = remember(uiState.language) {
                Locale(uiState.language).also {
                    Locale.setDefault(it)
                }
            }
            val configuration = remember(uiState.language) {
                Configuration(resources.configuration).apply {
                    setLocale(locale)
                    setLayoutDirection(locale)
                }
            }
            val localizedContext = remember(uiState.language) {
                createConfigurationContext(configuration)
            }
            val isRtl = uiState.language == "ar" || uiState.language == "ur"
            val layoutDirection = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(
                LocalDensity provides scaledDensity,
                LocalConfiguration provides configuration,
                LocalContext provides localizedContext,
                LocalLayoutDirection provides layoutDirection,
                LocalActivityResultRegistryOwner provides this
            ) {
                JalanIslamTheme(
                    paletteId = uiState.themePalette,
                    themeMode = uiState.themeMode
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainScreen(
                            viewModel = viewModel,
                            onRequestLocationPermission = {
                                openAppSettings()
                            },
                            onRequestAppPermissions = {
                                requestAppPermissions()
                            },
                            onRequestAudioPicker = { callback ->
                                openAudioPicker(callback)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun checkPermissions() {
        val fineLocation = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasLocation = fineLocation || coarseLocation
        viewModel.setLocationPermissionGranted(hasLocation)

        if (hasLocation && viewModel.uiState.value.useGps) {
            viewModel.scanCurrentLocation()
        } else if (!hasLocation) {
            requestAppPermissions()
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}
