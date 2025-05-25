package com.xenon.calculator.ui.layouts.converter

// import androidx.compose.foundation.layout.width // No longer needed if icon button is removed
// import androidx.compose.material.icons.filled.SwapHoriz // Icon no longer needed
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xenon.calculator.ui.layouts.ConverterCollapsingAppBarLayout
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.ConverterViewModel
import com.xenon.calculator.viewmodel.classes.AreaUnit
import com.xenon.calculator.viewmodel.classes.ConverterType
import com.xenon.calculator.viewmodel.classes.CurrencyUnit
import com.xenon.calculator.viewmodel.classes.LengthUnit
import com.xenon.calculator.viewmodel.classes.TemperatureUnit
import com.xenon.calculator.viewmodel.classes.VolumeUnit
import com.xenon.calculator.viewmodel.classes.WeightUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactConverterScreen(
    onNavigateBack: (() -> Unit)? = null,
    converterViewModel: ConverterViewModel = viewModel()
) {
    val selectedType by converterViewModel.selectedConverterType
    val value1 by converterViewModel.value1
    val value2 by converterViewModel.value2

    val fromVolumeUnit by converterViewModel.fromVolumeUnit
    val toVolumeUnit by converterViewModel.toVolumeUnit
    val fromLengthUnit by converterViewModel.fromLengthUnit
    val toLengthUnit by converterViewModel.toLengthUnit
    val fromTemperatureUnit by converterViewModel.fromTemperatureUnit
    val toTemperatureUnit by converterViewModel.toTemperatureUnit
    val fromCurrencyUnit by converterViewModel.fromCurrencyUnit
    val toCurrencyUnit by converterViewModel.toCurrencyUnit
    val fromAreaUnit by converterViewModel.fromAreaUnit
    val toAreaUnit by converterViewModel.toAreaUnit
    val fromWeightUnit by converterViewModel.fromWeightUnit
    val toWeightUnit by converterViewModel.toWeightUnit

    ConverterCollapsingAppBarLayout(
        navigationIcon = {
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
                .padding(horizontal = 15.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 20.dp, horizontal = 15.dp), // Inner padding for the card
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between groups
            ) {
                // Group 1: Converter Type
                ConverterInputGroup {
                    ConverterTypeDropdown(
                        selectedType = selectedType,
                        onTypeSelected = { newType ->
                            converterViewModel.onConverterTypeChange(newType)
                        }
                    )
                }

                // Row for Group 2 (Value1/FromUnit) and Group 3 (Value2/ToUnit)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top, // Or Alignment.CenterVertically
                    horizontalArrangement = Arrangement.spacedBy(10.dp) // Space between the two groups
                ) {
                    // Group 2: Value 1 and "From" Unit
                    ConverterInputGroup(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = value1,
                            onValueChange = { newValue ->
                                converterViewModel.onValueChanged(newValue, ConverterViewModel.EditedField.FIELD1)
                            },
                            label = { Text("Value 1") }, // Or "Input"
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        UnitDropdown( // This is the "From" unit, associated with Value 1
                            label = fromUnitLabel(selectedType), // Dynamic label
                            selectedConverterType = selectedType,
                            selectedVolumeUnit = fromVolumeUnit,
                            onVolumeUnitSelected = { unit -> converterViewModel.onFromVolumeUnitChange(unit) },
                            selectedLengthUnit = fromLengthUnit,
                            onLengthUnitSelected = { unit -> converterViewModel.onFromLengthUnitChange(unit) },
                            selectedTemperatureUnit = fromTemperatureUnit,
                            onTemperatureUnitSelected = { unit -> converterViewModel.onFromTemperatureUnitChange(unit) },
                            selectedCurrencyUnit = fromCurrencyUnit,
                            onCurrencyUnitSelected = { unit -> converterViewModel.onFromCurrencyUnitChange(unit) },
                            selectedAreaUnit = fromAreaUnit,
                            onAreaUnitSelected = { unit -> converterViewModel.onFromAreaUnitChange(unit) },
                            selectedWeightUnit = fromWeightUnit,
                            onWeightUnitSelected = { unit -> converterViewModel.onFromWeightUnitChange(unit) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

// Group 3: Value 2 and "To" Unit
                    ConverterInputGroup(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = value2,
                            onValueChange = { newValue ->
                                converterViewModel.onValueChanged(newValue, ConverterViewModel.EditedField.FIELD2)
                            },
                            label = { Text("Value 2") }, // Or "Result"
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        UnitDropdown( // This is the "To" unit, associated with Value 2
                            label = toUnitLabel(selectedType), // Dynamic label
                            selectedConverterType = selectedType,
                            selectedVolumeUnit = toVolumeUnit,
                            onVolumeUnitSelected = { unit -> converterViewModel.onToVolumeUnitChange(unit) },
                            selectedLengthUnit = toLengthUnit,
                            onLengthUnitSelected = { unit -> converterViewModel.onToLengthUnitChange(unit) },
                            selectedTemperatureUnit = toTemperatureUnit,
                            onTemperatureUnitSelected = { unit -> converterViewModel.onToTemperatureUnitChange(unit) },
                            selectedCurrencyUnit = toCurrencyUnit,
                            onCurrencyUnitSelected = { unit -> converterViewModel.onToCurrencyUnitChange(unit) },
                            selectedAreaUnit = toAreaUnit,
                            onAreaUnitSelected = { unit -> converterViewModel.onToAreaUnitChange(unit) },
                            selectedWeightUnit = toWeightUnit,
                            onWeightUnitSelected = { unit -> converterViewModel.onToWeightUnitChange(unit) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }


            }
        }
    }
}

// Helper functions for dynamic labels (optional, for better UX)
private fun fromUnitLabel(type: ConverterType): String = "From (${type.displayName.lowercase()})"
private fun toUnitLabel(type: ConverterType): String = "To (${type.displayName.lowercase()})"


@Composable
fun ConverterInputGroup(
    modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier // Apply the modifier passed from the Row (e.g., weight)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterTypeDropdown(
    selectedType: ConverterType, onTypeSelected: (ConverterType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val items = ConverterType.values()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedType.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Converter Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { type ->
                DropdownMenuItem(text = { Text(type.displayName) }, onClick = {
                    onTypeSelected(type)
                    expanded = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> GenericUnitDropdown(
    label: String,
    units: Array<T>,
    selectedUnit: T,
    onUnitSelected: (T) -> Unit,
    getDisplayName: (T) -> String,
    modifier: Modifier = Modifier
) where T : Enum<T> {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = getDisplayName(selectedUnit),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            units.forEach { unit ->
                DropdownMenuItem(text = { Text(getDisplayName(unit)) }, onClick = {
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
    modifier: Modifier = Modifier
) {
    when (selectedConverterType) {
        ConverterType.VOLUME -> GenericUnitDropdown(
            label,
            VolumeUnit.values(),
            selectedVolumeUnit,
            onVolumeUnitSelected,
            { it.displayName },
            modifier
        )

        ConverterType.AREA -> GenericUnitDropdown(
            label,
            AreaUnit.values(),
            selectedAreaUnit,
            onAreaUnitSelected,
            { it.displayName },
            modifier
        )

        ConverterType.LENGTH -> GenericUnitDropdown(
            label,
            LengthUnit.values(),
            selectedLengthUnit,
            onLengthUnitSelected,
            { it.displayName },
            modifier
        )

        ConverterType.TEMPERATURE -> GenericUnitDropdown(
            label,
            TemperatureUnit.values(),
            selectedTemperatureUnit,
            onTemperatureUnitSelected,
            { it.displayName },
            modifier
        )

        ConverterType.CURRENCY -> GenericUnitDropdown(
            label,
            CurrencyUnit.values(),
            selectedCurrencyUnit,
            onCurrencyUnitSelected,
            { it.displayName },
            modifier
        )

        ConverterType.WEIGHT -> GenericUnitDropdown(
            label,
            WeightUnit.values(),
            selectedWeightUnit,
            onWeightUnitSelected,
            { it.displayName },
            modifier
        )
    }
}

@Preview
@Composable
fun CompactConverterScreenPreview() {
    CalculatorTheme {
        CompactConverterScreen()
    }
}