package com.xenon.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.xenon.calculator.ui.layouts.ButtonLayout
import com.xenon.calculator.ui.layouts.CalculatorViewModel
import com.xenon.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(WindowInsets.safeDrawing.asPaddingValues())
                            .padding(horizontal = 15.dp, vertical = 0.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                    ) {
                        CalculatorApp(viewModel = calculatorViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorApp(viewModel: CalculatorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 0.dp)
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            CalculatorDisplay(
                currentInput = viewModel.currentInput,
                result = viewModel.result,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp, vertical = 24.dp)
            )
        }

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
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = currentInput,
            fontSize = 36.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.End,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = result,
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "App Content Preview")
@Composable
fun DefaultPreview() {
    CalculatorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val previewViewModel = CalculatorViewModel()
                previewViewModel.onButtonClick("123")
                previewViewModel.onButtonClick("+")
                previewViewModel.onButtonClick("456")
                CalculatorApp(viewModel = previewViewModel)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 480)
@Composable
fun CalculatorDisplayPreview() {
    CalculatorTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            CalculatorDisplay(
                currentInput = "12345+67890×(3-1)",
                result = "140780",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            )
        }
    }
}