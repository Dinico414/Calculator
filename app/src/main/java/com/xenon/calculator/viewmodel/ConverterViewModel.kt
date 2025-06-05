package com.xenon.calculator.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
// import androidx.lifecycle.ViewModel // Not needed if using AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.xenon.calculator.viewmodel.classes.AreaUnit
import com.xenon.calculator.viewmodel.classes.ConverterType
import com.xenon.calculator.viewmodel.classes.CurrencyUnit
import com.xenon.calculator.viewmodel.classes.LengthUnit
import com.xenon.calculator.viewmodel.classes.TemperatureUnit
import com.xenon.calculator.viewmodel.classes.VolumeUnit
import com.xenon.calculator.viewmodel.classes.WeightUnit
import java.text.DecimalFormat

class ConverterViewModel(application: Application) : AndroidViewModel(application) {

    enum class EditedField { FIELD1, FIELD2 }

    private var lastEditedField: EditedField = EditedField.FIELD1

    private val _selectedConverterType = mutableStateOf(ConverterType.LENGTH)
    val selectedConverterType: State<ConverterType> = _selectedConverterType

    private val _value1 = mutableStateOf("")
    val value1: State<String> = _value1

    private val _value2 = mutableStateOf("")
    val value2: State<String> = _value2

    private val _fromVolumeUnit = mutableStateOf(VolumeUnit.LITERS)
    val fromVolumeUnit: State<VolumeUnit> = _fromVolumeUnit
    private val _toVolumeUnit = mutableStateOf(VolumeUnit.MILLILITERS)
    val toVolumeUnit: State<VolumeUnit> = _toVolumeUnit

    private val _fromLengthUnit = mutableStateOf(LengthUnit.METERS)
    val fromLengthUnit: State<LengthUnit> = _fromLengthUnit
    private val _toLengthUnit = mutableStateOf(LengthUnit.KILOMETERS)
    val toLengthUnit: State<LengthUnit> = _toLengthUnit

    private val _fromTemperatureUnit = mutableStateOf(TemperatureUnit.CELSIUS)
    val fromTemperatureUnit: State<TemperatureUnit> = _fromTemperatureUnit
    private val _toTemperatureUnit = mutableStateOf(TemperatureUnit.FAHRENHEIT)
    val toTemperatureUnit: State<TemperatureUnit> = _toTemperatureUnit

    private val _fromCurrencyUnit = mutableStateOf(CurrencyUnit.USD)
    val fromCurrencyUnit: State<CurrencyUnit> = _fromCurrencyUnit
    private val _toCurrencyUnit = mutableStateOf(CurrencyUnit.EUR)
    val toCurrencyUnit: State<CurrencyUnit> = _toCurrencyUnit

    private val _fromAreaUnit = mutableStateOf(AreaUnit.SQUARE_METERS)
    val fromAreaUnit: State<AreaUnit> = _fromAreaUnit
    private val _toAreaUnit = mutableStateOf(AreaUnit.SQUARE_KILOMETERS)
    val toAreaUnit: State<AreaUnit> = _toAreaUnit

    private val _fromWeightUnit = mutableStateOf(WeightUnit.KILOGRAMS)
    val fromWeightUnit: State<WeightUnit> = _fromWeightUnit
    private val _toWeightUnit = mutableStateOf(WeightUnit.POUNDS)
    val toWeightUnit: State<WeightUnit> = _toWeightUnit


    private val decimalFormat = DecimalFormat("#.######")

    fun onConverterTypeChange(newType: ConverterType) {
        _selectedConverterType.value = newType
        recalculateOnTypeOrUnitChange()
    }

    fun onValueChanged(newValue: String, changedField: EditedField) {
        lastEditedField = changedField
        val inputDouble = newValue.toDoubleOrNull()

        if (changedField == EditedField.FIELD1) {
            _value1.value = newValue
            if (inputDouble == null) {
                _value2.value = ""
                return
            }
            val result = performConversion(inputDouble, getCurrentFromUnit(), getCurrentToUnit())
            _value2.value = if (result != null) decimalFormat.format(result) else ""
        } else {
            _value2.value = newValue
            if (inputDouble == null) {
                _value1.value = ""
                return
            }

            val result = performConversion(inputDouble, getCurrentToUnit(), getCurrentFromUnit())
            _value1.value = if (result != null) decimalFormat.format(result) else ""
        }
    }

