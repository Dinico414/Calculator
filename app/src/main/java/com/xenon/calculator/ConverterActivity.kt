package com.xenon.calculator

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
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xenon.calculator.ui.layouts.ConverterLayout
import com.xenon.calculator.ui.theme.CalculatorTheme


class ConverterActivity : ComponentActivity() {

    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private var activeThemeForConverterActivity: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedPreferenceManager = SharedPreferenceManager(applicationContext)

        activeThemeForConverterActivity = sharedPreferenceManager.theme

        setContent {
            val currentThemePreference = sharedPreferenceManager.theme
            val systemIsInDarkTheme = isSystemInDarkTheme()
            val appIsDarkTheme by remember(currentThemePreference, systemIsInDarkTheme) {
                derivedStateOf {
                    when (currentThemePreference) {
                        0 -> false
                        1 -> true
                        else -> systemIsInDarkTheme
                    }
                }
            }

            CalculatorTheme(darkTheme = appIsDarkTheme) {
                val systemUiController = rememberSystemUiController()
                val view = LocalView.current

                BoxWithConstraints {
                    val screenWidth = this.maxWidth
                    // val targetWidth = 418.30066.dp
                    // val tolerance = 0.5.dp
                    // val isTargetWidthMet = (screenWidth >= targetWidth - tolerance) && (screenWidth <= targetWidth + tolerance)

                    // val systemBarColor = if (isTargetWidthMet) Color.Black else MaterialTheme.colorScheme.background
                    // val darkIcons = !isTargetWidthMet && !appIsDarkTheme

                    val systemBarColor = MaterialTheme.colorScheme.background
                    val useDarkIcons = !appIsDarkTheme


                    if (!view.isInEditMode) {
                        SideEffect {
                            systemUiController.setSystemBarsColor(
                                color = systemBarColor,
                                darkIcons = useDarkIcons,
                                isNavigationBarContrastEnforced = false
                            )
                        }
                    }

                    // val layoutType = when {
                    //     // isTargetWidthMet -> LayoutType.COVER
                    //     screenWidth < 320.dp -> LayoutType.SMALL
                    //     screenWidth < 600.dp -> LayoutType.COMPACT
                    //     screenWidth < 840.dp -> LayoutType.MEDIUM
                    //     else -> LayoutType.EXPANDED
                    // }

                    ConverterLayout(
                        onNavigateBack = { finish() }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val storedTheme = sharedPreferenceManager.theme
        if (activeThemeForConverterActivity != storedTheme) {
            recreate()
        }
    }
}