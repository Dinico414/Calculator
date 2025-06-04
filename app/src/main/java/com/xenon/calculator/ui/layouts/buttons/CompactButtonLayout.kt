package com.xenon.calculator.ui.layouts.buttons

//import androidx.compose.animation.slideInVertically // Kept removed for simpler animation as discussed
//import androidx.compose.animation.slideOutVertically // Kept removed for simpler animation as discussed
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.xenon.calculator.R
import com.xenon.calculator.ui.res.CalculatorButton
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.ui.values.CompactWideButtonHeight
import com.xenon.calculator.ui.values.CompactWideButtonWidth
import com.xenon.calculator.ui.values.LargePadding
import com.xenon.calculator.ui.values.LargeSpacing
import com.xenon.calculator.ui.values.LargeWideButtonHeight
import com.xenon.calculator.ui.values.LargeWideButtonWidth
import com.xenon.calculator.ui.values.LargerSpacing
import com.xenon.calculator.ui.values.LargestSpacing
import com.xenon.calculator.ui.values.MediumSpacing
import com.xenon.calculator.ui.values.NoPadding
import com.xenon.calculator.ui.values.SmallButtonSizeSpacing
import com.xenon.calculator.ui.values.SmallestPadding
import com.xenon.calculator.viewmodel.CalculatorViewModel

val firaSansFamily = FontFamily(
    Font(R.font.fira_sans, FontWeight.Normal)
)

val ButtonWidth = CompactWideButtonWidth


val ButtonHeight = CompactWideButtonHeight



@Composable
fun CompactButtonLayout(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight(0.7f)
            .padding(LargePadding)
    ) {
        ScientificButtonsRow1(
            viewModel, modifier = Modifier
                .height(SmallButtonSizeSpacing)
                .fillMaxWidth()
        )

        val spacerHeight by animateDpAsState(
            targetValue = if (viewModel.isScientificMode) MediumSpacing else LargestSpacing,
            animationSpec = tween(durationMillis = 300),
            label = "ScientificModeSpacerHeight"
        )
        Spacer(Modifier.height(spacerHeight))

        Column(
            modifier = Modifier
                .weight(1f)
                .animateContentSize(animationSpec = tween(durationMillis = 300)) // Faster content size animation
        ) {
            val animatedScientificRowsInnerWeight by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0.2f else 0f,
                animationSpec = tween(durationMillis = 350),
                label = "AnimatedScientificRowsInnerWeight"
            )

            val commonButtonsInnerWeight by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0.70f else 1f,
                animationSpec = tween(durationMillis = 350),
                label = "CommonButtonsInnerWeight"
            )

            if (viewModel.isScientificMode || animatedScientificRowsInnerWeight > 0.001f) {
                AnimatedVisibility(
                    visible = viewModel.isScientificMode,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                    modifier = Modifier
                        .weight(
                            animatedScientificRowsInnerWeight.coerceAtLeast(0.001f)
                        )
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        ScientificButtonsRow2(viewModel, modifier = Modifier.weight(1f))
                        Spacer(Modifier.height(MediumSpacing))
                        ScientificButtonsRow3(
                            viewModel, viewModel.isInverseMode, modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.height(LargeSpacing))
                    }
                }
            }

            if (commonButtonsInnerWeight > 0.001f) {
                Column(
                    modifier = Modifier
                        .weight(commonButtonsInnerWeight.coerceAtLeast(0.001f))
                        .fillMaxHeight()
                ) {
                    val buttonRows = listOf(
                        listOf("AC", "( )", "%", "÷"),
                        listOf("7", "8", "9", "×"),
                        listOf("4", "5", "6", "-"),
                        listOf("1", "2", "3", "+"),
                        listOf(".", "0", "⌫", "=")
                    )

                    buttonRows.forEach { rowData ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = NoPadding),
                            horizontalArrangement = Arrangement.spacedBy(LargerSpacing)
                        ) {
                            rowData.forEach { buttonText ->
                                val isNumberButton = buttonText in listOf(
                                    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."
                                )
                                CalculatorButton(
                                    text = buttonText,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    isOperator = buttonText in listOf(
                                        "÷", "×", "-", "+", "%", "( )"
                                    ),
                                    isSpecial = buttonText == "=",
                                    isClear = buttonText == "AC",
                                    isScientificButton = false,
                                    isNumber = isNumberButton || buttonText == "⌫",
                                    isGlobalScientificModeActive = viewModel.isScientificMode,
                                    fontFamily = if (buttonText == "⌫") firaSansFamily else null,
                                    onClick = {
                                        if (buttonText == "( )") {
                                            viewModel.onParenthesesClick()
                                        } else {
                                            viewModel.onButtonClick(buttonText)
                                        }
                                    },
                                    onLongClick = if (buttonText == "⌫") {
                                        { viewModel.onButtonClick("AC") }
                                    } else null)
                            }
                        }
                        if (rowData != buttonRows.last()) {
                            val spacerInnerHeight by animateDpAsState(
                                targetValue = if (viewModel.isScientificMode) MediumSpacing else LargeSpacing,
                                animationSpec = tween(durationMillis = 300),
                                label = "SpacerInnerHeightAnimation"
                            )
                            Spacer(Modifier.height(spacerInnerHeight))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScientificButtonsRow1(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = SmallestPadding),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        val firstButtonText = if (viewModel.isInverseMode) "x²" else "√"
        val scientificButtons1 = listOf(firstButtonText, "π", "^")


        scientificButtons1.forEach { text ->
            CalculatorButton(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isOperator = true,
                isScientificButton = true,
                isNumber = false,
                isGlobalScientificModeActive = viewModel.isScientificMode,
                onClick = { viewModel.onButtonClick(text) })
        }
        CalculatorButton(
            text = "!",
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            isOperator = true,
            isScientificButton = true,
            isNumber = false,
            isGlobalScientificModeActive = viewModel.isScientificMode,
            onClick = { viewModel.onButtonClick("!") })

        IconButton(
            onClick = { viewModel.toggleScientificMode() },
            modifier = Modifier
                .height(ButtonHeight)
                .width(ButtonWidth)
                .clip(RoundedCornerShape(100f)),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.2f),
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            val rotationAngle by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0f else 180f,
                animationSpec = tween(durationMillis = 400),
                label = "IconRotation"
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Toggle Scientific Panel",
                modifier = Modifier.rotate(rotationAngle)
            )
        }
    }
}


