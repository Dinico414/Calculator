package com.xenonware.calculator.ui.res.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xenon.mylibrary.values.LargePadding
import com.xenon.mylibrary.values.MediumSpacing
import com.xenonware.calculator.ui.res.CalculatorButton
import com.xenonware.calculator.viewmodel.CalculatorViewModel

@Composable
fun CompactLandscapeButtonLayout(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier,
) {
    val buttonSpacing = MediumSpacing

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
            .padding(LargePadding),
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
                            isOperator = true,
                            isSpecial = false,
                            isClear = false,
                            isScientificButton = true,
                            isNumber = false,
                            isGlobalScientificModeActive = viewModel.isScientificMode, // Pass global state
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
                .weight(5f)
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

                            fontFamily = if (originalButtonText == "⌫") firaSansFamily else null,
                            onClick = {
                                when (originalButtonText) {
                                    "( )" -> viewModel.onParenthesesClick()
                                    else -> viewModel.onButtonClick(originalButtonText)
                                }
                            },
                            onLongClick = if (originalButtonText == "⌫") {
                                { viewModel.onButtonClick("AC") }
                            } else null
                        )
                    }
                }
            }
        }
    }
}