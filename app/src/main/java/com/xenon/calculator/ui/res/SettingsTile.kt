package com.xenon.calculator.ui.res

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
// Assuming these are defined in your project, e.g., in a Dimens.kt or similar
// import com.xenon.calculator.ui.values.ExtraLargePadding
// import com.xenon.calculator.ui.values.LargePadding
// import com.xenon.calculator.ui.values.LargerPadding
// import com.xenon.calculator.ui.values.MediumCornerRadius
import androidx.compose.ui.unit.dp // Added for default shape if your constants aren't found

// Using placeholder Dp values if your constants are not resolved.
// Replace with your actual definitions.
val ExtraLargePadding: Dp = 24.dp
val LargerPadding: Dp = 20.dp
val MediumCornerRadius: Dp = 12.dp


@Composable
fun SettingsTile(
    title: String,
    subtitle: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = RoundedCornerShape(MediumCornerRadius), // Default shape
    horizontalPadding: Dp = LargerPadding,
    verticalPadding: Dp = ExtraLargePadding
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick, role = Role.Button)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = subtitleColor
            )
        }
    }
}