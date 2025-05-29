package com.xenon.calculator.ui.layouts.converter

// Haze Imports
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xenon.calculator.R
import com.xenon.calculator.ui.layouts.CollapsingAppBarLayout
import com.xenon.calculator.ui.res.LargeCornerRadius
import com.xenon.calculator.ui.res.LargePadding
import com.xenon.calculator.ui.res.LargerSpacing
import com.xenon.calculator.ui.res.MediumCornerRadius
import com.xenon.calculator.ui.res.MediumPadding
import com.xenon.calculator.ui.res.MinMediumButtonHeight
import com.xenon.calculator.ui.res.NoElevation
import com.xenon.calculator.ui.res.SmallCornerRadius
import com.xenon.calculator.ui.res.SmallMediumPadding
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.ConverterViewModel
import com.xenon.calculator.viewmodel.classes.AreaUnit
import com.xenon.calculator.viewmodel.classes.ConverterType
import com.xenon.calculator.viewmodel.classes.CurrencyUnit
import com.xenon.calculator.viewmodel.classes.LengthUnit
import com.xenon.calculator.viewmodel.classes.TemperatureUnit
import com.xenon.calculator.viewmodel.classes.VolumeUnit
import com.xenon.calculator.viewmodel.classes.WeightUnit
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.FluentMaterials

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun CompactConverter(
    onNavigateBack: (() -> Unit)? = null, viewModel: ConverterViewModel
) {
    val hazeState = remember { HazeState() }

    val selectedType by viewModel.selectedConverterType
    val value1 by viewModel.value1
    val value2 by viewModel.value2

    val fromVolumeUnit by viewModel.fromVolumeUnit
    val toVolumeUnit by viewModel.toVolumeUnit
    val fromLengthUnit by viewModel.fromLengthUnit
    val toLengthUnit by viewModel.toLengthUnit
    val fromTemperatureUnit by viewModel.fromTemperatureUnit
    val toTemperatureUnit by viewModel.toTemperatureUnit
    val fromCurrencyUnit by viewModel.fromCurrencyUnit
    val toCurrencyUnit by viewModel.toCurrencyUnit
    val fromAreaUnit by viewModel.fromAreaUnit
    val toAreaUnit by viewModel.toAreaUnit
    val fromWeightUnit by viewModel.fromWeightUnit
    val toWeightUnit by viewModel.toWeightUnit

    var accumulatedRotation by remember { mutableFloatStateOf(0f) } // Store the total rotation
    val rotationAngle by animateFloatAsState(
        targetValue = accumulatedRotation, // Animate to the new accumulated rotation
        animationSpec = tween(durationMillis = 300),
        label = "IconRotation"
    )

    CollapsingAppBarLayout(title = { fontSize, color ->
        Text(stringResource(id = R.string.converter), fontSize = fontSize, color = color)
    }, navigationIcon = {
        onNavigateBack?.let {
            IconButton(onClick = it) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back"
                )
            }
        }
    }) { contentPadding ->

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .hazeSource(hazeState)
                .padding(horizontal = LargePadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(LargeCornerRadius))
                    .background(colorScheme.surfaceContainer)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = MediumPadding, horizontal = MediumPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(LargerSpacing)
            ) {
                ConverterInputGroup {
                    ConverterTypeDropdown(
                        selectedType = selectedType, onTypeSelected = { newType ->
                            viewModel.onConverterTypeChange(newType)
                        }, hazeState = hazeState
                    )
                }

                ConverterInputGroup(modifier = Modifier.fillMaxWidth()) {
                    UnitDropdown(
                        label = fromUnitLabel(selectedType),
                        selectedConverterType = selectedType,
                        selectedVolumeUnit = fromVolumeUnit,
                        onVolumeUnitSelected = { unit ->
                            viewModel.onFromVolumeUnitChange(
                                unit
                            )
                        },
                        selectedLengthUnit = fromLengthUnit,
                        onLengthUnitSelected = { unit ->
                            viewModel.onFromLengthUnitChange(
                                unit
                            )
                        },
                        selectedTemperatureUnit = fromTemperatureUnit,
                        onTemperatureUnitSelected = { unit ->
                            viewModel.onFromTemperatureUnitChange(
                                unit
                            )
                        },
                        selectedCurrencyUnit = fromCurrencyUnit,
                        onCurrencyUnitSelected = { unit ->
                            viewModel.onFromCurrencyUnitChange(
                                unit
                            )
                        },
                        selectedAreaUnit = fromAreaUnit,
                        onAreaUnitSelected = { unit -> viewModel.onFromAreaUnitChange(unit) },
                        selectedWeightUnit = fromWeightUnit,
                        onWeightUnitSelected = { unit ->
                            viewModel.onFromWeightUnitChange(
                                unit
                            )
                        },
                        hazeState = hazeState,
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = value1,
                        onValueChange = { newValue ->
                            viewModel.onValueChanged(
                                newValue, ConverterViewModel.EditedField.FIELD1
                            )
                        },
                        label = { Text(stringResource(id = R.string.value_1)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(SmallCornerRadius)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorScheme.primary.copy(alpha = 0.25f),
                            unfocusedContainerColor = colorScheme.primary.copy(alpha = 0.25f),
                            focusedIndicatorColor = colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = colorScheme.onPrimaryContainer,
                            unfocusedTextColor = colorScheme.onPrimaryContainer,
                            focusedLabelColor = colorScheme.primary,
                            unfocusedLabelColor = colorScheme.primary,
                        )
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.onUnitsSwitch()
                        accumulatedRotation += 180f
                    },
                    modifier = Modifier
                        .height(MinMediumButtonHeight)
                        .fillMaxWidth(0.5f)
                        .clip(CircleShape)
                        .background(colorScheme.tertiary)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.swap),
                        contentDescription = "Switch units",
                        tint = colorScheme.onTertiary,
                        modifier = Modifier.rotate(rotationAngle)
                    )
                }

                ConverterInputGroup(modifier = Modifier.fillMaxWidth()) {
                    UnitDropdown(
                        label = toUnitLabel(selectedType),
                        selectedConverterType = selectedType,
                        selectedVolumeUnit = toVolumeUnit,
                        onVolumeUnitSelected = { unit ->
                            viewModel.onToVolumeUnitChange(
                                unit
                            )
                        },
                        selectedLengthUnit = toLengthUnit,
                        onLengthUnitSelected = { unit ->
                            viewModel.onToLengthUnitChange(
                                unit
                            )
                        },
                        selectedTemperatureUnit = toTemperatureUnit,
                        onTemperatureUnitSelected = { unit ->
                            viewModel.onToTemperatureUnitChange(
                                unit
                            )
                        },
                        selectedCurrencyUnit = toCurrencyUnit,
                        onCurrencyUnitSelected = { unit ->
                            viewModel.onToCurrencyUnitChange(
                                unit
                            )
                        },
                        selectedAreaUnit = toAreaUnit,
                        onAreaUnitSelected = { unit -> viewModel.onToAreaUnitChange(unit) },
                        selectedWeightUnit = toWeightUnit,
                        onWeightUnitSelected = { unit ->
                            viewModel.onToWeightUnitChange(
                                unit
                            )
                        },
                        hazeState = hazeState,
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = value2,
                        onValueChange = { newValue ->
                            viewModel.onValueChanged(
                                newValue, ConverterViewModel.EditedField.FIELD2
                            )
                        },
                        label = { Text(stringResource(id = R.string.value_2)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(SmallCornerRadius)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorScheme.primary.copy(alpha = 0.25f),
                            unfocusedContainerColor = colorScheme.primary.copy(alpha = 0.25f),
                            focusedIndicatorColor = colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = colorScheme.onPrimaryContainer,
                            unfocusedTextColor = colorScheme.onPrimaryContainer,
                            focusedLabelColor = colorScheme.primary,
                            unfocusedLabelColor = colorScheme.primary,
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun fromUnitLabel(type: ConverterType): String {
    val typeName = stringResource(id = type.displayNameResId)
    return stringResource(id = R.string.label_from, typeName.lowercase())
}

@Composable
private fun toUnitLabel(type: ConverterType): String {
    val typeName = stringResource(id = type.displayNameResId)
    return stringResource(id = R.string.label_to, typeName.lowercase())
}


@Composable
fun ConverterInputGroup(
    modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(MediumCornerRadius))
            .background(colorScheme.surfaceContainerHighest)
            .padding(horizontal = MediumPadding, vertical = MediumPadding),
        verticalArrangement = Arrangement.spacedBy(LargerSpacing),
        content = content
    )
}

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
                .background(colorScheme.surfaceContainer)
                .hazeEffect(
                    state = hazeState, style = FluentMaterials.thinAcrylic()
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
                .background(colorScheme.surfaceContainer)
                .hazeEffect(
                    state = hazeState, style = FluentMaterials.thinAcrylic()
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

@Composable
fun UnitDropdown(
    label: String,
    selectedConverterType: ConverterType,
    selectedVolumeUnit: VolumeUnit,
    onVolumeUnitSelected: (VolumeUnit) -> Unit,
    selectedLengthUnit: LengthUnit,
    onLengthUnitSelected: (LengthUnit) -> Unit,
    selectedTemperatureUnit: TemperatureUnit,
    onTemperatureUnitSelected: (TemperatureUnit) -> Unit,
    selectedCurrencyUnit: CurrencyUnit,
    onCurrencyUnitSelected: (CurrencyUnit) -> Unit,
    selectedAreaUnit: AreaUnit,
    onAreaUnitSelected: (AreaUnit) -> Unit,
    selectedWeightUnit: WeightUnit,
    onWeightUnitSelected: (WeightUnit) -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    when (selectedConverterType) {
        ConverterType.VOLUME -> GenericUnitDropdown(
            label,
            VolumeUnit.entries.toTypedArray(),
            selectedVolumeUnit,
            onVolumeUnitSelected,
            { it.displayName },
            hazeState,
            modifier
        )

        ConverterType.AREA -> GenericUnitDropdown(
            label,
            AreaUnit.entries.toTypedArray(),
            selectedAreaUnit,
            onAreaUnitSelected,
            { it.displayName },
            hazeState,
            modifier
        )

        ConverterType.LENGTH -> GenericUnitDropdown(
            label,
            LengthUnit.entries.toTypedArray(),
            selectedLengthUnit,
            onLengthUnitSelected,
            { it.displayName },
            hazeState,
            modifier
        )

        ConverterType.TEMPERATURE -> GenericUnitDropdown(
            label,
            TemperatureUnit.entries.toTypedArray(),
            selectedTemperatureUnit,
            onTemperatureUnitSelected,
            { it.displayName },
            hazeState,
            modifier
        )

        ConverterType.CURRENCY -> GenericUnitDropdown(
            label,
            CurrencyUnit.entries.toTypedArray(),
            selectedCurrencyUnit,
            onCurrencyUnitSelected,
            { it.displayName },
            hazeState,
            modifier
        )

        ConverterType.WEIGHT -> GenericUnitDropdown(
            label,
            WeightUnit.entries.toTypedArray(),
            selectedWeightUnit,
            onWeightUnitSelected,
            { it.displayName },
            hazeState,
            modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CompactConverterScreenPreview() {
    CalculatorTheme {
        LocalContext.current
        val previewViewModel: ConverterViewModel = viewModel()
        CompactConverter(onNavigateBack = {}, viewModel = previewViewModel)
    }
}