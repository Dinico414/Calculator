package com.xenonware.calculator.util

import android.content.Context
import androidx.annotation.StringRes
import com.xenonware.calculator.R

enum class VolumeUnit(
    @param:StringRes val displayNameResId: Int,
    val toBaseFactor: Double
) {
    MILLILITERS(R.string.volume_milliliters, 1.0),
    LITERS(R.string.volume_liters, 1000.0),
    CUBIC_METERS(R.string.volume_cubic_meters, 1_000_000.0),
    CUBIC_DECIMETERS(R.string.volume_cubic_decimeters, 1_000.0),
    CUBIC_CENTIMETERS(R.string.volume_cubic_centimeters, 1.0),
    CUBIC_MILLIMETERS(R.string.volume_cubic_millimeters, 0.001),
    CUBIC_INCHES(R.string.volume_cubic_inches, 16.387064),
    CUBIC_FEET(R.string.volume_cubic_feet, 28316.846592),
    GALLONS_US(R.string.volume_gallons_us, 3785.41),
    GALLONS_UK(R.string.volume_gallons_uk, 4546.09),
    QUART_US(R.string.volume_quart_us, 946.353),
    QUART_UK(R.string.volume_quart_uk, 1136.52),
    TEASPOON_US(R.string.volume_teaspoon_us, 4.92892),
    TEASPOON_UK(R.string.volume_teaspoon_uk, 5.91939),
    TABLESPOON_US(R.string.volume_tablespoon_us, 14.7868),
    TABLESPOON_UK(R.string.volume_tablespoon_uk, 17.7582),
    PINTS_US(R.string.volume_pints_us, 473.176),
    PINTS_UK(R.string.volume_pints_uk, 568.261),
    CUP_US(R.string.volume_cup_us, 236.588),
    CUP_UK(R.string.volume_cup_uk, 284.131),
    FLUID_OUNCE_US(R.string.volume_fluid_ounce_us, 29.5735),
    FLUID_OUNCE_UK(R.string.volume_fluid_ounce_uk, 28.4131);

    fun getDisplayName(context: Context): String {
        return context.getString(displayNameResId)
    }

    fun fromBase(baseValue: Double): Double = baseValue / toBaseFactor
    fun toBase(value: Double): Double = value * toBaseFactor
}