package com.xenonware.calculator.ui.layouts.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.xenonware.calculator.ui.res.CalculatorButton
import com.xenonware.calculator.ui.values.LargePadding
import com.xenonware.calculator.ui.values.MediumSpacing
import com.xenonware.calculator.viewmodel.CalculatorViewModel

@Composable
fun CoverButtonLayout(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier,
) {
    val buttonSpacing = MediumSpacing

    val numericOpButtonRowsData = listOf(
        listOf("7", "8", "9", "÷", "AC"),
        listOf("4", "5", "6", "×", "( )"),
        listOf("1", "2", "3", "-", "%"),
        listOf(".", "0", "⌫", "+", "=")
    )

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(top = LargePadding)
            .background(color = Color.Black),
        horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
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