    private fun performConversion(value: Double, fromUnit: Any, toUnit: Any): Double? {
        if (fromUnit == toUnit) return value

        return when (_selectedConverterType.value) {
            ConverterType.VOLUME -> {
                if (fromUnit is VolumeUnit && toUnit is VolumeUnit) {
                    val baseValue = fromUnit.toBase(value)
                    toUnit.fromBase(baseValue)
                } else null
            }

            ConverterType.LENGTH -> {
                if (fromUnit is LengthUnit && toUnit is LengthUnit) {
                    val baseValue = fromUnit.toBase(value)
                    toUnit.fromBase(baseValue)
                } else null
            }

            ConverterType.TEMPERATURE -> {
                if (fromUnit is TemperatureUnit && toUnit is TemperatureUnit) {
                    convertTemperatureInternal(
                        value, fromUnit, toUnit
                    )
                } else null
            }

            ConverterType.CURRENCY -> {

                if (fromUnit is CurrencyUnit && toUnit is CurrencyUnit) {
                    val baseValue = fromUnit.toBase(value)
                    toUnit.fromBase(baseValue)
                } else null
            }

            ConverterType.AREA -> {
                if (fromUnit is AreaUnit && toUnit is AreaUnit) {
                    val baseValue = fromUnit.toBase(value)
                    toUnit.fromBase(baseValue)
                } else null
            }

            ConverterType.WEIGHT -> {
                if (fromUnit is WeightUnit && toUnit is WeightUnit) {
                    val baseValue = fromUnit.toBase(value)
                    toUnit.fromBase(baseValue)
                } else null
            }
        }
    }


    private fun convertTemperatureInternal(
        value: Double, from: TemperatureUnit, to: TemperatureUnit
    ): Double {

        if (from == to) return value
        val celsiusValue = when (from) {
            TemperatureUnit.CELSIUS -> value
            TemperatureUnit.FAHRENHEIT -> (value - 32) * 5.0 / 9.0
            TemperatureUnit.KELVIN -> value - 273.15
        }
        return when (to) {
            TemperatureUnit.CELSIUS -> celsiusValue
            TemperatureUnit.FAHRENHEIT -> (celsiusValue * 9.0 / 5.0) + 32
            TemperatureUnit.KELVIN -> celsiusValue + 273.15
        }
    }


    fun swapUnits() {
        when (_selectedConverterType.value) {
            ConverterType.VOLUME -> {
                val temp = _fromVolumeUnit.value; _fromVolumeUnit.value =
                    _toVolumeUnit.value; _toVolumeUnit.value = temp
            }

            ConverterType.LENGTH -> {
                val temp = _fromLengthUnit.value; _fromLengthUnit.value =
                    _toLengthUnit.value; _toLengthUnit.value = temp
            }

            ConverterType.TEMPERATURE -> {
                val temp = _fromTemperatureUnit.value; _fromTemperatureUnit.value =
                    _toTemperatureUnit.value; _toTemperatureUnit.value = temp
            }

            ConverterType.CURRENCY -> {
                val temp = _fromCurrencyUnit.value; _fromCurrencyUnit.value =
                    _toCurrencyUnit.value; _toCurrencyUnit.value = temp
            }

            ConverterType.AREA -> {
                val temp = _fromAreaUnit.value; _fromAreaUnit.value =
                    _toAreaUnit.value; _toAreaUnit.value = temp
            }

            ConverterType.WEIGHT -> {
                val temp = _fromWeightUnit.value; _fromWeightUnit.value =
                    _toWeightUnit.value; _toWeightUnit.value = temp
            }
        }

        // Swap the displayed values
        val tempValue = _value1.value
        _value1.value = _value2.value
        _value2.value = tempValue

        // After swapping units and values, recalculate based on the new state.
        // The last edited field determines which value is the source of truth.
        if (lastEditedField == EditedField.FIELD1) {
            // Value1 was the source, now it's in value2 field with the new "toUnit"
            // So we need to re-calculate value1 based on value2 (which has the original value1's number)
            val inputDouble = _value2.value.toDoubleOrNull()
            if (inputDouble != null) {
                // Convert from the new "toUnit" (which is the old "fromUnit") back to the new "fromUnit" (which is the old "toUnit")
                val result =
                    performConversion(inputDouble, getCurrentToUnit(), getCurrentFromUnit())
                _value1.value = if (result != null) decimalFormat.format(result) else ""
            } else if (_value2.value.isEmpty()) {
                _value1.value = ""
            }
        } else { // lastEditedField == EditedField.FIELD2
            // Value2 was the source, now it's in value1 field with the new "fromUnit"
            // So we need to re-calculate value2 based on value1
            val inputDouble = _value1.value.toDoubleOrNull()
            if (inputDouble != null) {
                val result =
                    performConversion(inputDouble, getCurrentFromUnit(), getCurrentToUnit())
                _value2.value = if (result != null) decimalFormat.format(result) else ""
            } else if (_value1.value.isEmpty()) {
                _value2.value = ""
            }
        }
    }


