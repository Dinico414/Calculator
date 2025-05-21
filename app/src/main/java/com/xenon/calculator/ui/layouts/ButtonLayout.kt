package com.xenon.calculator.ui.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xenon.calculator.ui.layouts.buttons.CompactButtonLayout
import com.xenon.calculator.ui.layouts.buttons.CompactLandscapeButtonLayout
import com.xenon.calculator.viewmodel.CalculatorViewModel
import com.xenon.calculator.viewmodel.LayoutType


@Composable
fun ButtonLayout(
    viewModel: CalculatorViewModel,
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
    layoutType: LayoutType, // This parameter is correctly defined
) {
    when (layoutType) {
        LayoutType.SMALL -> {
//            if (isLandscape) {
//                SmallLandscapeButtonLayout(viewModel = viewModel, modifier = modifier)
//            } else {
//                SmallButtonLayout(viewModel = viewModel, modifier = modifier)
//            }
            if (isLandscape) {
                CompactLandscapeButtonLayout(viewModel = viewModel, modifier = modifier)
            } else {
                CompactButtonLayout(viewModel = viewModel, modifier = modifier)
            }
        }

        LayoutType.COMPACT -> {
            if (isLandscape) {
                CompactLandscapeButtonLayout(viewModel = viewModel, modifier = modifier)
            } else {
                CompactButtonLayout(viewModel = viewModel, modifier = modifier)
            }
        }

        LayoutType.MEDIUM -> {
//            if (isLandscape) {
//                MediumLandscapeButtonLayout(viewModel = viewModel, modifier = modifier)
//            } else {
//                MediumButtonLayout(viewModel = viewModel, modifier = modifier)
//            }
            if (isLandscape) {
                CompactLandscapeButtonLayout(viewModel = viewModel, modifier = modifier)
            } else {
                CompactButtonLayout(viewModel = viewModel, modifier = modifier)
            }
        }

        LayoutType.EXPANDED -> {
//            if (isLandscape) {
//                ExpandedLandscapeButtonLayout(viewModel = viewModel, modifier = modifier)
//            } else {
//                ExpandedButtonLayout(viewModel = viewModel, modifier = modifier)
//            }
            if (isLandscape) {
                CompactLandscapeButtonLayout(viewModel = viewModel, modifier = modifier)
            } else {
                CompactButtonLayout(viewModel = viewModel, modifier = modifier)
            }
        }
    }
}
