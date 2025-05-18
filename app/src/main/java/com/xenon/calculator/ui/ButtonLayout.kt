@file:Suppress("unused", "unused")

package com.xenon.calculator.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ButtonLayout(
    viewModel: CalculatorViewModel,
    isTablet: Boolean,
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight(0.7f)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 4.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { viewModel.toggleScientificMode() },
                modifier = Modifier.matchParentSize(),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                val rotationAngle by animateFloatAsState(
                    targetValue = if (viewModel.isScientificMode) 180f else 0f,
                    animationSpec = tween(durationMillis = 300),
                    label = "IconRotation"
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Toggle Scientific Panel",
                    modifier = Modifier.rotate(rotationAngle)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
        ) {
            val scientificPanelWeight by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0.35f else 0f,
                animationSpec = tween(durationMillis = 300),
                label = "ScientificPanelWeight"
            )

            val commonButtonsWeight by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0.65f else 1f,
                animationSpec = tween(durationMillis = 300),
                label = "CommonButtonsWeight"
            )

            if (viewModel.isScientificMode || scientificPanelWeight > 0.001f) {
                AnimatedVisibility(
                    visible = viewModel.isScientificMode,
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
                    modifier = Modifier
                        .weight(scientificPanelWeight.coerceAtLeast(0.001f))
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        ScientificButtonsRow1(viewModel, modifier = Modifier.weight(1f))
                        Spacer(Modifier.height(4.dp))
                        ScientificButtonsRow2(viewModel, modifier = Modifier.weight(1f))
                        Spacer(Modifier.height(4.dp))
                        ScientificButtonsRow3(viewModel, viewModel.isInverseMode, modifier = Modifier.weight(1f))
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            if (commonButtonsWeight > 0.001f) {
                Column(
                    modifier = Modifier
                        .weight(commonButtonsWeight.coerceAtLeast(0.001f))
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
                                .padding(horizontal = 1.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            rowData.forEach { buttonText ->
                                val buttonWeight = 1f
                                CalculatorButton(
                                    text = buttonText,
                                    modifier = Modifier
                                        .weight(buttonWeight)
                                        .fillMaxHeight(),
                                    isOperator = buttonText in listOf(
                                        "÷", "×", "-", "+", "%", "( )"
                                    ),
                                    isSpecial = buttonText == "=",
                                    isClear = buttonText == "AC",
                                    onClick = {
                                        if (buttonText == "( )") {
                                            viewModel.onParenthesesClick()
                                        } else {
                                            viewModel.onButtonClick(buttonText)
                                        }
                                    })
                            }
                        }
                        if (rowData != buttonRows.last()) {
                            Spacer(Modifier.height(4.dp))
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
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val firstButtonText = if (viewModel.isInverseMode) "x²" else "√"
        val scientificButtons1 = listOf(firstButtonText, "π", "^", "!")

        scientificButtons1.forEach { text ->
            CalculatorButton(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isOperator = true,
                isScientific = true,
                onClick = { viewModel.onButtonClick(text) }
            )
        }
    }
}

@Composable
fun ScientificButtonsRow2(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalculatorButton(
            text = viewModel.angleUnit.name,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            isOperator = true,
            isScientific = true,
            onClick = { viewModel.toggleAngleUnit() }
        )

        val trigButtons = listOf("sin", "cos", "tan")
        trigButtons.forEach { text ->
            val buttonDisplayText =
                if (viewModel.isInverseMode) "$text⁻¹" else text
            CalculatorButton(
                text = buttonDisplayText,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isOperator = true,
                isScientific = true,
                onClick = { viewModel.onButtonClick(text) }
            )
        }
    }
}

@Composable
fun ScientificButtonsRow3(
    viewModel: CalculatorViewModel,
    isInverseMode: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                isScientific = true,
                isInverseActive = text == "INV" && isInverseMode,
                onClick = {
                    if (text == "INV") {
                        viewModel.toggleInverseMode()
                    } else {
                        viewModel.onButtonClick(text)
                    }
                }
            )
        }
    }
}


@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    isOperator: Boolean = false,
    isSpecial: Boolean = false,
    isClear: Boolean = false,
    isScientific: Boolean = false,
    isInverseActive: Boolean = false,
    onClick: () -> Unit,
) {
    val containerColor = when {
        isClear -> MaterialTheme.colorScheme.tertiary
        isSpecial -> MaterialTheme.colorScheme.inversePrimary
        text == "INV" && isScientific -> if (isInverseActive) MaterialTheme.colorScheme.errorContainer else Color.Transparent
        isScientific -> Color.Transparent
        isOperator -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.secondaryContainer
    }

    val contentColor = when {
        isClear -> MaterialTheme.colorScheme.onPrimary
        isSpecial -> MaterialTheme.colorScheme.onSurface
        text == "INV" && isScientific -> if (isInverseActive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        isScientific -> MaterialTheme.colorScheme.onSurfaceVariant
        isOperator -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurface
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cornerRadiusPercent by animateIntAsState(
        targetValue = if (isPressed && !isScientific) 30 else 100,
        animationSpec = tween(durationMillis = if (isScientific) 0 else 400),
        label = "cornerRadiusAnimation"
    )

    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp, minWidth = 40.dp),
        shape = RoundedCornerShape(percent = cornerRadiusPercent),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor, contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
        interactionSource = interactionSource
    ) {
        Text(
            text = text,
            fontSize = if (text.length > 2 || text.contains("⁻¹") || text.contains("ˣ") || text.contains("²")) 18.sp else 22.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}