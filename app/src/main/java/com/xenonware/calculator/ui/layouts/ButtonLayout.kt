package com.xenonware.calculator.ui.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xenonware.calculator.ui.layouts.buttons.CompactButtonLayout
import com.xenonware.calculator.ui.layouts.buttons.CompactLandscapeButtonLayout
import com.xenonware.calculator.ui.layouts.buttons.CoverButtonLayout
import com.xenonware.calculator.ui.layouts.buttons.SmallButtonLayout
import com.xenonware.calculator.ui.layouts.buttons.SmallLandscapeButtonLayout
import com.xenonware.calculator.viewmodel.CalculatorViewModel
import com.xenonware.calculator.viewmodel.LayoutType


@Composable
fun ButtonLayout(
    viewModel: CalculatorViewModel,
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
    layoutType: LayoutType,
) {
    when (layoutType) {
        LayoutType.COVER -> {
            if (isLandscape) {
                CoverButtonLayout(viewModel = viewModel, modifier = modifier)
            } else {
                CoverButtonLayout(viewModel = viewModel, modifier = modifier)
            }
        }

        LayoutType.SMALL -> {
            if (isLandscape) {
                SmallLandscapeButtonLayout(viewModel = viewModel, modifier = modifier)
            } else {
                SmallButtonLayout(viewModel = viewModel, modifier = modifier)
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
