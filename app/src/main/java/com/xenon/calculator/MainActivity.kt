package com.xenon.calculator

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.xenon.calculator.ui.layouts.ButtonLayout
import com.xenon.calculator.ui.layouts.CalculatorScreen
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.CalculatorViewModel
import com.xenon.calculator.viewmodel.LayoutType

class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(WindowInsets.safeDrawing.asPaddingValues())
                            .padding(horizontal = 15.dp)
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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val screenWeight = if (isLandscape) 0.4f else 0.3f
    val buttonLayoutWeight = if (isLandscape) 0.6f else 0.7f

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = this.maxWidth

        val layoutType = when {
            screenWidth < 320.dp -> LayoutType.SMALL
            screenWidth < 600.dp -> LayoutType.COMPACT
            screenWidth < 840.dp -> LayoutType.MEDIUM
            else -> LayoutType.EXPANDED
        }

        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(screenWeight)
                    .padding(horizontal = 10.dp, vertical = 0.dp)
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                CalculatorScreen(
                    viewModel = viewModel,
                    isLandscape = isLandscape,
                    layoutType = layoutType,
                    modifier = Modifier.fillMaxSize()
                )
            }

            ButtonLayout(
                viewModel = viewModel,
                isLandscape = isLandscape,
                layoutType = layoutType,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(buttonLayoutWeight)
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun DefaultPreviewPortrait() {
    CalculatorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
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
                previewViewModel.onButtonClick("=")


                CalculatorScreen(
                    viewModel = previewViewModel,
                    isLandscape = false,
                    layoutType = LayoutType.COMPACT,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun DefaultPreviewLandscape() {
    CalculatorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val previewViewModel = CalculatorViewModel()
                previewViewModel.onButtonClick("10")
                previewViewModel.onButtonClick("×")
                previewViewModel.onButtonClick("5")
                previewViewModel.onButtonClick("=")

                CalculatorScreen(
                    viewModel = previewViewModel,
                    isLandscape = true,
                    layoutType = LayoutType.COMPACT,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview
@Composable
fun CalculatorAppPreview() {
    val fakeViewModel = remember { CalculatorViewModel() }
    CalculatorTheme {
        CalculatorApp(viewModel = fakeViewModel)
    }
}

@Preview(widthDp = 800, heightDp = 360)
@Composable
fun CalculatorAppPreviewLandscape() {
    val fakeViewModel = remember { CalculatorViewModel() }
    CalculatorTheme {
        CalculatorApp(viewModel = fakeViewModel)
    }
}