package com.xenon.calculator.ui.layouts.converter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xenon.calculator.viewmodel.ConverterViewModel
import com.xenon.calculator.viewmodel.classes.ConverterType
import com.xenon.calculator.viewmodel.classes.CurrencyUnit
import com.xenon.calculator.viewmodel.classes.LengthUnit
import com.xenon.calculator.viewmodel.classes.TemperatureUnit
import com.xenon.calculator.viewmodel.classes.VolumeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactConverterScreen(converterViewModel: ConverterViewModel = viewModel()) {
    val selectedType by converterViewModel.selectedConverterType
    val inputValue by converterViewModel.inputValue
    val outputValue by converterViewModel.outputValue

    val fromVolumeUnit by converterViewModel.fromVolumeUnit
    val toVolumeUnit by converterViewModel.toVolumeUnit
    val fromLengthUnit by converterViewModel.fromLengthUnit
    val toLengthUnit by converterViewModel.toLengthUnit
    val fromTemperatureUnit by converterViewModel.fromTemperatureUnit
    val toTemperatureUnit by converterViewModel.toTemperatureUnit
    val fromCurrencyUnit by converterViewModel.fromCurrencyUnit
    val toCurrencyUnit by converterViewModel.toCurrencyUnit

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Unit Converter") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ConverterTypeDropdown(
                selectedType = selectedType,
                onTypeSelected = { newType -> converterViewModel.onConverterTypeChange(newType) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputValue,
                onValueChange = { newValue -> converterViewModel.onInputValueChange(newValue) },
                label = { Text("Input Value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UnitDropdown(
                    label = "From",
                    selectedConverterType = selectedType,
                    selectedVolumeUnit = fromVolumeUnit,
                    onVolumeUnitSelected = { unit -> converterViewModel.onFromVolumeUnitChange(unit) },
                    selectedLengthUnit = fromLengthUnit,
                    onLengthUnitSelected = { unit -> converterViewModel.onFromLengthUnitChange(unit) },
                    selectedTemperatureUnit = fromTemperatureUnit,
                    onTemperatureUnitSelected = { unit -> converterViewModel.onFromTemperatureUnitChange(unit) },
                    selectedCurrencyUnit = fromCurrencyUnit,
                    onCurrencyUnitSelected = { unit -> converterViewModel.onFromCurrencyUnitChange(unit) },
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { converterViewModel.swapUnits() }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Swap Units")
                }

                UnitDropdown(
                    label = "To",
                    selectedConverterType = selectedType,
                    selectedVolumeUnit = toVolumeUnit,
                    onVolumeUnitSelected = { unit -> converterViewModel.onToVolumeUnitChange(unit) },
                    selectedLengthUnit = toLengthUnit,
                    onLengthUnitSelected = { unit -> converterViewModel.onToLengthUnitChange(unit) },
                    selectedTemperatureUnit = toTemperatureUnit,
                    onTemperatureUnitSelected = { unit -> converterViewModel.onToTemperatureUnitChange(unit) },
                    selectedCurrencyUnit = toCurrencyUnit,
                    onCurrencyUnitSelected = { unit -> converterViewModel.onToCurrencyUnitChange(unit) },
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Result: $outputValue",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterTypeDropdown(
    selectedType: ConverterType,
    onTypeSelected: (ConverterType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val items = ConverterType.values()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedType.displayName, // Use displayName from enum if defined
            onValueChange = {},
            readOnly = true,
            label = { Text("Converter Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName) }, // Use displayName
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> GenericUnitDropdown( // Made T non-nullable as it's always an Enum
    label: String,
    units: Array<T>,
    selectedUnit: T,
    onUnitSelected: (T) -> Unit,
    getDisplayName: (T) -> String, // Function to get display name
    modifier: Modifier = Modifier
) where T : Enum<T> { // Ensure T is an Enum
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = getDisplayName(selectedUnit), // Use display name
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(getDisplayName(unit)) }, // Use display name
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun UnitDropdown(
    label: String,
    selectedConverterType: ConverterType,
    // Volume
    selectedVolumeUnit: VolumeUnit,
    onVolumeUnitSelected: (VolumeUnit) -> Unit,
    // Length
    selectedLengthUnit: LengthUnit,
    onLengthUnitSelected: (LengthUnit) -> Unit,
    // Temperature
    selectedTemperatureUnit: TemperatureUnit,
    onTemperatureUnitSelected: (TemperatureUnit) -> Unit,
    // Currency
    selectedCurrencyUnit: CurrencyUnit,
    onCurrencyUnitSelected: (CurrencyUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    when (selectedConverterType) {
        ConverterType.VOLUME -> GenericUnitDropdown(label, VolumeUnit.values(), selectedVolumeUnit, onVolumeUnitSelected, { it.displayName }, modifier)
        ConverterType.LENGTH -> GenericUnitDropdown(label, LengthUnit.values(), selectedLengthUnit, onLengthUnitSelected, { it.displayName }, modifier)
        ConverterType.TEMPERATURE -> GenericUnitDropdown(label, TemperatureUnit.values(), selectedTemperatureUnit, onTemperatureUnitSelected, { it.displayName }, modifier)
        ConverterType.CURRENCY -> GenericUnitDropdown(label, CurrencyUnit.values(), selectedCurrencyUnit, onCurrencyUnitSelected, { it.displayName }, modifier)
    }
}

