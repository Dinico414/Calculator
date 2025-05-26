package com.xenon.calculator

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xenon.calculator.ui.layouts.SettingsLayout
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.LayoutType
import com.xenon.calculator.viewmodel.SettingsViewModel

class SettingsActivity : ComponentActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.SettingsViewModelFactory(application)
        )[SettingsViewModel::class.java]

        setContent {
            val selectedThemeIndex by settingsViewModel.selectedThemeIndex
            val systemIsInDarkTheme = isSystemInDarkTheme()
            val isSettingsDarkTheme by remember(selectedThemeIndex, systemIsInDarkTheme) {
                derivedStateOf {
                    when (selectedThemeIndex) {
                        0 -> false // Light
                        1 -> true  // Dark
                        else -> systemIsInDarkTheme // System
                    }
                }
            }

            val configuration = LocalConfiguration.current
            val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

            CalculatorTheme(darkTheme = isSettingsDarkTheme) {
                val systemUiController = rememberSystemUiController()
                val view = LocalView.current

                BoxWithConstraints {
                    val screenWidth = this.maxWidth
                    // Reinstated the exact same targetWidth and tolerance logic from MainActivity
                    val targetWidth = 418.30066.dp
                    val tolerance = 0.5.dp
                    val isTargetWidthMet = (screenWidth >= targetWidth - tolerance) && (screenWidth <= targetWidth + tolerance)

                    // Apply the same system bar color logic as MainActivity
                    val systemBarColor = if (isTargetWidthMet) Color.Black else MaterialTheme.colorScheme.background
                    val darkIcons = !isTargetWidthMet && !isSettingsDarkTheme // Use isSettingsDarkTheme here

                    if (!view.isInEditMode) {
                        SideEffect {
                            systemUiController.setSystemBarsColor(
                                color = systemBarColor,
                                darkIcons = darkIcons,
                                isNavigationBarContrastEnforced = false
                            )
                        }
                    }

                    val layoutType = when {
                        isTargetWidthMet -> LayoutType.COVER // Cover mode logic reinstated
                        screenWidth < 320.dp -> LayoutType.SMALL
                        screenWidth < 600.dp -> LayoutType.COMPACT
                        screenWidth < 840.dp -> LayoutType.MEDIUM
                        else -> LayoutType.EXPANDED
                    }

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
}