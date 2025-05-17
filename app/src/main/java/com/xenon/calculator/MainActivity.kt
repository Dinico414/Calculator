package com.xenon.calculator // Adjust to your main package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels // For by viewModels()
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xenon.calculator.ui.ButtonLayout // Your ButtonLayout
import com.xenon.calculator.ui.CalculatorViewModel // Your ViewModel
import com.xenon.calculator.ui.theme.CalculatorTheme // Your app's theme

class MainActivity : ComponentActivity() {
    // Use the by viewModels() delegate to get the ViewModel instance
    private val calculatorViewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme { // Apply your app's theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorApp(viewModel = calculatorViewModel)
                }
            }
        }
    }
}

@Composable
fun CalculatorApp(viewModel: CalculatorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom // Align calculator to the bottom
    ) {
        // Display for input and result
        CalculatorDisplay(
            currentInput = viewModel.currentInput,
            result = viewModel.result,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Takes up remaining space above buttons
        )

        // Button Layout
        ButtonLayout(
            viewModel = viewModel,
            isTablet = false, // Determine this dynamically if needed
            isLandscape = false, // Determine this dynamically if needed
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CalculatorDisplay(currentInput: String, result: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.End // Align text to the right
    ) {
        Text(
            text = currentInput,
            fontSize = 36.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End,
            maxLines = 2, // Allow some wrapping for long input
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = result,
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}