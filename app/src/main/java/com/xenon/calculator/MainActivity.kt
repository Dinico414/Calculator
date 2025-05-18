package com.xenon.calculator // Adjust to your main package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.xenon.calculator.ui.ButtonLayout
import com.xenon.calculator.ui.CalculatorViewModel
import com.xenon.calculator.ui.theme.CalculatorTheme
import androidx.core.view.WindowCompat // <-- Import this

class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        // This allows your app content to draw behind the system bars
        WindowCompat.setDecorFitsSystemWindows(window, false) // <-- Add this line

        setContent {
            CalculatorTheme {
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
            // Apply padding to account for system bars (including navigation bar)
            // This uses the safe drawing insets, which are the parts of the screen
            // not obscured by system UI like status bar, navigation bar, or display cutouts.
            .padding(WindowInsets.safeDrawing.asPaddingValues()) // <-- Add this for overall padding
            .padding(8.dp), // Your existing app-specific padding
        verticalArrangement = Arrangement.Bottom
    ) {
        CalculatorDisplay(
            currentInput = viewModel.currentInput,
            result = viewModel.result,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        ButtonLayout(
            viewModel = viewModel,
            isTablet = false,
            isLandscape = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CalculatorDisplay(currentInput: String, result: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = currentInput,
            fontSize = 36.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End,
            maxLines = 2,
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