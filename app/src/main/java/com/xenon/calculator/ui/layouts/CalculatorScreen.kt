package com.xenon.calculator.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xenon.calculator.ui.layouts.screen.LandscapeCalculatorScreen
import com.xenon.calculator.ui.layouts.screen.PortraitCalculatorScreen
import com.xenon.calculator.ui.layouts.screen.SmallCalculatorScreen
import com.xenon.calculator.viewmodel.CalculatorViewModel
import com.xenon.calculator.viewmodel.LayoutType


@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    isLandscape: Boolean,
    layoutType: LayoutType,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (layoutType) {
            LayoutType.COVER -> {
                if (isLandscape) {
                    SmallCalculatorScreen(viewModel = viewModel)
                } else {
                    SmallCalculatorScreen(viewModel = viewModel)
                }
            }

            LayoutType.SMALL -> {
               if (isLandscape) {
                    SmallCalculatorScreen(viewModel = viewModel)
                } else {
                    SmallCalculatorScreen(viewModel = viewModel)
                }
            }

            LayoutType.COMPACT -> {
                if (isLandscape) {
                    LandscapeCalculatorScreen(viewModel = viewModel)
                } else {
                    PortraitCalculatorScreen(viewModel = viewModel)
                }
            }

            LayoutType.MEDIUM -> {
                if (isLandscape) {
                    LandscapeCalculatorScreen(viewModel = viewModel)
                } else {
                    PortraitCalculatorScreen(viewModel = viewModel)
                }
            }

            LayoutType.EXPANDED -> {
                if (isLandscape) {
                    LandscapeCalculatorScreen(viewModel = viewModel)
                } else {
                    PortraitCalculatorScreen(viewModel = viewModel)
                }
            }
        }
    }
}
