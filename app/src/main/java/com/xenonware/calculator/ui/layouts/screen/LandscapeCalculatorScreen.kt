package com.xenonware.calculator.ui.layouts.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xenon.mylibrary.theme.QuicksandTitleVariable
import com.xenon.mylibrary.values.LargePadding
import com.xenon.mylibrary.values.LargeTextFieldPadding
import com.xenon.mylibrary.values.LargerPadding
import com.xenonware.calculator.viewmodel.CalculatorViewModel

@Composable
fun LandscapeCalculatorScreen(viewModel: CalculatorViewModel) {
    Log.d("CalculatorDebug", "CompactLandscapeCalculatorScreen Composing")
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = LargeTextFieldPadding, end = LargeTextFieldPadding)
        ) {
            CompactLandscapeDisplaySection(
                viewModel = viewModel, modifier = Modifier
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
fun CompactLandscapeDisplaySection(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel,
    fraction: Float = 1f,
) {
    val scrollState = rememberScrollState()
    val inputText = viewModel.displayInputWithSeparators

    LaunchedEffect(inputText) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    val isAtStart by remember { derivedStateOf { scrollState.value == 0 } }
    val isAtEnd by remember { derivedStateOf { !scrollState.canScrollForward } }

    Row(
        modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .padding(end = LargerPadding)
         ) {
            AutoSizeTextWithScroll(
                text = inputText,
                maxFontSize = 64.sp,
                minFontSize = 24.sp,
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
                            colors = listOf(
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
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = fraction)
                            )
                        )
                    )
            )
        }

        Text(
            text = viewModel.result,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = QuicksandTitleVariable,
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.End,
            maxLines = 3,
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .padding(start = LargePadding)
        )
    }
}