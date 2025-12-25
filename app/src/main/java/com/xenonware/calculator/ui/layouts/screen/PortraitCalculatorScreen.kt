package com.xenonware.calculator.ui.layouts.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
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
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val inputText = viewModel.displayInputWithSeparators

    // Instantly scroll to the end when input changes
    LaunchedEffect(inputText) {
        scrollState.scrollTo(scrollState.maxValue)
    }

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
                maxFontSize = 52.sp,
                minFontSize = 28.sp,
                scrollState = scrollState,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth()
            )
        }

        Text(
            text = viewModel.result,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = QuicksandTitleVariable,
                lineHeight = 0.8.em
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
    var fontSize by remember { mutableStateOf(maxFontSize) } // ← No key! Persists across text changes
    var shouldScroll by remember { mutableStateOf(false) }

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val safeMarginPx = with(density) { 64.dp.toPx() }

    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        fontWeight = FontWeight.ExtraLight,
        fontSize = fontSize,
        softWrap = false,
        overflow = TextOverflow.Visible,
        maxLines = 1,
        modifier = modifier
            .horizontalScroll(scrollState, enabled = shouldScroll)
            .onSizeChanged { size ->
                if (size.width <= 0) return@onSizeChanged

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
                    layoutResult.didOverflowWidth && fontSize.value > minFontSize.value -> {
                        fontSize = (fontSize.value - 1f).coerceAtLeast(minFontSize.value).sp
                        shouldScroll = false
                    }
                    layoutResult.didOverflowWidth -> {
                        shouldScroll = true // At min size, still overflows → scroll
                    }
                    else -> {
                        shouldScroll = false
                        // Optional: allow growing back when deleting
                        if (fontSize < maxFontSize) {
                            fontSize = maxFontSize // instantly grow back when space allows
                        }
                    }
                }
            }
    )
}