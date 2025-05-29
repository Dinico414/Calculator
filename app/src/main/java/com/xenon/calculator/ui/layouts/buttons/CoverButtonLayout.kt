package com.xenon.calculator.ui.layouts.buttons

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.xenon.calculator.ui.res.MediumPadding
import com.xenon.calculator.ui.res.MediumSpacing
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.CalculatorViewModel

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
            .padding(top = MediumPadding)
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
                            fontFamily = if (originalButtonText == "⌫") firaSansFamily else null, // Make sure firaSansFamily is defined or remove this line if not needed
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
@Preview(
    showBackground = true,
    name = "Landscape Light - Basic",
    widthDp = 800,
    heightDp = 360
)
@Composable
fun CoverLayoutPreviewLightBasic() {
    CalculatorTheme(darkTheme = false) {
        // Set the background color of the Surface to Black
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black // Added this line
        ) {
            val sampleViewModel = CalculatorViewModel()
            // Assuming SmallButtonLayout is a typo and you meant CoverButtonLayout
            CoverButtonLayout(viewModel = sampleViewModel)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    name = "Landscape Dark - Basic",
    widthDp = 800,
    heightDp = 360
)
@Composable
fun CoverLayoutPreviewDarkBasic() {
    CalculatorTheme(darkTheme = true) {
        // Set the background color of the Surface to Black
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black // Added this line
        ) {
            val sampleViewModel = CalculatorViewModel()
            // Assuming SmallButtonLayout is a typo and you meant CoverButtonLayout
            CoverButtonLayout(viewModel = sampleViewModel)
        }
    }
}

// Placeholder for firaSansFamily if it's not defined elsewhere
// val firaSansFamily = null // Replace with your actual FontFamily or remove if not used