package com.xenon.calculator.viewmodel.classes

enum class VolumeUnit(val displayName: String, val toBaseFactor: Double) {
    MILLILITERS("Milliliters", 1.0),
    LITERS("Liters", 1000.0),
    GALLONS_US("US Gallons", 3785.41),
    CUBIC_METERS("Cubic Meters", 1_000_000.0);


    fun fromBase(baseValue: Double): Double = baseValue / toBaseFactor
    fun toBase(value: Double): Double = value * toBaseFactor
}