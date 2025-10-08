package com.xenonware.calculator.ui.res

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.xenonware.calculator.ui.values.NoElevation
import com.xenonware.calculator.ui.values.SmallCornerRadius
import com.xenonware.calculator.ui.values.SmallMediumPadding
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.CupertinoMaterials
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi


@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun <T> GenericUnitDropdown(
    label: String,
    units: Array<T>,
    selectedUnit: T,
    onUnitSelected: (T) -> Unit,
    getDisplayName: (T) -> String,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) where T : Enum<T> {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier
    ) {
        TextField(
            value = getDisplayName(selectedUnit),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            shape = RoundedCornerShape(SmallCornerRadius),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.primary,
                unfocusedContainerColor = colorScheme.primary.copy(alpha = 0.7f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = colorScheme.onPrimary,
                unfocusedTextColor = colorScheme.onPrimary,
                focusedLabelColor = colorScheme.onPrimary.copy(alpha = 0.4f),
                unfocusedLabelColor = colorScheme.onPrimary.copy(alpha = 0.4f),
                focusedTrailingIconColor = colorScheme.onPrimary,
                unfocusedTrailingIconColor = colorScheme.onPrimary,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.Transparent,
            shadowElevation = NoElevation,
            modifier = Modifier
                .padding(top = SmallMediumPadding, bottom = SmallMediumPadding)
                .clip(RoundedCornerShape(SmallCornerRadius))
                .background(colorScheme.surface)
                .hazeEffect(
                    state = hazeState, style = CupertinoMaterials.regular(colorScheme.surface)
                )
        ) {
            units.forEach { unit ->
                DropdownMenuItem(text = {
                    Text(
                        getDisplayName(unit), color = colorScheme.onSurface
                    )
                }, onClick = {
                    onUnitSelected(unit)
                    expanded = false
                })
            }
        }
    }
}