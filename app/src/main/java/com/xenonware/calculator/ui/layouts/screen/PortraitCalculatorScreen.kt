package com.xenonware.calculator.ui.layouts.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xenon.mylibrary.theme.QuicksandTitleVariable
import com.xenon.mylibrary.values.LargeTextFieldPadding
import com.xenon.mylibrary.values.LargerPadding
import com.xenonware.calculator.viewmodel.CalculatorViewModel

@Composable
fun PortraitCalculatorScreen(viewModel: CalculatorViewModel) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = LargeTextFieldPadding)
        ) {
            CompactPortraitDisplaySection(
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
fun CompactPortraitDisplaySection(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val inputScrollState = rememberScrollState()

    // Auto-scroll to bottom when new input arrives
    LaunchedEffect(viewModel.displayInputWithSeparators) {
        inputScrollState.animateScrollTo(inputScrollState.maxValue)
    }

    // Correctly detect top/bottom using the actual ScrollState
    val isAtTop by remember { derivedStateOf { inputScrollState.value == 0 } }
    val isAtBottom by remember { derivedStateOf { inputScrollState.value >= inputScrollState.maxValue } }

    // Fade intensity (smooth partial fade near edges)
    val fadeHeightPx = with(LocalDensity.current) { 64.dp.toPx() }

    val topFadeFraction by remember { derivedStateOf {
        if (isAtTop) 0f else {
            (inputScrollState.value.toFloat() / fadeHeightPx).coerceAtMost(1f)
        }
    } }

    val bottomFadeFraction by remember { derivedStateOf {
        if (isAtBottom) 0f else {
            ((inputScrollState.maxValue - inputScrollState.value).toFloat() / fadeHeightPx).coerceAtMost(1f)
        }
    } }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        // ========= INPUT SECTION (Scrollable with dynamic fades) =========
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(inputScrollState)
                .padding(horizontal = 16.dp)
        ) {
            // Main input text
            Text(
                text = viewModel.displayInputWithSeparators,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Light,
                    lineHeight = 1.1.em
                ),
                color = colorScheme.onSecondaryContainer,
                textAlign = TextAlign.End,
                softWrap = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
            )

            // Bottom fade overlay — visible when NOT at bottom (can scroll up)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(64.dp)
                    .graphicsLayer { alpha = bottomFadeFraction }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                colorScheme.surfaceDim.copy(alpha = bottomFadeFraction)
                            )
                        )
                    )
            )

            // Top fade overlay — visible when NOT at top (can scroll down)
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(64.dp)
                    .graphicsLayer { alpha = topFadeFraction }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorScheme.surfaceDim.copy(alpha = topFadeFraction),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        // ========= RESULT SECTION =========
        BasicText(
            text = viewModel.result,
            style = TextStyle(
                fontSize = 48.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = QuicksandTitleVariable,
                lineHeight = 1.em,
                color = colorScheme.onSecondaryContainer,
                textAlign = TextAlign.End
            ),
            autoSize = TextAutoSize.StepBased(
                minFontSize = 12.sp,
                maxFontSize = 48.sp,
                stepSize = 1.sp
            ),
            maxLines = Int.MAX_VALUE,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .padding(end = 16.dp, bottom = 8.dp)
        )
    }
}