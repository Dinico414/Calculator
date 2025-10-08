package com.xenonware.calculator.ui.layouts.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xenonware.calculator.ui.values.LargePadding
import com.xenonware.calculator.ui.values.LargeTextFieldPadding
import com.xenonware.calculator.ui.values.LargerPadding
import com.xenonware.calculator.viewmodel.CalculatorViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SmallCalculatorScreen(viewModel: CalculatorViewModel) {
    Log.d("CalculatorDebug", "CompactLandscapeCalculatorScreen Composing")
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CoverLandscapeDisplaySection(
                currentInput = viewModel.currentInput,
                result = viewModel.result,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = LargeTextFieldPadding)
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
fun CoverLandscapeDisplaySection(currentInput: String, result: String, modifier: Modifier = Modifier) {
    Log.d("CalculatorDebug", "LandscapeDisplaySection Composing. Input: '$currentInput', Result: '$result'")
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentInput,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 0.8.em
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Start,
            maxLines = 4,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = LargePadding)
        )

        Text(
            text = result,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 0.8.em
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.End,
            maxLines = 4,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = LargePadding)
        )
    }
}