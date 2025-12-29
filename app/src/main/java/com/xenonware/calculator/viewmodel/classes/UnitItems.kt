package com.xenonware.calculator.viewmodel.classes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.xenonware.calculator.ui.res.GenericUnitDropdown
import com.xenonware.calculator.util.AreaUnit
import com.xenonware.calculator.util.ConverterType
import com.xenonware.calculator.util.CurrencyUnit
import com.xenonware.calculator.util.EnergyUnit
import com.xenonware.calculator.util.LengthUnit
import com.xenonware.calculator.util.PowerUnit
import com.xenonware.calculator.util.SpeedUnit
import com.xenonware.calculator.util.TemperatureUnit
import com.xenonware.calculator.util.TorqueUnit
import com.xenonware.calculator.util.VolumeUnit
import com.xenonware.calculator.util.WeightUnit
import dev.chrisbanes.haze.HazeState

@Composable
fun UnitItems(
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

    selectedPowerUnit: PowerUnit,
    onPowerUnitSelected: (PowerUnit) -> Unit,

    selectedEnergyUnit: EnergyUnit,
    onEnergyUnitSelected: (EnergyUnit) -> Unit,

    selectedTorqueUnit: TorqueUnit,
    onTorqueUnitSelected: (TorqueUnit) -> Unit,


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

        ConverterType.POWER -> {
            val context = LocalContext.current
            GenericUnitDropdown(
                label,
                PowerUnit.entries.toTypedArray(),
                selectedPowerUnit,
                onPowerUnitSelected,
                { powerUnit -> powerUnit.getDisplayName(context) },
                hazeState,
                modifier
            )
        }

        ConverterType.ENERGY -> {
            val context = LocalContext.current
            GenericUnitDropdown(
                label,
                EnergyUnit.entries.toTypedArray(),
                selectedEnergyUnit,
                onEnergyUnitSelected,
                { energyUnit -> energyUnit.getDisplayName(context) },
                hazeState,
                modifier
            )
        }

        ConverterType.TORQUE -> {
            val context = LocalContext.current
            GenericUnitDropdown(
                label,
                TorqueUnit.entries.toTypedArray(),
                selectedTorqueUnit,
                onTorqueUnitSelected,
                { torqueUnit -> torqueUnit.getDisplayName(context) },
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