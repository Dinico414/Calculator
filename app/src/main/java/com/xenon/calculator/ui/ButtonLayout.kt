package com.xenon.calculator.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonLayout(
    viewModel: CalculatorViewModel,
    isTablet: Boolean, // Placeholder for future responsiveness
    isLandscape: Boolean, // Placeholder for future responsiveness
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), // Slightly different background
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp)
    ) {
        // Toggle button for Scientific Panel (optional placement)
        // This button could also be part of a top bar or settings menu
        TextButton(
            onClick = { viewModel.toggleScientificMode() },
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
        ) {
            Text(if (viewModel.isScientificMode) "Hide Scientific" else "Show Scientific")
            Spacer(Modifier.width(4.dp))
            Icon(
                if (viewModel.isScientificMode) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                contentDescription = "Toggle Scientific Panel"
            )
        }

        AnimatedVisibility(
            visible = viewModel.isScientificMode,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            Column {
                ScientificButtonsRow1(viewModel)
                Spacer(Modifier.height(4.dp))
                ScientificButtonsRow2(viewModel)
                Spacer(Modifier.height(8.dp)) // Spacing before common buttons
            }
        }

        // Common buttons
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
                    .padding(horizontal = 1.dp), // Minimal horizontal padding for rows
                horizontalArrangement = Arrangement.spacedBy(4.dp) // Spacing between buttons in a row
            ) {
                rowData.forEach { buttonText ->
                    val weight = if (buttonText == "0" && rowData.size == 4) 2.1f else 1f // Make 0 button wider if it's in a 4-button row context
                    CalculatorButton(
                        text = buttonText,
                        modifier = Modifier.weight(weight),
                        isOperator = buttonText in listOf("÷", "×", "-", "+", "%", "( )"), // Mark operators
                        isSpecial = buttonText == "=",
                        isClear = buttonText == "AC",
                        onClick = {
                            if (buttonText == "( )") {
                                viewModel.onParenthesesClick()
                            } else {
                                viewModel.onButtonClick(buttonText)
                            }
                        }
                    )
                }
            }
            if (rowData != buttonRows.last()) { // Add spacer unless it's the last row
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun ScientificButtonsRow1(viewModel: CalculatorViewModel) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val scientificButtons1 = listOf("√", "π", "^", "!")
        scientificButtons1.forEach { text ->
            CalculatorButton(text, Modifier.weight(1f), isOperator = true) { viewModel.onButtonClick(text) }
        }
        // Angle Unit Toggle Button
        CalculatorButton(
            text = viewModel.angleUnit.name,
            modifier = Modifier.weight(1f), // Give it same weight as other buttons
            isOperator = true // Style as operator
        ) {
            viewModel.toggleAngleUnit()
        }
    }
}

@Composable
fun ScientificButtonsRow2(viewModel: CalculatorViewModel) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val scientificButtons2 = listOf("INV", "sin", "cos", "tan")
        val trigButtons = listOf("sin", "cos", "tan")
        scientificButtons2.forEach { text ->
            val buttonDisplaytext = if (viewModel.isInverseMode && text in trigButtons) "a$text" else text
            CalculatorButton(
                text = buttonDisplaytext, // Show "asin" if inverse mode and button is "sin"
                modifier = Modifier.weight(1f),
                isOperator = true
            ) {
                viewModel.onButtonClick(text) // Send original function name for logic
            }
        }
        val otherScientificButtons = listOf("e", "ln", "log") // Add if you have more columns/space
        // For a 5-column layout like your XML, you could add one more here:
        CalculatorButton("e", Modifier.weight(1f), isOperator = true) { viewModel.onButtonClick("e") }
        // CalculatorButton("ln", Modifier.weight(1f), isOperator = true) { viewModel.onButtonClick("ln") }
        // CalculatorButton("log", Modifier.weight(1f), isOperator = true) { viewModel.onButtonClick("log") }

    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    isOperator: Boolean = false,
    isSpecial: Boolean = false,
    isClear: Boolean = false,
    onClick: () -> Unit
) {
    val containerColor = when {
        isClear -> MaterialTheme.colorScheme.errorContainer
        isSpecial -> MaterialTheme.colorScheme.primary
        isOperator -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surfaceContainerHighest // Number buttons
    }
    val contentColor = when {
        isClear -> MaterialTheme.colorScheme.onErrorContainer
        isSpecial -> MaterialTheme.colorScheme.onPrimary
        isOperator -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant // Number buttons
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(if (text == "0" && modifier.toString().contains("weight=2")) 1.05f else 1f, matchHeightConstraintsFirst = false) // Adjust aspect ratio slightly for wider 0
            .fillMaxHeight(0.1f) // Let rows control height, buttons fill fraction of available
            .defaultMinSize(minHeight = 48.dp, minWidth = 48.dp), // Ensure minimum touch target
        shape = RoundedCornerShape(16.dp), // More rounded
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp) // Adjust padding
    ) {
        Text(
            text,
            fontSize = if (text.length > 3) 16.sp else 20.sp, // Smaller font for longer text like "asin"
            maxLines = 1,
            overflow = TextOverflow.Ellipsis // Should not be needed with font size check
        )
    }
}
//
//@Preview(showBackground = true, name = "Portrait - Scientific")
//@Composable
//fun ButtonLayoutPreviewPortraitScientific() {
//    // This is where the warning originates for previews
//    @Suppress("viewModel_preview") // Specific suppression if available and works
//    // Or a more general one if the specific one isn't recognized by your lint version
//    // @Suppress("Local निर्माणViewModel") // Example of a more general suppression key (might vary)
//    val previewViewModel = CalculatorViewModel()
//    MaterialTheme {
//        ButtonLayout(previewViewModel, isTablet = false, isLandscape = false)
//    }
//}
//
//@Preview(showBackground = true, name = "Portrait - Non-Scientific")
//@Composable
//fun ButtonLayoutPreviewPortraitNonScientific() {
//    @Suppress("viewModel_preview")
//    val previewViewModel = CalculatorViewModel()
//    previewViewModel.toggleScientificMode()
//    MaterialTheme {
//        ButtonLayout(previewViewModel, isTablet = false, isLandscape = false)
//    }
//}
//
//
//@Preview(showBackground = true, widthDp = 700, heightDp = 380, name = "Landscape - Scientific")
//@Composable
//fun ButtonLayoutPreviewLandscapeScientific() {
//    @Suppress("viewModel_preview")
//    val previewViewModel = CalculatorViewModel()
//    MaterialTheme {
//        ButtonLayout(previewViewModel, isTablet = false, isLandscape = true)
//    }
//}}