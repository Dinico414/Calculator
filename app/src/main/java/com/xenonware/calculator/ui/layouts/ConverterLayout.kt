package com.xenonware.calculator.ui.layouts

import androidx.compose.runtime.Composable
import com.xenonware.calculator.ui.layouts.converter.CompactConverter
import com.xenonware.calculator.ui.layouts.converter.CoverConverter
import com.xenonware.calculator.ui.layouts.converter.LandscapeCompactConverter
import com.xenonware.calculator.ui.layouts.converter.TabletConverter
import com.xenonware.calculator.viewmodel.ConverterViewModel
import com.xenonware.calculator.viewmodel.LayoutType

@Composable
fun ConverterLayout(
    onNavigateBack: () -> Unit,
    viewModel: ConverterViewModel,
    isLandscape: Boolean,
    layoutType: LayoutType,
) {
        when (layoutType) {
            LayoutType.COVER -> {
                if (isLandscape) {
                    CoverConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = true
                    )
                } else {
                    CoverConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = false
                    )
                }
            }

            LayoutType.SMALL -> {
                if (isLandscape) {
                    LandscapeCompactConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = true
                    )
                } else {
                    LandscapeCompactConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = false
                    )
                }
            }

            LayoutType.COMPACT -> {
                if (isLandscape) {
                    LandscapeCompactConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = true
                    )
                } else {
                    CompactConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = false
                    )
                }
            }

            LayoutType.MEDIUM, LayoutType.EXPANDED -> {
                if (isLandscape) {
                    TabletConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = true
                    )
                } else {
                    TabletConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = false
                    )
                }
            }
        }

}
