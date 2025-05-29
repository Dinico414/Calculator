package com.xenon.calculator.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xenon.calculator.viewmodel.LayoutType

@Composable
fun ScreenEnvironment(
    themePreference: Int,
    content: @Composable (layoutType: LayoutType, isLandscape: Boolean) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = this.maxWidth
        val screenHeight = this.maxHeight
        val targetCoverWidth = 418.30066.dp
        val tolerance = 0.5.dp
        val isTargetWidthMet =
            (screenWidth >= targetCoverWidth - tolerance) && (screenWidth <= targetCoverWidth + tolerance)

        val dimensionForLayout = if (isLandscape && !isTargetWidthMet) screenHeight else screenWidth

        val layoutType = when {
            isTargetWidthMet -> LayoutType.COVER
            dimensionForLayout < 320.dp -> LayoutType.SMALL
            dimensionForLayout < 600.dp -> LayoutType.COMPACT
            dimensionForLayout < 840.dp -> LayoutType.MEDIUM
            else -> LayoutType.EXPANDED
        }

        val appIsDarkTheme = if (layoutType == LayoutType.COVER) {
            true
        } else {
            when (themePreference) {
                0 -> false
                1 -> true
                else -> isSystemInDarkTheme()
            }
        }

        CalculatorTheme(darkTheme = appIsDarkTheme) {
            val systemUiController = rememberSystemUiController()
            val view = LocalView.current

            val systemBarColor =
                if (layoutType == LayoutType.COVER) Color.Black else MaterialTheme.colorScheme.background
            val darkIconsForSystemBars =
                if (layoutType == LayoutType.COVER) false else !appIsDarkTheme

            if (!view.isInEditMode) {
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = systemBarColor, darkIcons = darkIconsForSystemBars
                    )

                    systemUiController.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = darkIconsForSystemBars,
                        navigationBarContrastEnforced = false
                    )
                }
            }

            content(layoutType, isLandscape)
        }
    }
}