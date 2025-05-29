package com.xenon.calculator.ui.layouts.buttons

// Import your CalculatorButton composable; ensure its path is correct
// Example: import com.xenon.calculator.ui.components.CalculatorButton
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.CalculatorViewModel

@Composable
fun CompactLandscapeButtonLayout(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier,
) {
    val buttonSpacing = 4.dp

    val scientificButtonRowsData = listOf(
        listOf("√", "π", "^", "!"),
        listOf(viewModel.angleUnit.name, "sin", "cos", "tan"),
        listOf("INV", "e", "ln", "log")
    )

    val numericOpButtonRowsData = listOf(
        listOf("7", "8", "9", "÷", "AC"),
        listOf("4", "5", "6", "×", "( )"),
        listOf("1", "2", "3", "-", "%"),
        listOf(".", "0", "⌫", "+", "=")
    )

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
    ) {
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            scientificButtonRowsData.forEach { rowData ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    rowData.forEach { originalButtonText ->
                        val isAngleToggle = originalButtonText == viewModel.angleUnit.name
                        val isInvButton = originalButtonText == "INV"

                        // Determine the text to display (handles inverse functions)
                        val textToDisplay = when {
                            isInvButton -> "INV" // Always show "INV" for the button itself
                            isAngleToggle -> viewModel.angleUnit.name
                            originalButtonText == "√" && viewModel.isInverseMode -> "x²"
                            originalButtonText == "sin" && viewModel.isInverseMode -> "sin⁻¹"
                            originalButtonText == "cos" && viewModel.isInverseMode -> "cos⁻¹"
                            originalButtonText == "tan" && viewModel.isInverseMode -> "tan⁻¹"
                            originalButtonText == "ln" && viewModel.isInverseMode -> "eˣ"
                            originalButtonText == "log" && viewModel.isInverseMode -> "10ˣ"
                            else -> originalButtonText
                        }

                        CalculatorButton(
                            text = textToDisplay,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            isOperator = true, // Most scientific buttons act as operators
                            isSpecial = false,
                            isClear = false,
                            isScientificButton = true, // Crucial: This IS a scientific panel button
                            isNumber = false,
                            isGlobalScientificModeActive = viewModel.isScientificMode, // Pass global state
                            isInverseActive = isInvButton && viewModel.isInverseMode,
                            onClick = {
                                when (originalButtonText) {
                                    "INV" -> viewModel.toggleInverseMode()
                                    viewModel.angleUnit.name -> viewModel.toggleAngleUnit()
                                    // Pass originalButtonText for logic in ViewModel, not textToDisplay
                                    else -> viewModel.onButtonClick(originalButtonText)
                                }
                            }
                        )
                    }
                }
            }
        }

        // Numeric and Operator Buttons Column
        Column(
            modifier = Modifier
                .weight(5f) // Adjust weight as needed for landscape
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            numericOpButtonRowsData.forEach { rowData ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    rowData.forEach { originalButtonText ->
                        val isOp = originalButtonText in listOf("÷", "×", "-", "+", "%", "( )")
                        val isClear = originalButtonText == "AC"
                        val isSpecial = originalButtonText == "="
                        val isNum = originalButtonText in listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "⌫")

                        CalculatorButton(
                            text = originalButtonText,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            isOperator = isOp,
                            isSpecial = isSpecial,
                            isClear = isClear,
                            isScientificButton = false,
                            isNumber = isNum,
                            isGlobalScientificModeActive = viewModel.isScientificMode,
                            isInverseActive = false,
                            // Add this line for the backspace button
                            fontFamily = if (originalButtonText == "⌫") firaSansFamily else null,
                            onClick = {
                                when (originalButtonText) {
                                    "( )" -> viewModel.onParenthesesClick()
                                    else -> viewModel.onButtonClick(originalButtonText)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// Previews remain largely the same, but their appearance will change based on
// the new CalculatorButton logic.

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    name = "Landscape Light - Common Mode",
    widthDp = 800, // Typical landscape width
    heightDp = 360 // Typical landscape height
)
@Composable
fun PhoneLandscapeLayoutPreviewLightCommon() {
    CalculatorTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val sampleViewModel = CalculatorViewModel()
            CompactLandscapeButtonLayout(viewModel = sampleViewModel)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    name = "Landscape Light - Scientific Mode",
    widthDp = 800,
    heightDp = 360
)
@Composable
fun PhoneLandscapeLayoutPreviewLightScientific() {
    CalculatorTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val sampleViewModel = CalculatorViewModel()
            CompactLandscapeButtonLayout(viewModel = sampleViewModel)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    name = "Landscape Dark - Common Mode",
    widthDp = 800,
    heightDp = 360
)
@Composable
fun PhoneLandscapeLayoutPreviewDarkCommon() {
    CalculatorTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val sampleViewModel = CalculatorViewModel()
            CompactLandscapeButtonLayout(viewModel = sampleViewModel)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    name = "Landscape Dark - Scientific Mode",
    widthDp = 800,
    heightDp = 360
)
@Composable
fun PhoneLandscapeLayoutPreviewDarkScientific() {
    CalculatorTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val sampleViewModel = CalculatorViewModel()
            CompactLandscapeButtonLayout(viewModel = sampleViewModel)
        }
    }
}