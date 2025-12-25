package com.xenonware.calculator.ui.layouts.screen

import android.annotation.SuppressLint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    val scrollState = rememberScrollState()
    val inputText = viewModel.displayInputWithSeparators

    // Instantly scroll to the end whenever the input changes
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
            Text(
                text = inputText,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 52.sp,
                    fontWeight = FontWeight.ExtraLight,
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                softWrap = false,
                overflow = TextOverflow.Visible,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .horizontalScroll(scrollState)
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