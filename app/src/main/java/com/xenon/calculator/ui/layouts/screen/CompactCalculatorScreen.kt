package com.xenon.calculator.ui.layouts.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xenon.calculator.viewmodel.CalculatorViewModel

@Composable
fun CompactCalculatorScreen(viewModel: CalculatorViewModel) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            DisplaySection(
                currentInput = viewModel.currentInput,
                result = viewModel.result,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
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
        verticalArrangement = Arrangement.Bottom // Keep items aligned to the bottom of this section
    ) {
        // Input Text - takes up the upper 70% of this Column
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
                .weight(0.5f) // Assign 70% of the available vertical space
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
                .weight(0.5f) // Assign 30% of the available vertical space
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DisplaySectionPreview() {
    MaterialTheme {
        DisplaySection(currentInput = "123 + 456", result = "579")
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    val fakeViewModel = remember { CalculatorViewModel() }
    MaterialTheme {
        CompactCalculatorScreen(viewModel = fakeViewModel)
    }
}

@Preview(showBackground = true, name = "Calculator Screen Phone Portrait")
@Composable
fun CalculatorScreenPhonePortraitPreview() {
    val fakeViewModel = remember { CalculatorViewModel() }
    MaterialTheme {
        CompactCalculatorScreen(viewModel = fakeViewModel)
    }
}