package com.xenon.calculator.ui.layouts.screen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xenon.calculator.ui.values.LargerPadding
import com.xenon.calculator.ui.values.LargeTextFieldPadding
import com.xenon.calculator.ui.values.LargePadding
import com.xenon.calculator.viewmodel.CalculatorViewModel

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
                .padding(end = LargeTextFieldPadding)
        ) {
            CompactLandscapeDisplaySection(
                currentInput = viewModel.currentInput,
                result = viewModel.result,
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
fun CompactLandscapeDisplaySection(currentInput: String, result: String, modifier: Modifier = Modifier) {
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
                lineHeight = 0.8.em),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Start,
            maxLines = 3,
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
            maxLines = 3,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = LargePadding)
        )
    }
}

@Preview(showBackground = true, name = "Landscape Display Section", widthDp = 800, heightDp = 150)
@Composable
fun LandscapeDisplaySectionPreview() {
    MaterialTheme {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)) {
            CompactLandscapeDisplaySection(
                currentInput = "12345 * (67890 + 123)",
                result = "836854335"
            )
        }
    }
}

@Preview(showBackground = true, name = "Compact Landscape Calculator Screen Phone", widthDp = 720, heightDp = 360)
@Composable
fun CompactLandscapeCalculatorScreenPhonePreview() {
    val fakeViewModel = remember { CalculatorViewModel() }
    MaterialTheme {
        LandscapeCalculatorScreen(viewModel = fakeViewModel)
    }
}

@Preview(showBackground = true, name = "Compact Landscape Calculator Screen Tablet", device = Devices.TABLET, widthDp = 1280, heightDp = 800)
@Composable
fun CompactLandscapeCalculatorScreenTabletPreview() {
    val fakeViewModel = remember { CalculatorViewModel() }
    MaterialTheme {
        LandscapeCalculatorScreen(viewModel = fakeViewModel)
    }
}