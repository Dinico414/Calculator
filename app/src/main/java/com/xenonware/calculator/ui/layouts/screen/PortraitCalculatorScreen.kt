package com.xenonware.calculator.ui.layouts.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xenon.mylibrary.theme.QuicksandTitleVariable
import com.xenon.mylibrary.values.LargeTextFieldPadding
import com.xenon.mylibrary.values.LargerPadding
import com.xenonware.calculator.viewmodel.CalculatorViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
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
fun CompactPortraitDisplaySection(
    viewModel: CalculatorViewModel,
    fraction: Float = 1f,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val inputText = viewModel.displayInputWithSeparators

    LaunchedEffect(inputText) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    val isAtStart by remember { derivedStateOf { scrollState.value == 0 } }
    val isAtEnd by remember { derivedStateOf { !scrollState.canScrollForward } }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
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
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = QuicksandTitleVariable,
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.End,
            maxLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        )
    }
}
@Composable
fun AutoSizeTextWithScroll(
    text: String,
    maxFontSize: TextUnit,
    minFontSize: TextUnit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    var fontSize by remember { mutableStateOf(maxFontSize) }
    var readyToMeasure by remember { mutableStateOf(false) }

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val safeMarginPx = with(density) { 64.dp.toPx() }

    LaunchedEffect(Unit) {
        readyToMeasure = true
    }

    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        fontWeight = FontWeight.ExtraLight,
        fontSize = fontSize,
        softWrap = false,
        maxLines = 1,
        modifier = modifier
            .horizontalScroll(scrollState, enabled = fontSize <= minFontSize)
            .onSizeChanged { size ->
                if (!readyToMeasure || size.width <= 0 || text.isEmpty()) return@onSizeChanged

                val availableWidth = (size.width - safeMarginPx).coerceAtLeast(1f).toInt()
                val constraints = Constraints(maxWidth = availableWidth)

                val layoutResult = textMeasurer.measure(
                    text = text,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = fontSize,
                        fontWeight = FontWeight.ExtraLight
                    ),
                    constraints = constraints,
                    maxLines = 1,
                    softWrap = false
                )

                when {
                    layoutResult.didOverflowWidth && fontSize > minFontSize -> {
                        fontSize = (fontSize.value - 1f).coerceAtLeast(minFontSize.value).sp
                    }

                    !layoutResult.didOverflowWidth && fontSize < maxFontSize -> {
                        val nextSize = (fontSize.value + 1f).coerceAtMost(maxFontSize.value).sp
                        val testResult = textMeasurer.measure(
                            text = text,
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = nextSize,
                                fontWeight = FontWeight.ExtraLight
                            ),
                            constraints = constraints,
                            maxLines = 1,
                            softWrap = false
                        )
                        if (!testResult.didOverflowWidth) {
                            fontSize = nextSize
                        }
                    }
                }
            }
    )
}