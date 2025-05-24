package com.xenon.calculator.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.xenon.calculator.viewmodel.classes.ConverterType
import com.xenon.calculator.viewmodel.classes.CurrencyUnit
import com.xenon.calculator.viewmodel.classes.LengthUnit
import com.xenon.calculator.viewmodel.classes.TemperatureUnit
import com.xenon.calculator.viewmodel.classes.VolumeUnit
import java.text.DecimalFormat

class ConverterViewModel : ViewModel() {

    private val _selectedConverterType = mutableStateOf(ConverterType.LENGTH)
    val selectedConverterType: State<ConverterType> = _selectedConverterType

    private val _inputValue = mutableStateOf("")
    val inputValue: State<String> = _inputValue

    private val _outputValue = mutableStateOf("")
    val outputValue: State<String> = _outputValue

    // --- Volume Units ---
    private val _fromVolumeUnit = mutableStateOf(VolumeUnit.LITERS)
    val fromVolumeUnit: State<VolumeUnit> = _fromVolumeUnit

    private val _toVolumeUnit = mutableStateOf(VolumeUnit.MILLILITERS)
    val toVolumeUnit: State<VolumeUnit> = _toVolumeUnit

    // --- Length Units ---
    private val _fromLengthUnit = mutableStateOf(LengthUnit.METERS)
    val fromLengthUnit: State<LengthUnit> = _fromLengthUnit

    private val _toLengthUnit = mutableStateOf(LengthUnit.KILOMETERS)
    val toLengthUnit: State<LengthUnit> = _toLengthUnit

    // --- Temperature Units ---
    private val _fromTemperatureUnit = mutableStateOf(TemperatureUnit.CELSIUS)
    val fromTemperatureUnit: State<TemperatureUnit> = _fromTemperatureUnit

    private val _toTemperatureUnit = mutableStateOf(TemperatureUnit.FAHRENHEIT)
    val toTemperatureUnit: State<TemperatureUnit> = _toTemperatureUnit

    // --- Currency Units ---
    private val _fromCurrencyUnit = mutableStateOf(CurrencyUnit.USD)
    val fromCurrencyUnit: State<CurrencyUnit> = _fromCurrencyUnit

    private val _toCurrencyUnit = mutableStateOf(CurrencyUnit.EUR)
    val toCurrencyUnit: State<CurrencyUnit> = _toCurrencyUnit

    // Placeholder for currency rates (relative to USD for simplicity)
    // In a real app, fetch these from an API
    private val currencyRates = mapOf(
        CurrencyUnit.USD to 1.0,
        CurrencyUnit.EUR to 0.92, // Example rate: 1 USD = 0.92 EUR
        CurrencyUnit.GBP to 0.79, // Example rate: 1 USD = 0.79 GBP
        CurrencyUnit.JPY to 150.0, // Example rate: 1 USD = 150 JPY
        CurrencyUnit.INR to 83.0  // Example rate: 1 USD = 83 INR
    )

    private val decimalFormat = DecimalFormat("#.####") // Format output to 4 decimal places

    init {
        // Initial conversion if needed, or when values change
        convert()
    }

    fun onConverterTypeChange(newType: ConverterType) {
        _selectedConverterType.value = newType
        // Optionally reset units to defaults for the new type
        // e.g., _fromLengthUnit.value = LengthUnit.METERS etc.
        convert() // Recalculate with new type
    }

    fun onInputValueChange(newValue: String) {
        _inputValue.value = newValue
        convert()
    }

    // --- Unit Change Handlers ---
    fun onFromVolumeUnitChange(newUnit: VolumeUnit) {
        _fromVolumeUnit.value = newUnit
        convert()
    }
    fun onToVolumeUnitChange(newUnit: VolumeUnit) {
        _toVolumeUnit.value = newUnit
        convert()
    }
    fun onFromLengthUnitChange(newUnit: LengthUnit) {
        _fromLengthUnit.value = newUnit
        convert()
    }
    fun onToLengthUnitChange(newUnit: LengthUnit) {
        _toLengthUnit.value = newUnit
        convert()
    }
    fun onFromTemperatureUnitChange(newUnit: TemperatureUnit) {
        _fromTemperatureUnit.value = newUnit
        convert()
    }
    fun onToTemperatureUnitChange(newUnit: TemperatureUnit) {
        _toTemperatureUnit.value = newUnit
        convert()
    }
    fun onFromCurrencyUnitChange(newUnit: CurrencyUnit) {
        _fromCurrencyUnit.value = newUnit
        convert()
    }
    fun onToCurrencyUnitChange(newUnit: CurrencyUnit) {
        _toCurrencyUnit.value = newUnit
        convert()
    }


