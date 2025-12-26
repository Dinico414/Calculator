package com.xenonware.calculator.ui.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.xenon.mylibrary.theme.QuicksandTitleVariable
import com.xenon.mylibrary.values.CompactWideButtonHeight
import com.xenon.mylibrary.values.CompactWideButtonWidth
import com.xenon.mylibrary.values.LargePadding
import com.xenon.mylibrary.values.LargeSpacing
import com.xenon.mylibrary.values.LargerSpacing
import com.xenon.mylibrary.values.LargestSpacing
import com.xenon.mylibrary.values.MediumSpacing
import com.xenon.mylibrary.values.NoPadding
import com.xenon.mylibrary.values.SmallestPadding
import com.xenonware.calculator.R
import com.xenonware.calculator.ui.res.CalculatorButton
import com.xenonware.calculator.viewmodel.CalculatorViewModel
import com.xenonware.calculator.viewmodel.LayoutType

val firaSansFamily = FontFamily(
    Font(R.font.fira_sans, FontWeight.Normal)
)

val ButtonWidth = CompactWideButtonWidth
val ButtonHeight = CompactWideButtonHeight

@Composable
fun ButtonLayout(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel,
    layoutType: LayoutType,
    isHistoryMode: Boolean = false,
    isLandscape: Boolean
) {
    // ===================================================================
    // LAYOUT TYPE CLARITY
    // ===================================================================
    // LayoutType.COVER      → Small/cover screen (e.g., folded phone cover display)
    // LayoutType.COMPACT    → Normal phone portrait
    // LayoutType.MEDIUM     → Larger phone or small tablet
    // LayoutType.EXPANDED   → Tablet or desktop window
    //
    // "Compact and up" means COMPACT, MEDIUM, or EXPANDED → has more space
    // ===================================================================

    val isCompactOrLarger = layoutType in listOf(
        LayoutType.COMPACT,
        LayoutType.MEDIUM,
        LayoutType.EXPANDED
    )

    val isCoverScreen = layoutType == LayoutType.COVER
    val isSmallScreenPortrait = isCoverScreen && !isLandscape

    // Common spacing used across layouts
    val buttonSpacing = MediumSpacing

    // ===================================================================
    // MAIN LAYOUT DECISION
    // ===================================================================

    if (isCompactOrLarger && !isLandscape) {
        // → COMPACT AND UP IN PORTRAIT
        // Special portrait layout with top scientific row + toggleable panel
        CompactPortraitLayout(
            viewModel = viewModel,
            modifier = modifier,
            isHistoryMode = isHistoryMode
        )
    } else {
        // → ALL OTHER CASES:
        //   • Cover screen (portrait or landscape)
        //   • Any layout in landscape (including compact+)
        //
        // Uses a horizontal Row layout:
        //   - Optional scientific column on the left (only in landscape on compact+)
        //   - Main numeric + operators column on the right
        val showScientificColumn = isCompactOrLarger && isLandscape

        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = LargePadding,
                    start = if (isSmallScreenPortrait) NoPadding else LargePadding,
                    end = if (isSmallScreenPortrait) NoPadding else LargePadding,
                    bottom = if (isSmallScreenPortrait) NoPadding else LargePadding
                )
                .let {
                    // Cover screen gets full black background (typical for foldable cover)
                    if (isSmallScreenPortrait) it.background(Color.Black) else it
                },
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            // Scientific column — only shown in landscape on compact or larger devices
            if (showScientificColumn) {
                ScientificColumn(
                    viewModel = viewModel,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight(),
                    buttonSpacing = buttonSpacing,
                    isHistoryMode = isHistoryMode,
                )
            }

            NumericOperatorsColumn(
                viewModel = viewModel,
                modifier = Modifier
                    .weight(if (showScientificColumn) 5f else 1f)
                    .fillMaxHeight(),
                buttonSpacing = buttonSpacing,
                isHistoryMode = isHistoryMode,
            )
        }
    }
}

