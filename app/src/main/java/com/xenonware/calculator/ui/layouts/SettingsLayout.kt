package com.xenonware.calculator.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xenonware.calculator.ui.layouts.settings.CoverSettings
import com.xenonware.calculator.ui.layouts.settings.DefaultSettings
import com.xenonware.calculator.viewmodel.LayoutType
import com.xenonware.calculator.viewmodel.SettingsViewModel


@Composable
fun SettingsLayout(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel,
    isLandscape: Boolean,
    layoutType: LayoutType,
    onNavigateToDeveloperOptions: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (layoutType) {
            LayoutType.COVER -> {
                if (isLandscape) {
                    CoverSettings(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        isLandscape = true,
                        layoutType = layoutType,
                        onNavigateToDeveloperOptions = onNavigateToDeveloperOptions
                    )
                } else {
                    CoverSettings(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        isLandscape = false,
                        layoutType = layoutType,
                        onNavigateToDeveloperOptions = onNavigateToDeveloperOptions
                    )
                }
            }

            LayoutType.SMALL, LayoutType.COMPACT, LayoutType.MEDIUM, LayoutType.EXPANDED -> {
                if (isLandscape) {
                    DefaultSettings(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = true,
                        onNavigateToDeveloperOptions = onNavigateToDeveloperOptions
                    )
                } else {
                    DefaultSettings(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = false,
                        onNavigateToDeveloperOptions = onNavigateToDeveloperOptions
                    )
                }
            }
        }
    }
}
