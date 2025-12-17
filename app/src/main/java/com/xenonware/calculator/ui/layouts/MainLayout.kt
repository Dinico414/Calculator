package com.xenonware.calculator.ui.layouts

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntSize
import com.xenonware.calculator.ui.layouts.calculator.CompactCalculator
import com.xenonware.calculator.ui.layouts.calculator.CoverCalculator
import com.xenonware.calculator.viewmodel.CalculatorViewModel
import com.xenonware.calculator.viewmodel.LayoutType

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainLayout(
    viewModel: CalculatorViewModel,
    isLandscape: Boolean,
    layoutType: LayoutType,
    onOpenSettings: () -> Unit,
    onOpenConverter: () -> Unit,
    appSize: IntSize,
) {

    when (layoutType) {
        LayoutType.COVER -> {
            if (isLandscape) {
                CoverCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize
                )
            } else {
                CoverCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize
                )
            }
        }

        LayoutType.SMALL -> {
            if (isLandscape) {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize
                )
            } else {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize
                )
            }
        }

        LayoutType.COMPACT -> {
            if (isLandscape) {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize
                )
            } else {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize
                )
            }
        }

        LayoutType.MEDIUM -> {
            if (isLandscape) {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize
                )
            } else {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize
                )
            }
        }

        LayoutType.EXPANDED -> {
            if (isLandscape) {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize
                )
            } else {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize
                )
            }
        }
    }
}
