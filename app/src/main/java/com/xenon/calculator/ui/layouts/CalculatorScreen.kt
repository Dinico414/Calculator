package com.xenon.calculator.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xenon.calculator.ui.layouts.screen.CompactCalculatorScreen
import com.xenon.calculator.ui.layouts.screen.CompactLandscapeCalculatorScreen
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
//                if (isLandscape) {
//                    CoverCalculatorScreen(viewModel = viewModel)
//                } else {
//                    CoverCalculatorScreen(viewModel = viewModel)
//                }
                if (isLandscape) {
                    CompactLandscapeCalculatorScreen(viewModel = viewModel)
                } else {
                    CompactLandscapeCalculatorScreen(viewModel = viewModel)
                }
            }

            LayoutType.SMALL -> {
//                if (isLandscape) {
//                    SmallLandscapeCalculatorScreen(viewModel = viewModel)
//                } else {
//                    SmallCalculatorScreen(viewModel = viewModel)
//                }
                if (isLandscape) {
                    CompactLandscapeCalculatorScreen(viewModel = viewModel)
                } else {
                    CompactLandscapeCalculatorScreen(viewModel = viewModel)
                }
            }

            LayoutType.COMPACT -> {
                if (isLandscape) {
                    CompactLandscapeCalculatorScreen(viewModel = viewModel)
                } else {
                    CompactCalculatorScreen(viewModel = viewModel)
                }
            }

            LayoutType.MEDIUM -> {
//                if (isLandscape) {
//                    MediumLandscapeCalculatorScreen(viewModel = viewModel)
//                } else {
//                    MediumCalculatorScreen(viewModel = viewModel)
//                }
                if (isLandscape) {
                    CompactLandscapeCalculatorScreen(viewModel = viewModel)
                } else {
                    CompactCalculatorScreen(viewModel = viewModel)
                }
            }

            LayoutType.EXPANDED -> {
//                if (isLandscape) {
//                    ExpandedLandscapeCalculatorScreen(viewModel = viewModel)
//                } else {
//                    ExpandedCalculatorScreen(viewModel = viewModel)
//                }
                if (isLandscape) {
                    CompactLandscapeCalculatorScreen(viewModel = viewModel)
                } else {
                    CompactCalculatorScreen(viewModel = viewModel)
                }
            }
        }
    }
}
