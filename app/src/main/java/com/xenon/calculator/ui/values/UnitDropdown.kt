package com.xenon.calculator.ui.values

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.xenon.calculator.ui.res.GenericUnitDropdown
import com.xenon.calculator.viewmodel.classes.AreaUnit
import com.xenon.calculator.viewmodel.classes.ConverterType
import com.xenon.calculator.viewmodel.classes.CurrencyUnit
import com.xenon.calculator.viewmodel.classes.LengthUnit
import com.xenon.calculator.viewmodel.classes.TemperatureUnit
import com.xenon.calculator.viewmodel.classes.VolumeUnit
import com.xenon.calculator.viewmodel.classes.WeightUnit
import dev.chrisbanes.haze.HazeState

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
        ConverterType.AREA -> {
            val context = LocalContext.current
            GenericUnitDropdown(
                label,
                AreaUnit.entries.toTypedArray(),
                selectedAreaUnit,
                onAreaUnitSelected,
                { areaUnit -> areaUnit.getDisplayName(context) },
                hazeState,
                modifier
            )
        }

        ConverterType.LENGTH -> {
            val context = LocalContext.current
            GenericUnitDropdown(
                label,
                LengthUnit.entries.toTypedArray(),
                selectedLengthUnit,
                onLengthUnitSelected,
                { lengthUnit -> lengthUnit.getDisplayName(context) },
                hazeState,
                modifier
            )
        }

        ConverterType.VOLUME -> {
            val context = LocalContext.current
            GenericUnitDropdown(
                label,
                VolumeUnit.entries.toTypedArray(),
                selectedVolumeUnit,
                onVolumeUnitSelected,
                { volumeUnit -> volumeUnit.getDisplayName(context) },
                hazeState,
                modifier
            )
        }

        ConverterType.WEIGHT -> {
            val context = LocalContext.current
            GenericUnitDropdown(
                label,
                WeightUnit.entries.toTypedArray(),
                selectedWeightUnit,
                onWeightUnitSelected,
                { weightUnit -> weightUnit.getDisplayName(context) },
                hazeState,
                modifier
            )
        }

        ConverterType.TEMPERATURE -> GenericUnitDropdown(
            label,
            TemperatureUnit.entries.toTypedArray(),
            selectedTemperatureUnit,
            onTemperatureUnitSelected,
            { it.displayName },
            hazeState,
            modifier
        )

        ConverterType.CURRENCY -> {
            val context = LocalContext.current
            GenericUnitDropdown(
                label,
                CurrencyUnit.entries.toTypedArray(),
                selectedCurrencyUnit,
                onCurrencyUnitSelected,
                { currencyUnit -> currencyUnit.getDisplayName(context) },
                hazeState,
                modifier
            )
        }
    }
}