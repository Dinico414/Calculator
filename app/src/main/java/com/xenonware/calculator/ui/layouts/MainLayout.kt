package com.xenonware.calculator.ui.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import com.xenonware.calculator.ui.layouts.calculator.CompactCalculator
import com.xenonware.calculator.viewmodel.CalculatorViewModel
import com.xenonware.calculator.viewmodel.LayoutType

@Composable
fun MainLayout (
    viewModel: CalculatorViewModel,
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
    layoutType: LayoutType,
    onOpenSettings: () -> Unit,
    onOpenConverter: () -> Unit,
    appSize: IntSize,
) {
    when (layoutType) {
        LayoutType.COVER -> {
            if (isLandscape) {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    modifier = modifier,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize)
            } else {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    modifier = modifier,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize)
            }
        }

        LayoutType.SMALL -> {
            if (isLandscape) {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    modifier = modifier,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize)
            } else {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    modifier = modifier,
                    onOpenSettings = onOpenSettings,
                    onOpenConverter = onOpenConverter,
                    appSize = appSize)
            }
        }

        LayoutType.COMPACT -> {
            CompactCalculator(
                viewModel = viewModel,
                isLandscape = isLandscape,
                modifier = modifier,
                onOpenSettings = onOpenSettings,
                onOpenConverter = onOpenConverter,
                appSize = appSize
            )
        }

        LayoutType.MEDIUM -> {
//            if (isLandscape) {
//                MediumLandscapeButtonLayout(viewModel = viewModel, modifier = modifier)
//            } else {
//                MediumButtonLayout(viewModel = viewModel, modifier = modifier)
//            }
            CompactCalculator(
                viewModel = viewModel,
                isLandscape = isLandscape,
                modifier = modifier,
                onOpenSettings = onOpenSettings,
                onOpenConverter = onOpenConverter,
                appSize = appSize
            )
        }

        LayoutType.EXPANDED -> {
//            if (isLandscape) {
//                ExpandedLandscapeButtonLayout(viewModel = viewModel, modifier = modifier)
//            } else {
//                ExpandedButtonLayout(viewModel = viewModel, modifier = modifier)
//            }
            CompactCalculator(
                viewModel = viewModel,
                isLandscape = isLandscape,
                modifier = modifier,
                onOpenSettings = onOpenSettings,
                onOpenConverter = onOpenConverter,
                appSize = appSize
            )
        }
    }
}