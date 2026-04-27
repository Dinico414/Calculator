package com.xenonware.calculator.ui.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntSize
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
    appSize: IntSize,
    layoutType: LayoutType,
) {
        when (layoutType) {
            LayoutType.COVER -> {
                if (isLandscape) {
                    CoverConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = true,
                        appSize = appSize,
                        )
                } else {
                    CoverConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = false,
                        appSize = appSize,
                        )
                }
            }

            LayoutType.SMALL -> {
                if (isLandscape) {
                    LandscapeCompactConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = true,
                        appSize = appSize,
                        )
                } else {
                    LandscapeCompactConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = false,
                        appSize = appSize,
                        )
                }
            }

            LayoutType.COMPACT -> {
                if (isLandscape) {
                    LandscapeCompactConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = true,
                        appSize = appSize,
                        )
                } else {
                    CompactConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = false,
                        appSize = appSize,
                        )
                }
            }

            LayoutType.MEDIUM, LayoutType.EXPANDED -> {
                if (isLandscape) {
                    TabletConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = true,
                        appSize = appSize,

                        )
                } else {
                    TabletConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        layoutType = layoutType,
                        isLandscape = false,
                        appSize = appSize,
                        )
                }
            }
        }

}