@Composable
fun ScientificButtonsRow2(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SmallestPadding),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalculatorButton(
            text = viewModel.angleUnit.name,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            isOperator = true,
            isScientificButton = true,
            isNumber = false,
            isGlobalScientificModeActive = viewModel.isScientificMode,
            onClick = { viewModel.toggleAngleUnit() })

        val trigButtons = listOf("sin", "cos", "tan")
        trigButtons.forEach { text ->
            val buttonDisplayText = if (viewModel.isInverseMode) "$text⁻¹" else text
            CalculatorButton(
                text = buttonDisplayText,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isOperator = true,
                isScientificButton = true,
                isNumber = false,
                isGlobalScientificModeActive = viewModel.isScientificMode,
                onClick = { viewModel.onButtonClick(text) })
        }
        Spacer(Modifier.width(ButtonWidth))
    }
}

@Composable
fun ScientificButtonsRow3(
    viewModel: CalculatorViewModel,
    isInverseMode: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SmallestPadding),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val lnButtonText = if (isInverseMode) "eˣ" else "ln"
        val logButtonText = if (isInverseMode) "10ˣ" else "log"

        val scientificButtons3Order = listOf("INV", "e", lnButtonText, logButtonText)

        scientificButtons3Order.forEach { text ->
            CalculatorButton(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isOperator = true,
                isScientificButton = true,
                isNumber = false,
                isGlobalScientificModeActive = viewModel.isScientificMode,
                isInverseActive = text == "INV" && isInverseMode,
                onClick = {
                    if (text == "INV") {
                        viewModel.toggleInverseMode()
                    } else {
                        val textToSend = when (text) {
                            "eˣ" -> "ln"
                            "10ˣ" -> "log"
                            else -> text
                        }
                        viewModel.onButtonClick(textToSend)
                    }
                })
        }
        Spacer(Modifier.width(ButtonWidth))
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Portrait - Common Mode")
@Composable
fun PreviewPortraitCommon() {
    CalculatorTheme(darkTheme = false) {
        Surface {
            val sampleViewModel = CalculatorViewModel()
            CompactButtonLayout(viewModel = sampleViewModel)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Portrait - Scientific Mode")
@Composable
fun PreviewPortraitScientific() {
    CalculatorTheme(darkTheme = false) {
        Surface {
            val sampleViewModel = CalculatorViewModel()
            sampleViewModel.toggleScientificMode()
            CompactButtonLayout(viewModel = sampleViewModel)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Landscape - Common Mode", widthDp = 800, heightDp = 360)
@Composable
fun PreviewLandscapeCommon() {
    CalculatorTheme(darkTheme = false) {
        Surface {
            val sampleViewModel = CalculatorViewModel()
            CompactButtonLayout(viewModel = sampleViewModel)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Landscape - Scientific Mode", widthDp = 800, heightDp = 360)
@Composable
fun PreviewLandscapeScientific() {
    CalculatorTheme(darkTheme = false) {
        Surface {
            val sampleViewModel = CalculatorViewModel()
            sampleViewModel.toggleScientificMode()
            CompactButtonLayout(viewModel = sampleViewModel)
        }
    }
}

@Preview(showBackground = true, name = "Num Btn (Port, SciOff)")
@Composable
fun NumBtnPortSciOff() {
    CalculatorTheme {
        Surface {
            CalculatorButton(
                text = "7",
                isScientificButton = false,
                isNumber = true,
                isGlobalScientificModeActive = false,
                onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "Num Btn (Port, SciOn)")
@Composable
fun NumBtnPortSciOn() {
    CalculatorTheme {
        Surface {
            CalculatorButton(
                text = "7",
                isScientificButton = false,
                isNumber = true,
                isGlobalScientificModeActive = true,
                onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "Num Btn (Land, SciOff)", widthDp = 100, heightDp = 60)
@Composable
fun NumBtnLandSciOff() {
    CalculatorTheme {
        Surface {
            CalculatorButton(
                text = "7",
                isScientificButton = false,
                isNumber = true,
                isGlobalScientificModeActive = false,
                onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "Num Btn (Land, SciOn)", widthDp = 100, heightDp = 60)
@Composable
fun NumBtnLandSciOn() {
    CalculatorTheme {
        Surface {
            CalculatorButton(
                text = "7",
                isScientificButton = false,
                isNumber = true,
                isGlobalScientificModeActive = true,
                onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "Sci Panel Btn (Port)")
@Composable
fun SciPanelBtnPort() {
    CalculatorTheme {
        Surface {
            CalculatorButton(
                text = "sin",
                isScientificButton = true,
                isNumber = false,
                isGlobalScientificModeActive = true,
                isOperator = true,
                onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "Sci Panel Btn (Land)", widthDp = 100, heightDp = 60)
@Composable
fun SciPanelBtnLand() {
    CalculatorTheme {
        Surface {
            CalculatorButton(
                text = "sin",
                isScientificButton = true,
                isNumber = false,
                isGlobalScientificModeActive = true,
                isOperator = true,
                onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "Sci Panel Btn Long (Land)", widthDp = 100, heightDp = 60)
@Composable
fun SciPanelBtnLongLand() {
    CalculatorTheme {
        Surface {
            CalculatorButton(
                text = "sin⁻¹",
                isScientificButton = true,
                isNumber = false,
                isGlobalScientificModeActive = true,
                isOperator = true,
                onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "Operator Btn (Port, SciOff)")
@Composable
fun OperatorBtnPortSciOff() {
    CalculatorTheme {
        Surface {
            CalculatorButton(
                text = "+",
                isScientificButton = false,
                isNumber = false,
                isGlobalScientificModeActive = false,
                isOperator = true,
                onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "Operator Btn (Port, SciOn)")
@Composable
fun OperatorBtnPortSciOn() {
    CalculatorTheme {
        Surface {
            CalculatorButton(
                text = "+",
                isScientificButton = false,
                isNumber = false,
                isGlobalScientificModeActive = true,
                isOperator = true,
                onClick = {})
        }
    }
}

// Preview for the backspace button with Fira Sans
@Preview(showBackground = true, name = "Backspace Button with Fira Sans")
@Composable
fun BackspaceButtonPreview() {
    CalculatorTheme {
        Surface {
            CalculatorButton(
                text = "⌫",
                isScientificButton = false,
                isNumber = true,
                isGlobalScientificModeActive = false,
                fontFamily = firaSansFamily,
                onClick = {})
        }
    }
}