    // Consolidate unit change handlers
    fun onFromUnitChange(newUnit: Any) {
        when (newUnit) {
            is VolumeUnit -> _fromVolumeUnit.value = newUnit
            is LengthUnit -> _fromLengthUnit.value = newUnit
            is TemperatureUnit -> _fromTemperatureUnit.value = newUnit
            is CurrencyUnit -> _fromCurrencyUnit.value = newUnit
            is AreaUnit -> _fromAreaUnit.value = newUnit
            is WeightUnit -> _fromWeightUnit.value = newUnit
        }
        lastEditedField =
            EditedField.FIELD2 // When "from" unit changes, "value2" is derived from "value1"
        recalculateOnTypeOrUnitChange()
    }

    fun onToUnitChange(newUnit: Any) {
        when (newUnit) {
            is VolumeUnit -> _toVolumeUnit.value = newUnit
            is LengthUnit -> _toLengthUnit.value = newUnit
            is TemperatureUnit -> _toTemperatureUnit.value = newUnit
            is CurrencyUnit -> _toCurrencyUnit.value = newUnit
            is AreaUnit -> _toAreaUnit.value = newUnit
            is WeightUnit -> _toWeightUnit.value = newUnit
        }
        lastEditedField =
            EditedField.FIELD1 // When "to" unit changes, "value1" is derived from "value2"
        recalculateOnTypeOrUnitChange()
    }


    // This function can be removed or simplified if `swapUnits` is the only way to switch.
    // The `swapUnits` function already handles recalculation correctly.
    // fun onUnitsSwitch() { ... } // Consider if this is still needed or if swapUnits is enough.
    // If you keep it, ensure it calls recalculateOnTypeOrUnitChange or has correct logic.
    // For now, I'll assume `swapUnits` is the primary method for this.
    // If `onUnitsSwitch` is called from UI for a generic switch button, it should call `swapUnits`.

    /**
     * Recalculates conversions when converter type or a unit changes.
     * It respects the last edited field to determine the source of truth for conversion.
     */
    private fun recalculateOnTypeOrUnitChange() {
        if (lastEditedField == EditedField.FIELD1) {
            // Value1 is the source of truth, recalculate value2
            val inputDouble = _value1.value.toDoubleOrNull()
            if (inputDouble != null) {
                val result =
                    performConversion(inputDouble, getCurrentFromUnit(), getCurrentToUnit())
                _value2.value = if (result != null) decimalFormat.format(result) else ""
            } else {
                _value2.value = "" // Clear if source is invalid
            }
        } else { // lastEditedField == EditedField.FIELD2
            // Value2 is the source of truth, recalculate value1
            val inputDouble = _value2.value.toDoubleOrNull()
            if (inputDouble != null) {
                // Convert from "toUnit" back to "fromUnit"
                val result =
                    performConversion(inputDouble, getCurrentToUnit(), getCurrentFromUnit())
                _value1.value = if (result != null) decimalFormat.format(result) else ""
            } else {
                _value1.value = "" // Clear if source is invalid
            }
        }
    }


    // The old recalculateForUnitChange might need adjustment or replacement
    // by the more general recalculateOnTypeOrUnitChange
    /*
    private fun recalculateForUnitChange(fieldWhoseUnitChanged: EditedField) {
        // This logic is now subtly different because the "source" value matters.
        // Let's use the new recalculateOnTypeOrUnitChange,
        // but ensure lastEditedField is set correctly before calling.

        // If fieldWhoseUnitChanged is FIELD1 (i.e. fromUnit changed),
        // we want to recalculate value1 based on value2.
        // So, value2 becomes the source of truth.
        if (fieldWhoseUnitChanged == EditedField.FIELD1) {
            val value2Double = _value2.value.toDoubleOrNull()
            if (value2Double != null) {
                val result = performConversion(value2Double, getCurrentToUnit(), getCurrentFromUnit())
                _value1.value = if (result != null) decimalFormat.format(result) else ""
            } else {
                 // If value2 is empty, and fromUnit changes, value1 should also be empty
                 // or reflect that no conversion is possible.
                _value1.value = ""
            }
        } else { // fieldWhoseUnitChanged is FIELD2 (i.e. toUnit changed)
            // We want to recalculate value2 based on value1.
            // So, value1 becomes the source of truth.
            val value1Double = _value1.value.toDoubleOrNull()
            if (value1Double != null) {
                val result = performConversion(value1Double, getCurrentFromUnit(), getCurrentToUnit())
                _value2.value = if (result != null) decimalFormat.format(result) else ""
            } else {
                _value2.value = ""
            }
        }
    }
    */

