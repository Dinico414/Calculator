package com.xenonware.calculator.ui.layouts.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xenon.mylibrary.values.CompactWideButtonHeight
import com.xenon.mylibrary.values.CompactWideButtonWidth
import com.xenon.mylibrary.values.LargePadding
import com.xenon.mylibrary.values.LargeSpacing
import com.xenon.mylibrary.values.LargeTextFieldPadding
import com.xenon.mylibrary.values.LargerPadding
import com.xenon.mylibrary.values.LargerSpacing
import com.xenon.mylibrary.values.LargestSpacing
import com.xenon.mylibrary.values.MediumSpacing
import com.xenon.mylibrary.values.SmallButtonSizeSpacing
import com.xenon.mylibrary.values.SmallestPadding
import com.xenonware.calculator.ui.res.CalculatorButton
import com.xenonware.calculator.ui.res.buttons.firaSansFamily
import com.xenonware.calculator.viewmodel.CalculatorViewModel

@Composable
fun CompactCalculator(
    viewModel: CalculatorViewModel,
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit,
    onOpenConverter: () -> Unit,
    appSize: IntSize,
) {
    if (isLandscape) {
        CompactLandscapeScreen(viewModel = viewModel, modifier = modifier)
    } else {
        CompactPortraitScreen(viewModel = viewModel, modifier = modifier)
    }
}

// ====================== PORTRAIT ======================

@Composable
private fun CompactPortraitScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        CompactPortraitDisplay(
            currentInput = viewModel.currentInput,
            result = viewModel.result,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        CompactPortraitButtonGrid(viewModel = viewModel)
    }
}

@Composable
private fun CompactPortraitDisplay(
    currentInput: String,
    result: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = LargeTextFieldPadding)
            .padding(horizontal = LargerPadding),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Current input (above result, smaller, lighter)
        if (currentInput.isNotEmpty()) {
            Text(
                text = currentInput,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Light,
                    lineHeight = 1.em
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                textAlign = TextAlign.End,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        // Main result — large and bold
        Text(
            text = if (result.isEmpty()) "0" else result,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 64.sp,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CompactPortraitButtonGrid(viewModel: CalculatorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.7f)
            .padding(LargePadding)
    ) {
        ScientificButtonsRow1(viewModel = viewModel)

        val spacerHeight by animateDpAsState(
            targetValue = if (viewModel.isScientificMode) MediumSpacing else LargestSpacing,
            animationSpec = tween(300)
        )
        Spacer(Modifier.height(spacerHeight))

        Column(
            modifier = Modifier
                .weight(1f)
                .animateContentSize(tween(300))
        ) {
            val sciWeight by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0.2f else 0f,
                animationSpec = tween(350)
            )
            val commonWeight by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0.70f else 1f,
                animationSpec = tween(350)
            )

            if (viewModel.isScientificMode || sciWeight > 0.001f) {
                AnimatedVisibility(
                    visible = viewModel.isScientificMode,
                    enter = fadeIn(tween(300)),
                    exit = fadeOut(tween(300)),
                    modifier = Modifier.weight(sciWeight.coerceAtLeast(0.001f))
                ) {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        ScientificButtonsRow2(viewModel = viewModel, modifier = Modifier.weight(1f))
                        Spacer(Modifier.height(MediumSpacing))
                        ScientificButtonsRow3(
                            viewModel = viewModel,
                            isInverseMode = viewModel.isInverseMode,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.height(LargeSpacing))
                    }
                }
            }

            if (commonWeight > 0.001f) {
                Column(
                    modifier = Modifier
                        .weight(commonWeight.coerceAtLeast(0.001f))
                        .fillMaxHeight()
                ) {
                    val rows = listOf(
                        listOf("AC", "( )", "%", "÷"),
                        listOf("7", "8", "9", "×"),
                        listOf("4", "5", "6", "-"),
                        listOf("1", "2", "3", "+"),
                        listOf(".", "0", "⌫", "=")
                    )

                    rows.forEachIndexed { index, row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(LargerSpacing)
                        ) {
                            row.forEach { text ->
                                val isNumber = text in listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "⌫")
                                CalculatorButton(
                                    text = text,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    isOperator = text in listOf("÷", "×", "-", "+", "%", "( )"),
                                    isSpecial = text == "=",
                                    isClear = text == "AC",
                                    isScientificButton = false,
                                    isNumber = isNumber,
                                    isGlobalScientificModeActive = viewModel.isScientificMode,
                                    fontFamily = if (text == "⌫") firaSansFamily else null,
                                    onClick = {
                                        if (text == "( )") viewModel.onParenthesesClick()
                                        else viewModel.onButtonClick(text)
                                    },
                                    onLongClick = if (text == "⌫") { { viewModel.onButtonClick("AC") } } else null
                                )
                            }
                        }

                        if (index < rows.lastIndex) {
                            val innerSpacer by animateDpAsState(
                                targetValue = if (viewModel.isScientificMode) MediumSpacing else LargeSpacing,
                                animationSpec = tween(300)
                            )
                            Spacer(Modifier.height(innerSpacer))
                        }
                    }
                }
            }
        }
    }
}

// ====================== LANDSCAPE ======================

@Composable
private fun CompactLandscapeScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        CompactLandscapeDisplay(
            currentInput = viewModel.currentInput,
            result = viewModel.result,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        CompactLandscapeButtonGrid(viewModel = viewModel)
    }
}

@Composable
private fun CompactLandscapeDisplay(
    currentInput: String,
    result: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = LargerPadding)
            .padding(end = LargeTextFieldPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentInput,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Light
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
            textAlign = TextAlign.Start,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = LargePadding)
        )

        Text(
            text = result.ifEmpty { "0" },
            style = MaterialTheme.typography.displayMedium.copy(
                fontSize = 48.sp,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = LargePadding)
        )
    }
}

