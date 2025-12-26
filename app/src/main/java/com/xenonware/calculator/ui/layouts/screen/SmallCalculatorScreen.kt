package com.xenonware.calculator.ui.layouts.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xenon.mylibrary.values.LargeTextFieldPadding
import com.xenon.mylibrary.values.LargerPadding
import com.xenonware.calculator.viewmodel.CalculatorViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SmallCalculatorScreen(viewModel: CalculatorViewModel) {
    Log.d("CalculatorDebug", "SmallCalculatorScreen Composing")
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CoverLandscapeDisplaySection(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(LargerPadding)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = screenHeight * 0.75f)
        )
    }
}

@Composable
fun CoverLandscapeDisplaySection(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel,
    fraction: Float = 1f,
) {
    val scrollState = rememberScrollState()
    val inputText = viewModel.displayInputWithSeparators
    val rawResult = viewModel.result

    LaunchedEffect(inputText) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    val isAtStart by remember { derivedStateOf { scrollState.value == 0 } }
    val isAtEnd by remember { derivedStateOf { !scrollState.canScrollForward } }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .padding(end = LargeTextFieldPadding)
        ) {
            AutoSizeTextWithScroll(
                text = inputText,
                maxFontSize = 32.sp,
                minFontSize = 18.sp,
                scrollState = scrollState,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight()
                    .width(32.dp)
                    .graphicsLayer { alpha = if (isAtStart) 0f else fraction }
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = fraction),
                                Color.Transparent
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(32.dp)
                    .graphicsLayer { alpha = if (isAtEnd) 0f else fraction }
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = fraction)
                            )
                        )
                    )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {
            SmartResultText(
                rawResult = rawResult, modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth()
            )
        }
    }
}