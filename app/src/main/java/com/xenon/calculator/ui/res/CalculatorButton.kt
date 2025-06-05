package com.xenon.calculator.ui.res

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xenon.calculator.ui.values.MediumButtonHeight
import com.xenon.calculator.ui.values.MinMediumButtonHeight
import com.xenon.calculator.ui.values.NoElevation
import com.xenon.calculator.ui.values.SmallPadding
import kotlin.math.abs

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    isOperator: Boolean = false,
    isSpecial: Boolean = false,
    isClear: Boolean = false,
    isScientificButton: Boolean,
    isNumber: Boolean,
    isGlobalScientificModeActive: Boolean,
    isInverseActive: Boolean = false,
    fontFamily: FontFamily? = null,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val haptic = LocalHapticFeedback.current

    val targetFontSize = when {
        isScientificButton -> when {
            isLandscape -> if (text.length > 2 || text.contains("⁻¹") || text.contains("ˣ") || text.contains("²")) 18.sp else 20.sp
            else -> if (text.length > 2 || text.contains("⁻¹") || text.contains("ˣ") || text.contains("²")) 18.sp else 20.sp
        }
        isNumber -> when {
            isLandscape -> if (isGlobalScientificModeActive) 26.sp else 28.sp
            else -> if (isGlobalScientificModeActive) 26.sp else 32.sp
        }
        else -> when {
            isLandscape -> if (isGlobalScientificModeActive) 26.sp else 28.sp
            else -> if (isGlobalScientificModeActive) 26.sp else 32.sp
        }
    }

    val animatedFontSize by animateDpAsState(
        targetValue = targetFontSize.value.dp,
        animationSpec = tween(durationMillis = 300),
        label = "FontSizeAnimation"
    )

    val containerColor = when {
        isClear -> MaterialTheme.colorScheme.tertiary
        isSpecial -> MaterialTheme.colorScheme.inversePrimary
        text == "INV" && isScientificButton -> if (isInverseActive) MaterialTheme.colorScheme.error else Color.Transparent
        isScientificButton -> Color.Transparent
        isOperator -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.secondaryContainer
    }

    val contentColor = when {
        isClear -> MaterialTheme.colorScheme.onPrimary
        isSpecial -> MaterialTheme.colorScheme.onSurface
        text == "INV" && isScientificButton -> if (isInverseActive) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.onSurfaceVariant
        isScientificButton -> MaterialTheme.colorScheme.onSurfaceVariant
        isOperator -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurface
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var longClickFired = remember { false }
    var longClickAnimationActive = remember { mutableStateOf(false) }

    val cornerRadiusPercent by animateIntAsState(
        targetValue = if (isPressed && !isScientificButton) 30 else 100,
        animationSpec = tween(durationMillis = if (isScientificButton) 0 else 350),
        label = "cornerRadiusAnimation",
    )

    val longPressDelay = 600
    if (onLongClick != null) {
        animateIntAsState(
            targetValue = if (isPressed) 1 else 0,
            animationSpec = tween(durationMillis = longPressDelay),
            finishedListener = { value ->
                if (isPressed && value == 1) {
                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                    onLongClick()
                    longClickFired = true
                    longClickAnimationActive.value = true
                }
            }
        )
    }
    val longClickAnimValue by animateFloatAsState(
        targetValue = if (longClickAnimationActive.value) 3f else 0f,
        animationSpec = tween(
            durationMillis = 200 / 2,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            longClickAnimationActive.value = false
        }
    )

    Button(
        onClick = {
            if (longClickFired) {
                longClickFired = false
            } else {
                haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                onClick()
            }
        },
        modifier = modifier
            .defaultMinSize(
                minWidth = MinMediumButtonHeight,
                minHeight = MediumButtonHeight
            )
            .padding(abs(longClickAnimValue).dp),
        shape = RoundedCornerShape(percent = cornerRadiusPercent),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor, contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = NoElevation, pressedElevation = NoElevation),
        contentPadding = PaddingValues(horizontal = SmallPadding, vertical = SmallPadding),
        interactionSource = interactionSource
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            fontSize = animatedFontSize.value.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}