@Composable
private fun CompactLandscapeButtonGrid(viewModel: CalculatorViewModel) {
    val spacing = MediumSpacing

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(LargePadding),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        // Scientific column
        Column(
            modifier = Modifier.weight(3f).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            listOf(
                listOf("√", "π", "^", "!"),
                listOf(viewModel.angleUnit.name, "sin", "cos", "tan"),
                listOf("INV", "e", "ln", "log")
            ).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    row.forEach { originalText ->
                        val displayText = when {
                            originalText == "√" && viewModel.isInverseMode -> "x²"
                            originalText == "sin" && viewModel.isInverseMode -> "sin⁻¹"
                            originalText == "cos" && viewModel.isInverseMode -> "cos⁻¹"
                            originalText == "tan" && viewModel.isInverseMode -> "tan⁻¹"
                            originalText == "ln" && viewModel.isInverseMode -> "eˣ"
                            originalText == "log" && viewModel.isInverseMode -> "10ˣ"
                            else -> originalText
                        }

                        CalculatorButton(
                            text = displayText,
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            isOperator = true,
                            isScientificButton = true,
                            isNumber = false,
                            isGlobalScientificModeActive = viewModel.isScientificMode,
                            isInverseActive = originalText == "INV" && viewModel.isInverseMode,
                            onClick = {
                                when (originalText) {
                                    "INV" -> viewModel.toggleInverseMode()
                                    viewModel.angleUnit.name -> viewModel.toggleAngleUnit()
                                    else -> viewModel.onButtonClick(originalText)
                                }
                            }
                        )
                    }
                }
            }
        }

        // Numeric + operators column
        Column(
            modifier = Modifier.weight(5f).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            listOf(
                listOf("7", "8", "9", "÷", "AC"),
                listOf("4", "5", "6", "×", "( )"),
                listOf("1", "2", "3", "-", "%"),
                listOf(".", "0", "⌫", "+", "=")
            ).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    row.forEach { text ->
                        CalculatorButton(
                            text = text,
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            isOperator = text in listOf("÷", "×", "-", "+", "%", "( )"),
                            isSpecial = text == "=",
                            isClear = text == "AC",
                            isScientificButton = false,
                            isNumber = text in listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "⌫"),
                            isGlobalScientificModeActive = viewModel.isScientificMode,
                            fontFamily = if (text == "⌫") firaSansFamily else null,
                            onClick = {
                                if (text == "( )") viewModel.onParenthesesClick()
                                else viewModel.onButtonClick(text)
                            },
                            onLongClick = if (text == "⌫") { { viewModel.onButtonClick("AC") } } else null
                        )
                    }
                }
            }
        }
    }
}

// ====================== SCIENTIFIC ROWS ======================

@Composable
private fun ScientificButtonsRow1(viewModel: CalculatorViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(SmallButtonSizeSpacing)
            .padding(horizontal = SmallestPadding),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val sqrtText = if (viewModel.isInverseMode) "x²" else "√"
        listOf(sqrtText, "π", "^", "!").forEach { text ->
            CalculatorButton(
                text = text,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                isOperator = true,
                isScientificButton = true,
                isNumber = false,
                isGlobalScientificModeActive = viewModel.isScientificMode,
                onClick = { viewModel.onButtonClick(text) }
            )
        }

        val interactionSource = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .size(CompactWideButtonWidth, CompactWideButtonHeight)
                .clip(RoundedCornerShape(100))
                .background(MaterialTheme.colorScheme.inversePrimary)
                .clickable(
                    onClick = { viewModel.toggleScientificMode() },
                    interactionSource = interactionSource,
                    indication = LocalIndication.current
                ),
            contentAlignment = Alignment.Center
        ) {
            val rotation by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0f else 180f,
                animationSpec = tween(400)
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Toggle Scientific Panel",
                modifier = Modifier.rotate(rotation),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ScientificButtonsRow2(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SmallestPadding),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalculatorButton(
            text = viewModel.angleUnit.name,
            modifier = Modifier.weight(1f).fillMaxHeight(),
            isOperator = true,
            isScientificButton = true,
            isNumber = false,
            isGlobalScientificModeActive = viewModel.isScientificMode,
            onClick = { viewModel.toggleAngleUnit() }
        )

        listOf("sin", "cos", "tan").forEach { fn ->
            val displayText = if (viewModel.isInverseMode) "$fn⁻¹" else fn
            CalculatorButton(
                text = displayText,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                isOperator = true,
                isScientificButton = true,
                isNumber = false,
                isGlobalScientificModeActive = viewModel.isScientificMode,
                onClick = { viewModel.onButtonClick(fn) }
            )
        }

        Spacer(Modifier.width(CompactWideButtonWidth))
    }
}

@Composable
private fun ScientificButtonsRow3(
    viewModel: CalculatorViewModel,
    isInverseMode: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SmallestPadding),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val lnText = if (isInverseMode) "eˣ" else "ln"
        val logText = if (isInverseMode) "10ˣ" else "log"

        listOf("INV", "e", lnText, logText).forEach { text ->
            CalculatorButton(
                text = text,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                isOperator = true,
                isScientificButton = true,
                isNumber = false,
                isGlobalScientificModeActive = viewModel.isScientificMode,
                isInverseActive = text == "INV" && isInverseMode,
                onClick = {
                    if (text == "INV") {
                        viewModel.toggleInverseMode()
                    } else {
                        val actualFn = when (text) {
                            "eˣ" -> "ln"
                            "10ˣ" -> "log"
                            else -> text
                        }
                        viewModel.onButtonClick(actualFn)
                    }
                }
            )
        }

        Spacer(Modifier.width(CompactWideButtonWidth))
    }
}