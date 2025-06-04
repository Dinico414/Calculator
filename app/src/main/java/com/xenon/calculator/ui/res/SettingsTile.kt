package com.xenon.calculator.ui.res

import androidx.compose.foundation.ExperimentalFoundationApi // Import this
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable // Import this
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
import com.xenon.calculator.ui.values.ExtraLargePadding
import com.xenon.calculator.ui.values.LargerPadding
import com.xenon.calculator.ui.values.MediumCornerRadius


@OptIn(ExperimentalFoundationApi::class) // Add this annotation
@Composable
fun SettingsTile(
    title: String,
    subtitle: String,
    onClick: (() -> Unit)?,
    onLongClick: (() -> Unit)? = null, // Add onLongClick parameter
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = RoundedCornerShape(MediumCornerRadius),
    horizontalPadding: Dp = LargerPadding,
    verticalPadding: Dp = ExtraLargePadding
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .combinedClickable( // Use combinedClickable
                onClick = { onClick?.invoke() }, // Call the existing onClick
                onLongClick = { onLongClick?.invoke() }, // Call the new onLongClick
                role = Role.Button,
                enabled = onClick != null || onLongClick != null // Enable if either is provided
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