@Composable
private fun CompactPortraitLayout(
    viewModel: CalculatorViewModel, modifier: Modifier = Modifier, isHistoryMode: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxHeight(0.7f)
            .padding(LargePadding)
    ) {
        ScientificButtonsRow1(
            viewModel = viewModel,
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth(),
            isInverseMode = viewModel.isInverseMode,
            isHistoryMode = isHistoryMode,
        )

        val spacerHeight by animateDpAsState(
            targetValue = if (viewModel.isScientificMode) MediumSpacing else LargestSpacing,
            animationSpec = tween(300),
            label = ""
        )
        Spacer(Modifier.height(spacerHeight))

        Column(
            modifier = Modifier
                .weight(1f)
                .animateContentSize(tween(300))
        ) {
            val sciWeight by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0.2f else 0f,
                animationSpec = tween(350),
                label = ""
            )
            val commonWeight by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0.70f else 1f,
                animationSpec = tween(350),
                label = ""
            )

            if (viewModel.isScientificMode || sciWeight > 0.001f) {
                AnimatedVisibility(
                    visible = viewModel.isScientificMode,
                    enter = fadeIn(tween(300)),
                    exit = fadeOut(tween(300)),
                    modifier = Modifier
                        .weight(sciWeight.coerceAtLeast(0.001f))
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        ScientificButtonsRow2(
                            viewModel = viewModel,
                            modifier = Modifier.weight(1f),
                            isHistoryMode = isHistoryMode,)

                        Spacer(Modifier.height(MediumSpacing))

                        ScientificButtonsRow3(
                            viewModel = viewModel,
                            isInverseMode = viewModel.isInverseMode,
                            modifier = Modifier.weight(1f),
                                    isHistoryMode = isHistoryMode,
                        )
                        Spacer(Modifier.height(LargeSpacing))
                    }
                }
            }

            if (commonWeight > 0.001f) {
                CommonButtonGrid(
                    viewModel = viewModel,
                    modifier = Modifier
                        .weight(commonWeight.coerceAtLeast(0.001f))
                        .fillMaxHeight(),
                    isScientificMode = viewModel.isScientificMode,
                    isHistoryMode = isHistoryMode,
                )
            }
        }
    }
}

@Composable
private fun ScientificColumn(
    viewModel: CalculatorViewModel, modifier: Modifier = Modifier, buttonSpacing: Dp, isHistoryMode: Boolean = false
) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(buttonSpacing)
    ) {
        val rows = listOf(
            listOf("√", "π", "^", "!"),
            listOf(viewModel.angleUnit.name, "sin", "cos", "tan"),
            listOf("INV", "e", "ln", "log")
        )

        rows.forEach { rowData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                rowData.forEach { originalText ->
                    val displayText = when (originalText) {
                        "INV" -> "INV"
                        viewModel.angleUnit.name -> viewModel.angleUnit.name
                        "√" if viewModel.isInverseMode -> "x²"
                        "sin" if viewModel.isInverseMode -> "sin⁻¹"
                        "cos" if viewModel.isInverseMode -> "cos⁻¹"
                        "tan" if viewModel.isInverseMode -> "tan⁻¹"
                        "ln" if viewModel.isInverseMode -> "eˣ"
                        "log" if viewModel.isInverseMode -> "10ˣ"
                        else -> originalText
                    }

                    val isInv = originalText == "INV"
                    val isAngle = originalText == viewModel.angleUnit.name

                    CalculatorButton(
                        text = displayText,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        isOperator = true,
                        isScientificButton = true,
                        isNumber = false,
                        isGlobalScientificModeActive = true,
                        isInverseActive = isInv && viewModel.isInverseMode,
                        isHistoryMode = isHistoryMode,
                        onClick = {
                            when {
                                isInv -> viewModel.toggleInverseMode()
                                isAngle -> viewModel.toggleAngleUnit()
                                else -> viewModel.onButtonClick(originalText)
                            }
                        })
                }
            }
        }
    }
}

@Composable
private fun NumericOperatorsColumn(
    viewModel: CalculatorViewModel, modifier: Modifier = Modifier, buttonSpacing: Dp, isHistoryMode: Boolean = false,
) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(buttonSpacing)
    ) {
        val rows = listOf(
            listOf("7", "8", "9", "÷", "AC"),
            listOf("4", "5", "6", "×", "( )"),
            listOf("1", "2", "3", "-", "%"),
            listOf(".", "0", "⌫", "+", "=")
        )

        rows.forEach { rowData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                rowData.forEach { text ->
                    val isOperator = text in listOf("÷", "×", "-", "+", "%", "( )")
                    val isClear = text == "AC"
                    val isSpecial = text == "="
                    val isNumber =
                        text in listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "⌫")

                    CalculatorButton(
                        text = text,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        isOperator = isOperator,
                        isSpecial = isSpecial,
                        isClear = isClear,
                        isScientificButton = false,
                        isNumber = isNumber,
                        isGlobalScientificModeActive = viewModel.isScientificMode,
                        isHistoryMode = isHistoryMode,
                        fontWeight = if (text == "⌫") FontWeight.SemiBold else FontWeight.Normal,
                        fontFamily = if (text == "⌫") firaSansFamily else QuicksandTitleVariable,
                        onClick = {
                            if (text == "( )") viewModel.onParenthesesClick()
                            else viewModel.onButtonClick(text)
                        },
                        onLongClick = if (text == "⌫") {
                            { viewModel.onButtonClick("AC") }
                        } else null)
                }
            }
        }
    }
}

