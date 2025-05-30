package com.xenon.calculator.ui.layouts.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.xenon.calculator.ui.values.LargePadding
import com.xenon.calculator.ui.values.SmallTextFieldPadding
import com.xenon.calculator.viewmodel.CalculatorViewModel

@Composable
fun PortraitCalculatorScreen(viewModel: CalculatorViewModel) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = SmallTextFieldPadding)
        ) {
            DisplaySection(
                currentInput = viewModel.currentInput,
                result = viewModel.result,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(LargePadding)
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
fun DisplaySection(currentInput: String, result: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {

        Text(
            text = currentInput,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 36.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 0.8.em),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Start,
            maxLines = 3,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f)
        )
        Text(
            text = result,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
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