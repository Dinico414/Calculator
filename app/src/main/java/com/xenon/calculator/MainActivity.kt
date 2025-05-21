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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xenon.calculator.ui.layouts.ButtonLayout
import com.xenon.calculator.ui.layouts.CalculatorScreen
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.CalculatorViewModel
import com.xenon.calculator.viewmodel.LayoutType


class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false) // Crucial for edge-to-edge

        setContent {
            CalculatorTheme {
                // Get screen width for conditional system bar and surface styling
                BoxWithConstraints {
                    val screenWidth = this.maxWidth
                    // Define targetWidth and tolerance (consider moving to constants)
                    val targetWidth = 418.30066.dp
                    val tolerance = 0.5.dp
                    val isTargetWidthMet = (screenWidth >= targetWidth - tolerance) && (screenWidth <= targetWidth + tolerance)

                    // System UI Controller
                    val systemUiController = rememberSystemUiController()
                    val view = LocalView.current

                    // Determine system bar color and icon darkness based on targetWidthMet
                    val systemBarColor = if (isTargetWidthMet) Color.Black else MaterialTheme.colorScheme.background // Or your default system bar color
                    val darkIcons = !isTargetWidthMet // Icons should be light if background is black

                    // Apply system bar colors. Use SideEffect for calls outside of composition.
                    if (!view.isInEditMode) { // Prevents crashing previews
                        SideEffect {
                            systemUiController.setSystemBarsColor(
                                color = systemBarColor,
                                darkIcons = darkIcons,
                                isNavigationBarContrastEnforced = false // Optional: to ensure nav bar color is applied
                            )
                        }
                    }


                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(WindowInsets.safeDrawing.asPaddingValues())
                            .then(
                                if (isTargetWidthMet) {
                                    Modifier
                                        .background(Color.Black)
                                        .padding(horizontal = 0.dp)
                                        .clip(RoundedCornerShape(0.dp))
                                } else {
                                    Modifier
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(horizontal = 15.dp)
                                        .clip(RoundedCornerShape(30.dp))
                                }
                            ),
                        color = if (isTargetWidthMet) Color.Black else MaterialTheme.colorScheme.background
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .then(
                                    if (!isTargetWidthMet) {
                                        Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
                                    } else {
                                        Modifier
                                    }
                                )
                        ) {
                            CalculatorApp(
                                viewModel = calculatorViewModel,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorApp(viewModel: CalculatorViewModel) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = this.maxWidth
        val targetWidth = 418.30066.dp
        val tolerance = 0.5.dp
        val isTargetWidthMet = (screenWidth >= targetWidth - tolerance) && (screenWidth <= targetWidth + tolerance)


        val layoutType = when {
            isTargetWidthMet -> LayoutType.COVER
            screenWidth < 320.dp -> LayoutType.SMALL
            screenWidth < 600.dp -> LayoutType.COMPACT
            screenWidth < 840.dp -> LayoutType.MEDIUM
            else -> LayoutType.EXPANDED
        }

//        LaunchedEffect(layoutType, screenWidth) {
//            Toast.makeText(
//                context, "Layout: ${layoutType.name}, Width: $screenWidth", Toast.LENGTH_SHORT
//            ).show()
//        }

        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
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
                    .weight(0.65f)
            )
        }
    }
}

// ... (Rest of your Previews and other code remains the same) ...

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
    val fakeViewModel = remember {
        CalculatorViewModel()
    }
    CalculatorTheme {
        CalculatorApp(viewModel = fakeViewModel)

    }
}