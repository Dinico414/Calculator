package com.xenon.calculator.ui.res

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.xenon.calculator.ui.values.ExtraLargePadding
import com.xenon.calculator.ui.values.LargerPadding
import com.xenon.calculator.ui.values.MediumCornerRadius

@Composable
fun SettingsSwitchTile(
    title: String,
    subtitle: String = "",
    checked: Boolean = false,
    onCheckedChange: ((enabled: Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MediumCornerRadius))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick, role = Role.Button)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = LargerPadding, vertical = ExtraLargePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title, style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 5.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        Switch(
            checked = checked, onCheckedChange = onCheckedChange
        )
    }
}