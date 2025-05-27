package com.xenon.calculator.ui.layouts.converter

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
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
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.ConverterViewModel
import com.xenon.calculator.viewmodel.classes.ConverterType
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi


@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun CoverConverter(
    onNavigateBack: (() -> Unit)? = null, viewModel: ConverterViewModel
) {
    LocalContext.current
    val hazeState = remember { HazeState() }

    val selectedType by viewModel.selectedConverterType
    val value1 by viewModel.value1
    val value2 by viewModel.value2

    val fromTemperatureUnit by viewModel.fromTemperatureUnit
    val toTemperatureUnit by viewModel.toTemperatureUnit
    val fromCurrencyUnit by viewModel.fromCurrencyUnit
    val fromVolumeUnit by viewModel.fromVolumeUnit
    val fromLengthUnit by viewModel.fromLengthUnit
    val toCurrencyUnit by viewModel.toCurrencyUnit
    val fromWeightUnit by viewModel.fromWeightUnit
    val toVolumeUnit by viewModel.toVolumeUnit
    val toLengthUnit by viewModel.toLengthUnit
    val fromAreaUnit by viewModel.fromAreaUnit
    val toWeightUnit by viewModel.toWeightUnit
    val toAreaUnit by viewModel.toAreaUnit

    Scaffold(
        containerColor = Color.Black, topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.converter), color = Color.White) },
                navigationIcon = {
                    onNavigateBack?.let {
                        IconButton(onClick = it) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Navigate back",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }) { contentPadding ->

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .background(Color.Black)
                .fillMaxSize()
                .hazeSource(hazeState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ConverterTypeDropdown(
                    selectedType = selectedType, onTypeSelected = { newType ->
                        viewModel.onConverterTypeChange(newType)
                    }, hazeState = hazeState
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        UnitDropdown(
                            label = fromUnitLabel(selectedType), // Now a Composable call
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

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
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
                                focusedContainerColor = colorScheme.primary.copy(alpha = 0.25f),
                                unfocusedContainerColor = colorScheme.primary.copy(
                                    alpha = 0.25f
                                ),
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
}


@Composable
private fun fromUnitLabel(type: ConverterType): String {
    val typeName = stringResource(id = type.displayNameResId)
    // Pass typeName.lowercase() as an argument to fill the %s placeholder
    return stringResource(id = R.string.label_from, typeName.lowercase())
}

@Composable
private fun toUnitLabel(type: ConverterType): String {
    val typeName = stringResource(id = type.displayNameResId)
    // Pass typeName.lowercase() as an argument to fill the %s placeholder
    return stringResource(id = R.string.label_to, typeName.lowercase())
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun CoverConverterScreenPreview() {
    CalculatorTheme {
        LocalContext.current
        val previewViewModel: ConverterViewModel = viewModel()
        CoverConverter(onNavigateBack = {}, viewModel = previewViewModel)
    }
}