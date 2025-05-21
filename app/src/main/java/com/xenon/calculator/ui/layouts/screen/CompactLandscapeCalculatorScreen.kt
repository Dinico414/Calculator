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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xenon.calculator.viewmodel.CalculatorViewModel

@Composable
fun CompactLandscapeCalculatorScreen(viewModel: CalculatorViewModel) {
    Log.d("CalculatorDebug", "CompactLandscapeCalculatorScreen Composing")
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(15.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            LandscapeDisplaySection(
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
fun LandscapeDisplaySection(currentInput: String, result: String, modifier: Modifier = Modifier) {
    Log.d("CalculatorDebug", "LandscapeDisplaySection Composing. Input: '$currentInput', Result: '$result'")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentInput,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 36.sp),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Start,
            maxLines = 5,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = 10.dp)
        )

        Text(
            text = result,
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 48.sp),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.End,
            maxLines = 2,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 10.dp)
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
            LandscapeDisplaySection(
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
        CompactLandscapeCalculatorScreen(viewModel = fakeViewModel)
    }
}

@Preview(showBackground = true, name = "Compact Landscape Calculator Screen Tablet", device = Devices.TABLET, widthDp = 1280, heightDp = 800)
@Composable
fun CompactLandscapeCalculatorScreenTabletPreview() {
    val fakeViewModel = remember { CalculatorViewModel() }
    MaterialTheme {
        CompactLandscapeCalculatorScreen(viewModel = fakeViewModel)
    }
}