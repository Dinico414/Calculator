package com.xenon.calculator.ui.layouts.converter

// Haze Imports
import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xenon.calculator.ui.layouts.CollapsingAppBarLayout
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
    onNavigateBack: (() -> Unit)? = null,
viewModel: ConverterViewModel) {
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

    CollapsingAppBarLayout(title = { fontSize, color ->
        Text("UnitConverter", fontSize = fontSize, color = color)
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
                .padding(horizontal = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
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
                        label = { Text("Value 1") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        )
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
                        label = { Text("Value 2") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                }
            }
        }
    }
}

private fun fromUnitLabel(type: ConverterType): String = "From (${type.displayName.lowercase()})"
private fun toUnitLabel(type: ConverterType): String = "To (${type.displayName.lowercase()})"

@Composable
fun ConverterInputGroup(
    modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
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
            value = selectedType.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Converter Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
                focusedLabelColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.4f),
                unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.4f),
                focusedTrailingIconColor = MaterialTheme.colorScheme.onSecondary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSecondary,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.Transparent,
            shadowElevation = 0.dp,
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Gray)
                .hazeEffect(
                    state = hazeState, style = FluentMaterials.thinAcrylic()
                )
        ) {
            items.forEach { type ->
                DropdownMenuItem(text = {
                    Text(
                        type.displayName, color = MaterialTheme.colorScheme.onSurface
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
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedLabelColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
                unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
                focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.Transparent,
            shadowElevation = 0.dp,
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Gray)
                .hazeEffect(
                    state = hazeState, style = FluentMaterials.thinAcrylic()
                )
        ) {
            units.forEach { unit ->
                DropdownMenuItem(text = {
                    Text(
                        getDisplayName(unit), color = MaterialTheme.colorScheme.onSurface
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
        val previewViewModel: ConverterViewModel = viewModel()
            CompactConverter(onNavigateBack = {}, viewModel = previewViewModel)
    }
}