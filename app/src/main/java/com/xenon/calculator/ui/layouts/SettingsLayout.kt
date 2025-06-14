package com.xenon.calculator.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xenon.calculator.ui.layouts.settings.CoverSettings
import com.xenon.calculator.ui.layouts.settings.DefaultSettings
import com.xenon.calculator.viewmodel.LayoutType
import com.xenon.calculator.viewmodel.SettingsViewModel


@Composable
fun SettingsLayout(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel,
    isLandscape: Boolean,
    layoutType: LayoutType,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (layoutType) {
            LayoutType.COVER -> {
                if (isLandscape) {
                    CoverSettings(
                        onNavigateBack = onNavigateBack, viewModel = viewModel
                    )
                } else {
                    CoverSettings(
                        onNavigateBack = onNavigateBack, viewModel = viewModel
                    )
                }
            }

            LayoutType.SMALL, LayoutType.COMPACT, LayoutType.MEDIUM, LayoutType.EXPANDED -> {
                if (isLandscape) {
                    DefaultSettings(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = true
                    )
                } else {
                    DefaultSettings(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = false
                    )
                }
            }
        }
    }
}
