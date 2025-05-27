package com.xenon.calculator.ui.layouts.converter

// Haze Imports
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xenon.calculator.R
import com.xenon.calculator.ui.layouts.CollapsingAppBarLayout
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.ConverterViewModel
import com.xenon.calculator.viewmodel.classes.ConverterType
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun TabletConverter(
    onNavigateBack: (() -> Unit)? = null,
    viewModel: ConverterViewModel
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ConverterInputGroup(modifier = Modifier.weight(1f)) {
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

                    ConverterInputGroup(modifier = Modifier.weight(1f)) {
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


@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun TabletConverterScreenPreview() {
    CalculatorTheme {
        LocalContext.current
        val previewViewModel: ConverterViewModel = viewModel()
        TabletConverter(onNavigateBack = {}, viewModel = previewViewModel)
    }
}