    // Simplified unit change handlers:
    fun onFromVolumeUnitChange(newUnit: VolumeUnit) {
        _fromVolumeUnit.value = newUnit; lastEditedField =
            EditedField.FIELD2; recalculateOnTypeOrUnitChange()
    }

    fun onToVolumeUnitChange(newUnit: VolumeUnit) {
        _toVolumeUnit.value = newUnit; lastEditedField =
            EditedField.FIELD1; recalculateOnTypeOrUnitChange()
    }

    fun onFromLengthUnitChange(newUnit: LengthUnit) {
        _fromLengthUnit.value = newUnit; lastEditedField =
            EditedField.FIELD2; recalculateOnTypeOrUnitChange()
    }

    fun onToLengthUnitChange(newUnit: LengthUnit) {
        _toLengthUnit.value = newUnit; lastEditedField =
            EditedField.FIELD1; recalculateOnTypeOrUnitChange()
    }

    fun onFromTemperatureUnitChange(newUnit: TemperatureUnit) {
        _fromTemperatureUnit.value = newUnit; lastEditedField =
            EditedField.FIELD2; recalculateOnTypeOrUnitChange()
    }

    fun onToTemperatureUnitChange(newUnit: TemperatureUnit) {
        _toTemperatureUnit.value = newUnit; lastEditedField =
            EditedField.FIELD1; recalculateOnTypeOrUnitChange()
    }

    fun onFromCurrencyUnitChange(newUnit: CurrencyUnit) {
        _fromCurrencyUnit.value = newUnit; lastEditedField =
            EditedField.FIELD2; recalculateOnTypeOrUnitChange()
    }

    fun onToCurrencyUnitChange(newUnit: CurrencyUnit) {
        _toCurrencyUnit.value = newUnit; lastEditedField =
            EditedField.FIELD1; recalculateOnTypeOrUnitChange()
    }

    fun onFromAreaUnitChange(newUnit: AreaUnit) {
        _fromAreaUnit.value = newUnit; lastEditedField =
            EditedField.FIELD2; recalculateOnTypeOrUnitChange()
    }

    fun onToAreaUnitChange(newUnit: AreaUnit) {
        _toAreaUnit.value = newUnit; lastEditedField =
            EditedField.FIELD1; recalculateOnTypeOrUnitChange()
    }

    fun onFromWeightUnitChange(newUnit: WeightUnit) {
        _fromWeightUnit.value = newUnit; lastEditedField =
            EditedField.FIELD2; recalculateOnTypeOrUnitChange()
    }

    fun onToWeightUnitChange(newUnit: WeightUnit) {
        _toWeightUnit.value = newUnit; lastEditedField =
            EditedField.FIELD1; recalculateOnTypeOrUnitChange()
    }


    // `onUnitsSwitch` seems to be a duplicate of `swapUnits`.
    // Let's remove `onUnitsSwitch` and ensure `swapUnits` is used.
    // If you had a specific reason for `onUnitsSwitch`, please clarify.
    // fun onUnitsSwitch() { ... } // Removed, use swapUnits


    private fun getCurrentFromUnit(): Any {
        return when (_selectedConverterType.value) {
            ConverterType.VOLUME -> _fromVolumeUnit.value
            ConverterType.LENGTH -> _fromLengthUnit.value
            ConverterType.TEMPERATURE -> _fromTemperatureUnit.value
            ConverterType.CURRENCY -> _fromCurrencyUnit.value
            ConverterType.AREA -> _fromAreaUnit.value
            ConverterType.WEIGHT -> _fromWeightUnit.value
        }
    }

    private fun getCurrentToUnit(): Any {
        return when (_selectedConverterType.value) {
            ConverterType.VOLUME -> _toVolumeUnit.value
            ConverterType.LENGTH -> _toLengthUnit.value
            ConverterType.TEMPERATURE -> _toTemperatureUnit.value
            ConverterType.CURRENCY -> _toCurrencyUnit.value
            ConverterType.AREA -> _toAreaUnit.value
            ConverterType.WEIGHT -> _toWeightUnit.value
        }
    }

    class ConverterViewModelFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConverterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return ConverterViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}