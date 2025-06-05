package com.xenon.calculator.viewmodel.classes

enum class AreaUnit(
    val displayName: String,
    val toBaseFactor: Double
) {
    SQUARE_KILOMETERS("Square Kilometers", 1_000_000.0), //km^2
    SQUARE_DECAMETERS("Square Decameters", 100.0), //dam^2
    SQUARE_METERS("Square Meters", 1.0), //m^2
    SQUARE_DECIMETERS("Square Decimeters", 0.01), //dm^2
    SQUARE_CENTIMETERS("Square Centimeters", 0.000_1), //cm^2
    SQUARE_MILLIMETERS("Square Millimeters", 0.000_000_1), //mm^2
    SQUARE_MICROMETERS("Square Micrometers", 0.000_000_000_000_1), //µm^2
    SQUARE_NANOMETERS("Square Nanometers", 0.000_000_000_000_000_000_1), //nm
    SQUARE_MILES("Square Miles", 2_589_988.110336), //mi^2
    SQUARE_YARDS("Square Yards", 0.83612736), //yd^2
    SQUARE_FEET("Square Feet", 0.09290304), //ft^2
    SQUARE_INCHES("Square Inches", 0.00064516), //in^2
    HECTARES("Hectares", 10_000.0), //ha
    ACRES("Acres", 4046.8564224), //ac
    AR("Ar", 100_000.0); //Ar")

    fun fromBase(baseValue: Double): Double = baseValue / toBaseFactor
    fun toBase(value: Double): Double = value * toBaseFactor
}