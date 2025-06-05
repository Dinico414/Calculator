package com.xenon.calculator.viewmodel.classes

enum class AreaUnit(
    val displayName: String,
    val toBase: (Double) -> Double,
    val fromBase: (Double) -> Double
) {
    SQUARE_METERS("Square Meters", { it }, { it }),
    SQUARE_KILOMETERS("Square Kilometers", { it * 1_000_000.0 }, { it / 1_000_000.0 }),
    SQUARE_CENTIMETERS("Square Centimeters", { it / 10_000.0 }, { it * 10_000.0 }),
    SQUARE_MILLIMETERS("Square Millimeters", { it / 1_000_000.0 }, { it * 1_000_000.0 }),
    SQUARE_MILES("Square Miles", { it * 2_589_988.110336 }, { it / 2_589_988.110336 }),
    SQUARE_YARDS("Square Yards", { it * 0.83612736 }, { it / 0.83612736 }),
    SQUARE_FEET("Square Feet", { it * 0.09290304 }, { it / 0.09290304 }),
    SQUARE_INCHES("Square Inches", { it * 0.00064516 }, { it / 0.00064516 }),
    HECTARES("Hectares", { it * 10_000.0 }, { it / 10_000.0 }),
    ACRES("Acres", { it * 4046.8564224 }, { it / 4046.8564224 });
}