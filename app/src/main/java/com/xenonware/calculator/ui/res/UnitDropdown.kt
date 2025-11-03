package com.xenonware.calculator.ui.res

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.xenonware.calculator.util.AreaUnit
import com.xenonware.calculator.util.ConverterType
import com.xenonware.calculator.util.CurrencyUnit
import com.xenonware.calculator.util.LengthUnit
import com.xenonware.calculator.util.SpeedUnit
import com.xenonware.calculator.util.TemperatureUnit
import com.xenonware.calculator.util.VolumeUnit
import com.xenonware.calculator.util.WeightUnit
import dev.chrisbanes.haze.HazeState

@Composable
fun UnitDropdown(
    label: String,
    selectedConverterType: ConverterType,

    selectedVolumeUnit: VolumeUnit,
    onVolumeUnitSelected: (VolumeUnit) -> Unit,

    selectedAreaUnit: AreaUnit,
    onAreaUnitSelected: (AreaUnit) -> Unit,

    selectedLengthUnit: LengthUnit,
    onLengthUnitSelected: (LengthUnit) -> Unit,

    selectedSpeedUnit: SpeedUnit,
    onSpeedUnitSelected: (SpeedUnit) -> Unit,

    selectedWeightUnit: WeightUnit,
    onWeightUnitSelected: (WeightUnit) -> Unit,

    selectedTemperatureUnit: TemperatureUnit,
    onTemperatureUnitSelected: (TemperatureUnit) -> Unit,

    selectedCurrencyUnit: CurrencyUnit,
    onCurrencyUnitSelected: (CurrencyUnit) -> Unit,

    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    when (selectedConverterType) {
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

        ConverterType.SPEED -> {
            val context = LocalContext.current
            GenericUnitDropdown(
                label,
                SpeedUnit.entries.toTypedArray(),
                selectedSpeedUnit,
                onSpeedUnitSelected,
                { speedUnit -> speedUnit.getDisplayName(context) },
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