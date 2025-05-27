package com.xenon.calculator.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xenon.calculator.ui.layouts.settings.CompactSettings
import com.xenon.calculator.ui.layouts.settings.CoverSettings
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
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel
                    )
                } else {
                    CoverSettings(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel
                    )
                }
            }

            LayoutType.SMALL -> {
                if (isLandscape) {
                    CompactSettings(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    CompactSettings(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }
            }

            LayoutType.COMPACT -> {
                if (isLandscape) {
                    CompactSettings(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    CompactSettings(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }}

            LayoutType.MEDIUM -> {
             if (isLandscape) {
                    CompactSettings(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    CompactSettings(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }
            }

            LayoutType.EXPANDED -> {
                if (isLandscape) {
                    CompactSettings(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    CompactSettings(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }
            }
        }
    }
}
