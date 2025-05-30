package com.xenon.calculator.ui.values

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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