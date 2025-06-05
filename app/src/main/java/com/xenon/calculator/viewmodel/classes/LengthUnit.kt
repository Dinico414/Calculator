package com.xenon.calculator.viewmodel.classes

enum class LengthUnit(
    val displayName: String,
    val toBaseFactor: Double) {
    NANOMETERS("Nanometers", 1e-9), //nm
    MICROMETERS("Micrometers", 1e-6), //µm
    MILLIMETERS("Millimeters", 0.001), //mm
    CENTIMETERS("Centimeters", 0.01), //cm
    DECIMETERS("Decimeters", 1.0), //dm
    METERS("Meters", 1.0), //m
    DECAMETERS("Decameters", 10.0), //dam
    HECTOMETERS("Hectometers", 100.0), //hm
    KILOMETERS("Kilometers", 1000.0), //km
    INCHES("Inches", 0.0254), //in
    FEET("Feet", 0.3048), //ft
    YARDS("Yards", 0.9144), //yd
    MILES("Miles", 1609.34), //mi
    SEAMILES("Sea Miles", 1093.61), //sm
    NAUTICAL_MILES("Nautical Miles", 1852.0), //nmi
    LIGHT_YEARS("Light Years", 9.461e15), //ly
    PARSEC("Parsecs", 3.086e16); //pc

    fun fromBase(baseValue: Double): Double = baseValue / toBaseFactor
    fun toBase(value: Double): Double = value * toBaseFactor
}