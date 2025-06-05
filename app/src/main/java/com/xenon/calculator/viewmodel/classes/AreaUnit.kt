package com.xenon.calculator.viewmodel.classes

import androidx.annotation.StringRes
import com.xenon.calculator.R

enum class AreaUnit(
    @StringRes val displayNameResId: Int,
    val toBaseFactor: Double
) {
    SQUARE_KILOMETERS(R.string.area_square_kilometers, 1_000_000.0),
    SQUARE_DECAMETERS(R.string.area_square_decameters, 100.0),
    SQUARE_METERS(R.string.area_square_meters, 1.0),
    SQUARE_DECIMETERS(R.string.area_square_decimeters, 0.01),
    SQUARE_CENTIMETERS(R.string.area_square_centimeters, 0.000_1),
    SQUARE_MILLIMETERS(R.string.area_square_millimeters, 0.000_000_1),
    SQUARE_MICROMETERS(R.string.area_square_micrometers, 0.000_000_000_000_1),
    SQUARE_NANOMETERS(R.string.area_square_nanometers, 0.000_000_000_000_000_000_1),
    SQUARE_MILES(R.string.area_square_miles, 2_589_988.110336),
    SQUARE_YARDS(R.string.area_square_yards, 0.83612736),
    SQUARE_FEET(R.string.area_square_feet, 0.09290304),
    SQUARE_INCHES(R.string.area_square_inches, 0.00064516),
    HECTARES(R.string.area_hectares, 10_000.0),
    ACRES(R.string.area_acres, 4046.8564224),
    AR(R.string.area_ar, 100_000.0);

    fun getDisplayName(context: android.content.Context): String {
        return context.getString(displayNameResId)
    }

    fun fromBase(baseValue: Double): Double = baseValue / toBaseFactor
    fun toBase(value: Double): Double = value * toBaseFactor
}