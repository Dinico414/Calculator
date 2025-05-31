package com.xenon.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.ViewModelProvider
import com.xenon.calculator.ui.layouts.SettingsLayout
import com.xenon.calculator.ui.theme.ScreenEnvironment
import com.xenon.calculator.viewmodel.SettingsViewModel

class SettingsActivity : ComponentActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.SettingsViewModelFactory(application)
        )[SettingsViewModel::class.java]

        enableEdgeToEdge()

        setContent {

            val persistedAppThemeIndex by settingsViewModel.persistedThemeIndex.collectAsState()
            val coverThemeEnabled by settingsViewModel.enableCoverTheme.collectAsState()
            val containerSize = LocalWindowInfo.current.containerSize
            val applyCoverTheme = remember(containerSize, coverThemeEnabled) {
                settingsViewModel.applyCoverTheme(containerSize)
            }

            ScreenEnvironment(persistedAppThemeIndex, applyCoverTheme) { layoutType, isLandscape ->
                SettingsLayout(
                    onNavigateBack = { finish() },
                    viewModel = settingsViewModel,
                    isLandscape = isLandscape,
                    layoutType = layoutType
                )
            }
        }
    }
}