@Composable
private fun CommonButtonGrid(
    viewModel: CalculatorViewModel, modifier: Modifier = Modifier, isScientificMode: Boolean, isHistoryMode: Boolean = false
) {
    Column(modifier = modifier) {
        val rows = listOf(
            listOf("AC", "( )", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf(".", "0", "⌫", "=")
        )

        rows.forEachIndexed { index, rowData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = NoPadding),
                horizontalArrangement = Arrangement.spacedBy(LargerSpacing)
            ) {
                rowData.forEach { text ->
                    val isNumber =
                        text in listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".")
                    CalculatorButton(
                        text = text,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        isOperator = text in listOf("÷", "×", "-", "+", "%", "( )"),
                        isSpecial = text == "=",
                        isClear = text == "AC",
                        isScientificButton = false,
                        isHistoryMode = isHistoryMode,
                        isNumber = isNumber || text == "⌫",
                        isGlobalScientificModeActive = isScientificMode,
                        fontWeight = if (text == "⌫") FontWeight.SemiBold else FontWeight.Normal,
                        fontFamily = if (text == "⌫") firaSansFamily else QuicksandTitleVariable,
                        onClick = {
                            if (text == "( )") viewModel.onParenthesesClick()
                            else viewModel.onButtonClick(text)
                        },
                        onLongClick = if (text == "⌫") {
                            { viewModel.onButtonClick("AC") }
                        } else null)
                }
            }

            if (index < rows.lastIndex) {
                val spacerHeight by animateDpAsState(
                    targetValue = if (isScientificMode) MediumSpacing else LargeSpacing,
                    animationSpec = tween(300),
                    label = ""
                )
                Spacer(Modifier.height(spacerHeight))
            }
        }
    }
}


@Composable
fun ScientificButtonsRow1(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier,
    isInverseMode: Boolean,
    isHistoryMode: Boolean = false
) {
    Row(
        modifier = modifier.padding(horizontal = SmallestPadding),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing),
        verticalAlignment = Alignment.CenterVertically,
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
                isHistoryMode = isHistoryMode,
                isScientificButton = true,
                isNumber = false,
                isGlobalScientificModeActive = viewModel.isScientificMode,
                onClick = { viewModel.onButtonClick(text) })
        }

        val interactionSource = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .height(ButtonHeight)
                .width(ButtonWidth)
                .clip(RoundedCornerShape(100f))
                .background(MaterialTheme.colorScheme.inversePrimary)
                .clickable(
                    onClick = {
                        viewModel.toggleScientificMode()
                        if (isInverseMode) viewModel.toggleInverseMode()
                    },
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                ), contentAlignment = Alignment.Center
        ) {
            val rotationAngle by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0f else 180f, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow
                ), label = "IconRotation"
            )
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowUp,
                contentDescription = "Toggle Scientific Panel",
                modifier = Modifier.rotate(rotationAngle),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ScientificButtonsRow2(viewModel: CalculatorViewModel, modifier: Modifier = Modifier, isHistoryMode: Boolean = false) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SmallestPadding),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
              val trigButtons = listOf(viewModel.angleUnit.name, "sin", "cos", "tan")
        trigButtons.forEach { text ->
            val buttonDisplayText = when {
                text == viewModel.angleUnit.name -> text
                viewModel.isInverseMode -> "$text⁻¹"
                else -> text
            }
            CalculatorButton(
                text = buttonDisplayText,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isOperator = true,
                isScientificButton = true,
                isNumber = false,
                isHistoryMode = isHistoryMode,
                isGlobalScientificModeActive = viewModel.isScientificMode,
                onClick = {
                    if (text == viewModel.angleUnit.name) {
                        viewModel.toggleAngleUnit()
                    } else {
                        viewModel.onButtonClick(text)
                    }
                })
        }
        Spacer(Modifier.width(ButtonWidth))
    }
}

@Composable
fun ScientificButtonsRow3(
    viewModel: CalculatorViewModel,
    isInverseMode: Boolean,
    modifier: Modifier = Modifier,
    isHistoryMode: Boolean = false
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
                isHistoryMode = isHistoryMode,
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