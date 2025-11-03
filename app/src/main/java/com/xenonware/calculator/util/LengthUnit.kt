package com.xenonware.calculator.util

import android.content.Context
import androidx.annotation.StringRes
import com.xenonware.calculator.R

enum class LengthUnit(
    @StringRes val displayNameResId: Int,
    val toBaseFactor: Double
) {
    NANOMETERS(R.string.length_nanometers, 1e-9),
    MICROMETERS(R.string.length_micrometers, 1e-6),
    MILLIMETERS(R.string.length_millimeters, 0.001),
    CENTIMETERS(R.string.length_centimeters, 0.01),
    DECIMETERS(R.string.length_decimeters, 0.1),
    METERS(R.string.length_meters, 1.0),
    DECAMETERS(R.string.length_decameters, 10.0),
    HECTOMETERS(R.string.length_hectometers, 100.0),
    KILOMETERS(R.string.length_kilometers, 1000.0),
    INCHES(R.string.length_inches, 0.0254),
    FEET(R.string.length_feet, 0.3048),
    YARDS(R.string.length_yards, 0.9144),
    MILES(R.string.length_miles, 1609.34),
    SEAMILES(R.string.length_seamiles, 1093.61),
    NAUTICAL_MILES(R.string.length_nautical_miles, 1852.0),
    LIGHT_YEARS(R.string.length_light_years, 9.461e15),
    PARSEC(R.string.length_parsecs, 3.086e16);

    fun getDisplayName(context: Context): String {
        return context.getString(displayNameResId)
    }

    fun fromBase(baseValue: Double): Double = baseValue / toBaseFactor
    fun toBase(value: Double): Double = value * toBaseFactor
}