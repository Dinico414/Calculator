package com.xenon.calculator.ui.layouts.buttons

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

    // Data for the Scientific Buttons (3 rows, 4 buttons each)
    val scientificButtonRows = listOf(
        listOf("√", "π", "^", "!"),
        listOf(viewModel.angleUnit.name, "sin", "cos", "tan"),
        listOf("INV", "e", "ln", "log")
    )

    val numericOpButtonRows = listOf(
        listOf("7", "8", "9", "÷", "AC"),
        listOf("4", "5", "6", "×", "( )"),
        listOf("1", "2", "3", "-", "%"),
        listOf(".", "0", "⌫", "+", "=")
    )

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(all = buttonSpacing),
        horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
    ) {
        Column(
            modifier = Modifier
                .weight(0.25f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            scientificButtonRows.forEach { rowData ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    rowData.forEach { originalButtonText ->
                        val isAngleToggle = originalButtonText == viewModel.angleUnit.name
                        val isInvButton = originalButtonText == "INV"
                        val textToDisplay = when {
                            isInvButton -> "INV"
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
                            isOperator = true,
                            isScientific = true,
                            isInverseActive = isInvButton && viewModel.isInverseMode,
                            onClick = {
                                when (originalButtonText) {
                                    "INV" -> viewModel.toggleInverseMode()
                                    viewModel.angleUnit.name -> viewModel.toggleAngleUnit()
                                    else -> viewModel.onButtonClick(originalButtonText)
                                }
                            }
                        )
                    }
                }
            }
        }


        Column(
            modifier = Modifier
                .weight(0.75f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            numericOpButtonRows.forEach { rowData ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    rowData.forEach { originalButtonText ->
                        val isOp = originalButtonText in listOf("÷", "×", "-", "+", "%", "( )")
                        val isClearButton = originalButtonText == "AC"
                        val isSpecialButton = originalButtonText == "="
                        CalculatorButton(
                            text = originalButtonText,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            isOperator = isOp,
                            isSpecial = isSpecialButton,
                            isClear = isClearButton,
                            isScientific = false,
                            isInverseActive = false,
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

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Phone Landscape Mixed Layout Light", widthDp = 800, heightDp = 360)
@Composable
fun PhoneLandscapeMixedLayoutPreviewLight() {
    CalculatorTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val sampleViewModel = CalculatorViewModel()
            CompactLandscapeButtonLayout(viewModel = sampleViewModel)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Phone Landscape Mixed Layout Dark", widthDp = 800, heightDp = 360)
@Composable
fun PhoneLandscapeMixedLayoutPreviewDark() {
    CalculatorTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val sampleViewModel = CalculatorViewModel()
            CompactLandscapeButtonLayout(viewModel = sampleViewModel)
        }
    }
}