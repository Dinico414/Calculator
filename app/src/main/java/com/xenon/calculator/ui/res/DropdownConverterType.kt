package com.xenon.calculator.ui.res

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
import androidx.compose.ui.res.stringResource
import com.xenon.calculator.ui.values.NoElevation
import com.xenon.calculator.ui.values.SmallCornerRadius
import com.xenon.calculator.ui.values.SmallMediumPadding
import com.xenon.calculator.viewmodel.classes.ConverterType
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.CupertinoMaterials
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.FluentMaterials

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun ConverterTypeDropdown(
    selectedType: ConverterType, onTypeSelected: (ConverterType) -> Unit, hazeState: HazeState
) {
    var expanded by remember { mutableStateOf(false) }
    val items = ConverterType.entries.toTypedArray()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = stringResource(id = selectedType.displayNameResId),
            onValueChange = {},
            readOnly = true,
            label = { Text("Converter Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            shape = RoundedCornerShape(SmallCornerRadius),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.secondary,
                unfocusedContainerColor = colorScheme.secondary.copy(alpha = 0.7f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = colorScheme.onSecondary,
                unfocusedTextColor = colorScheme.onSecondary,
                focusedLabelColor = colorScheme.onSecondary.copy(alpha = 0.4f),
                unfocusedLabelColor = colorScheme.onSecondary.copy(alpha = 0.4f),
                focusedTrailingIconColor = colorScheme.onSecondary,
                unfocusedTrailingIconColor = colorScheme.onSecondary,
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
            items.forEach { type ->
                DropdownMenuItem(text = {
                    Text(
                        text = stringResource(id = type.displayNameResId),
                        color = colorScheme.onSurface
                    )
                }, onClick = {
                    onTypeSelected(type)
                    expanded = false
                })
            }
        }
    }
}