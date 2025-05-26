package com.xenon.calculator.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xenon.calculator.ui.layouts.converter.CompactConverter
//import com.xenon.calculator.ui.layouts.converter.CoverConverter
import com.xenon.calculator.viewmodel.LayoutType
import com.xenon.calculator.viewmodel.ConverterViewModel


@Composable
fun ConverterLayout(
    onNavigateBack: () -> Unit, // Add this parameter
    viewModel: ConverterViewModel,
    isLandscape: Boolean,
    layoutType: LayoutType,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (layoutType) {
            LayoutType.COVER -> {
                if (isLandscape) {
                    CompactConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel
                    )
                } else {
                    CompactConverter(
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel
                    )
                }
            }

            LayoutType.SMALL -> {
                if (isLandscape) {
                    CompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    CompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }
            }

            LayoutType.COMPACT -> {
                if (isLandscape) {
                    CompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    CompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }}

            LayoutType.MEDIUM -> {
                if (isLandscape) {
                    CompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    CompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }
            }

            LayoutType.EXPANDED -> {
                if (isLandscape) {
                    CompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                } else {
                    CompactConverter(onNavigateBack = onNavigateBack,
                        viewModel = viewModel)
                }
            }
        }
    }
}
