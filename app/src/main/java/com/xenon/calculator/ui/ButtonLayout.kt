package com.xenon.calculator.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween // If you want to customize animationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // Required for by animateFloatAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextOverflow
// Make sure you have a CalculatorViewModel, if not, you'll need to create a placeholder
// import com.example.yourproject.CalculatorViewModel // Your actual ViewModel import
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Placeholder for CalculatorViewModel - Replace with your actual ViewModel
// class CalculatorViewModel { // Simple placeholder
//     val isScientificMode: Boolean = true
//     fun toggleScientificMode() {}
//     fun onButtonClick(text: String) {}
//     fun onParenthesesClick() {}
//     val angleUnit: AngleUnit = AngleUnit.DEG
//     fun toggleAngleUnit() {}
//     val isInverseMode: Boolean = false
// }
// enum class AngleUnit { DEG, RAD }


@Composable
fun ButtonLayout(
    viewModel: CalculatorViewModel,
    isTablet: Boolean,
    isLandscape: Boolean, // Kept for potential future use
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight(0.75f) // Overall height constraint for the button layout
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp)
    ) {
        TextButton(
            onClick = { viewModel.toggleScientificMode() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        ) {
            Text(if (viewModel.isScientificMode) "Hide Scientific" else "Show Scientific")
            Spacer(Modifier.width(4.dp))
            Icon(
                if (viewModel.isScientificMode) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                contentDescription = "Toggle Scientific Panel"
            )
        }

        Column(
            modifier = Modifier
                .weight(1f) // This Column takes remaining space after TextButton
                .clipToBounds()
                .animateContentSize() // Animates overall size changes of this Column
        ) {
            // Animate the weight factor for the scientific panel
            val scientificPanelWeight by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0.2f else 0f,
                animationSpec = tween(durationMillis = 300), // Adjust duration as needed
                label = "ScientificPanelWeight"
            )

            // Animate the weight factor for the common buttons panel
            val commonButtonsWeight by animateFloatAsState(
                targetValue = if (viewModel.isScientificMode) 0.8f else 1f,
                animationSpec = tween(durationMillis = 300), // Adjust duration as needed
                label = "CommonButtonsWeight"
            )

            // Scientific Buttons Section
            // Conditionally compose AnimatedVisibility only if it's meant to take some space
            // and viewModel.isScientificMode is true to drive the enter/exit animations
            if (viewModel.isScientificMode || scientificPanelWeight > 0.001f) {
                AnimatedVisibility(
                    visible = viewModel.isScientificMode, // Controls enter/exit animations
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
                    modifier = Modifier
                        .weight(scientificPanelWeight.coerceAtLeast(0.001f)) // Use animated weight
                        .fillMaxWidth() // Ensure it takes full width within its weighted space
                ) {
                    // This Column now fills the height allocated by AnimatedVisibility
                    Column(modifier = Modifier.fillMaxHeight()) {
                        ScientificButtonsRow1(viewModel, modifier = Modifier.weight(1f))
                        Spacer(Modifier.height(4.dp))
                        ScientificButtonsRow2(viewModel, modifier = Modifier.weight(1f))
                        Spacer(Modifier.height(8.dp)) // Spacing at the bottom of scientific panel
                    }
                }
            }

            // Common Buttons Section
            // Conditionally compose if it's meant to take some space
            if (commonButtonsWeight > 0.001f) {
                Column(
                    modifier = Modifier
                        .weight(commonButtonsWeight.coerceAtLeast(0.001f)) // Use animated weight
                        .fillMaxHeight() // Ensure it takes full height within its weighted space
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
                                .weight(1f) // Each row takes equal portion of this common buttons Column's height
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
                                    isOperator = buttonText in listOf("÷", "×", "-", "+", "%", "( )"),
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
        modifier = modifier // This modifier from parent Column now includes .weight(1f)
            .fillMaxWidth()
            .padding(horizontal = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val scientificButtons1 = listOf("√", "π", "^", "!")
        scientificButtons1.forEach { text ->
            CalculatorButton(
                text,
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isOperator = true
            ) { viewModel.onButtonClick(text) }
        }
        CalculatorButton(
            text = viewModel.angleUnit.name, // Placeholder for actual AngleUnit
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            isOperator = true
        ) {
            viewModel.toggleAngleUnit()
        }
    }
}

@Composable
fun ScientificButtonsRow2(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier // This modifier from parent Column now includes .weight(1f)
            .fillMaxWidth()
            .padding(horizontal = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val scientificButtons2 = listOf("INV", "sin", "cos", "tan")
        val trigButtons = listOf("sin", "cos", "tan")
        scientificButtons2.forEach { text ->
            val buttonDisplayText = if (viewModel.isInverseMode && text in trigButtons) "a$text" else text
            CalculatorButton(
                text = buttonDisplayText,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isOperator = true
            ) {
                viewModel.onButtonClick(text) // Send original function name for logic
            }
        }
        CalculatorButton(
            "e",
            Modifier
                .weight(1f)
                .fillMaxHeight(),
            isOperator = true
        ) { viewModel.onButtonClick("e") }
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
        else -> MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val contentColor = when {
        isClear -> MaterialTheme.colorScheme.onErrorContainer
        isSpecial -> MaterialTheme.colorScheme.onPrimary
        isOperator -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp, minWidth = 40.dp), // Ensure minimum touch target
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp) // Adjusted padding
    ) {
        Text(
            text,
            fontSize = if (text.length > 3) 16.sp else 20.sp, // Smaller font for longer text
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// --- Preview Stubs (Ensure you have a CalculatorViewModel placeholder for these to work) ---

// Placeholder for CalculatorViewModel if not already defined elsewhere
// Remove or replace with your actual ViewModel
// enum class AngleUnit { DEG, RAD }
// class CalculatorViewModel {
//     var isScientificMode: Boolean by mutableStateOf(true)
//         private set // Or make it public if toggled from outside preview
//     var isInverseMode: Boolean by mutableStateOf(false)
//         private set
//     var angleUnit: AngleUnit by mutableStateOf(AngleUnit.DEG)
//         private set

//     fun toggleScientificMode() {
//         isScientificMode = !isScientificMode
//     }
//     fun onButtonClick(text: String) { /* TODO */ }
//     fun onParenthesesClick() { /* TODO */ }
//     fun toggleAngleUnit() {
//         angleUnit = if (angleUnit == AngleUnit.DEG) AngleUnit.RAD else AngleUnit.DEG
//     }
// }

// @Preview(showBackground = true, widthDp = 360, heightDp = 740)
// @Composable
// fun ButtonLayoutPreviewScientificAnimated() {
//     val previewViewModel = CalculatorViewModel() // Initialize with isScientificMode = true
//     // To see the animation in preview, you might need to interact or trigger state change
//     // or have different previews for different states.
//     MaterialTheme {
//         ButtonLayout(viewModel = previewViewModel, isTablet = false, isLandscape = false)
//     }
// }

// @Preview(showBackground = true, widthDp = 360, heightDp = 600)
// @Composable
// fun ButtonLayoutPreviewCommonAnimated() {
//     val previewViewModel = CalculatorViewModel().apply {
//         // If your ViewModel needs a method to set initial state for preview:
//         // (this assumes isScientificMode is mutable for preview setup)
//         // isScientificMode = false // Or toggle it if that's how it works
//     }
//     // If isScientificMode is toggled, for this preview, ensure it starts as false
//     // One way: previewViewModel.toggleScientificMode() // if it starts true

//     MaterialTheme {
//         ButtonLayout(viewModel = previewViewModel, isTablet = false, isLandscape = false)
//     }
// }