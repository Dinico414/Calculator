package com.xenon.calculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(modifier = Modifier.fillMaxSize()) {
        // Display area (takes up remaining space or a fixed portion)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Takes up space not used by ButtonLayout
                .background(Color.LightGray) // Placeholder for your display
        ) {
            // Your calculator display UI
            Text("Display Area", Modifier.align(Alignment.Center))
        }

        // Button Layout - constrained to max 75% of screen height
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = screenHeight * 0.75f) // Max 75% of screen height
            // .weight(3f) // Alternatively, if display is weight(1f), this gives it 3/4 of space
        ) {
            ButtonLayout(
                viewModel = viewModel,
                isTablet = false, // Replace with actual logic
                isLandscape = false, // Replace with actual logic
                modifier = Modifier.fillMaxSize() // ButtonLayout will fill the Box
            )
        }
    }
}

@Composable
fun DisplaySection(currentInput: String, result: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom // Align text to the bottom
    ) {
        // Workings Text (Current Input)
        Text(
            text = currentInput,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 36.sp), // Adjust size as needed
            color = MaterialTheme.colorScheme.onSecondaryContainer, // Text color on screen
            textAlign = TextAlign.End,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Result Text
        Text(
            text = result,
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 48.sp), // Adjust size as needed
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}