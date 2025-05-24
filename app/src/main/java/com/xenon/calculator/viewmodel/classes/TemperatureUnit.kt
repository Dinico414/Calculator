package com.xenon.calculator.viewmodel.classes

enum class TemperatureUnit(val displayName: String) {
    CELSIUS("Celsius"),
    FAHRENHEIT("Fahrenheit"),
    KELVIN("Kelvin");
    // Conversions for temperature are not simple factors, so they'll be handled in ViewModel
}