    fun swapUnits() {
        when (selectedConverterType.value) {
            ConverterType.VOLUME -> {
                val temp = _fromVolumeUnit.value
                _fromVolumeUnit.value = _toVolumeUnit.value
                _toVolumeUnit.value = temp
            }
            ConverterType.LENGTH -> {
                val temp = _fromLengthUnit.value
                _fromLengthUnit.value = _toLengthUnit.value
                _toLengthUnit.value = temp
            }
            ConverterType.TEMPERATURE -> {
                val temp = _fromTemperatureUnit.value
                _fromTemperatureUnit.value = _toTemperatureUnit.value
                _toTemperatureUnit.value = temp
            }
            ConverterType.CURRENCY -> {
                val temp = _fromCurrencyUnit.value
                _fromCurrencyUnit.value = _toCurrencyUnit.value
                _toCurrencyUnit.value = temp
            }
        }
        convert() // Recalculate after swapping
    }

    private fun convert() {
        val inputDouble = inputValue.value.toDoubleOrNull()
        if (inputDouble == null) {
            _outputValue.value = "" // Or "Invalid Input"
            return
        }

        val result = when (selectedConverterType.value) {
            ConverterType.VOLUME -> {
                val baseValue = fromVolumeUnit.value.toBase(inputDouble) // Convert input to base unit (e.g., mL)
                toVolumeUnit.value.fromBase(baseValue) // Convert from base unit to target unit
            }
            ConverterType.LENGTH -> {
                val baseValue = fromLengthUnit.value.toBase(inputDouble) // Convert input to base unit (e.g., meters)
                toLengthUnit.value.fromBase(baseValue) // Convert from base unit to target unit
            }
            ConverterType.TEMPERATURE -> {
                convertTemperature(inputDouble, fromTemperatureUnit.value, toTemperatureUnit.value)
            }
            ConverterType.CURRENCY -> {
                convertCurrency(inputDouble, fromCurrencyUnit.value, toCurrencyUnit.value)
            }
        }
        _outputValue.value = decimalFormat.format(result)
    }

    private fun convertTemperature(value: Double, from: TemperatureUnit, to: TemperatureUnit): Double {
        if (from == to) return value

        // Convert input to Celsius first (base)
        val celsiusValue = when (from) {
            TemperatureUnit.CELSIUS -> value
            TemperatureUnit.FAHRENHEIT -> (value - 32) * 5.0 / 9.0
            TemperatureUnit.KELVIN -> value - 273.15
        }

        // Convert Celsius to target unit
        return when (to) {
            TemperatureUnit.CELSIUS -> celsiusValue
            TemperatureUnit.FAHRENHEIT -> (celsiusValue * 9.0 / 5.0) + 32
            TemperatureUnit.KELVIN -> celsiusValue + 273.15
        }
    }

    private fun convertCurrency(value: Double, from: CurrencyUnit, to: CurrencyUnit): Double {
        if (from == to) return value

        val rateFrom = currencyRates[from] ?: 1.0 // Default to 1.0 if rate not found
        val rateTo = currencyRates[to] ?: 1.0   // Default to 1.0 if rate not found

        // Convert 'from' currency to USD (our base currency in this example)
        val valueInUsd = value / rateFrom // If 'from' is USD, rateFrom is 1.0. If 'from' is EUR, this converts EUR to USD.

        // Convert USD to 'to' currency
        return valueInUsd * rateTo // If 'to' is USD, rateTo is 1.0. If 'to' is EUR, this converts USD to EUR.
    }
}