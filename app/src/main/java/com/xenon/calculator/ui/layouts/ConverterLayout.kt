package com.xenon.calculator.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xenon.calculator.ui.layouts.converter.CompactConverter
import com.xenon.calculator.ui.layouts.converter.CoverConverter
import com.xenon.calculator.ui.layouts.converter.LandscapeCompactConverter
import com.xenon.calculator.ui.layouts.converter.TabletConverter
import com.xenon.calculator.viewmodel.ConverterViewModel
import com.xenon.calculator.viewmodel.LayoutType

@Composable
fun ConverterLayout(
    onNavigateBack: () -> Unit,
    viewModel: ConverterViewModel,
    isLandscape: Boolean,
    layoutType: LayoutType,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (layoutType) {
            LayoutType.COVER -> {
                if (isLandscape) {
                    CoverConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel
                    )
                } else {
                    CoverConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel
                    )
                }
            }

            LayoutType.SMALL -> {
                if (isLandscape) {
                    LandscapeCompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    CompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }
            }

            LayoutType.COMPACT -> {
                if (isLandscape) {
                    LandscapeCompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    CompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }}

            LayoutType.MEDIUM -> {
                if (isLandscape) {
                    TabletConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    TabletConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }
            }

            LayoutType.EXPANDED -> {
                if (isLandscape) {
                    TabletConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    TabletConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }
            }
        }
    }
}
