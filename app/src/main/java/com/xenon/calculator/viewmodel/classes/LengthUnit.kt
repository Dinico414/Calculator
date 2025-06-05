package com.xenon.calculator.viewmodel.classes

enum class LengthUnit(val displayName: String, val toBaseFactor: Double) {
    MILLIMETERS("Millimeters", 0.001),
    CENTIMETERS("Centimeters", 0.01),
    METERS("Meters", 1.0),
    KILOMETERS("Kilometers", 1000.0),
    INCHES("Inches", 0.0254),
    FEET("Feet", 0.3048),
    YARDS("Yards", 0.9144),
    MILES("Miles", 1609.34);


    fun fromBase(baseValue: Double): Double = baseValue / toBaseFactor
    fun toBase(value: Double): Double = value * toBaseFactor
}