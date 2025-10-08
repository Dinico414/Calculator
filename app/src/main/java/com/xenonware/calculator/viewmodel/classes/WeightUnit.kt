package com.xenonware.calculator.viewmodel.classes

import android.content.Context
import androidx.annotation.StringRes
import com.xenon.calculator.R

enum class WeightUnit(
    @StringRes val displayNameResId: Int,
    val toBaseFactor: Double
) {
    METRIC_TONNES(R.string.weight_metric_tonnes, 1000.0),
    KILOGRAMS(R.string.weight_kilograms, 1.0),
    GRAMS(R.string.weight_grams, 0.001),
    MILLIGRAMS(R.string.weight_milligrams, 1e-6),
    MICROGRAMS(R.string.weight_micrograms, 1e-9),
    NANOGRAMS(R.string.weight_nanograms, 1e-12),
    IMPERIAL_TONS(R.string.weight_imperial_tons, 1016.0469088),
    US_TONS(R.string.weight_us_tons, 907.18474),
    POUNDS(R.string.weight_pounds, 0.45359237),
    OUNCES(R.string.weight_ounces, 0.028349523125),
    GRAIN(R.string.weight_grain, 0.00006479891),
    CARATS(R.string.weight_carats, 0.0002),
    STONES(R.string.weight_stones, 6.35029318);

    fun getDisplayName(context: Context): String {
        return context.getString(displayNameResId)
    }

    fun fromBase(baseKilogramValue: Double): Double = baseKilogramValue / toBaseFactor
    fun toBase(valueInThisUnit: Double): Double = valueInThisUnit * toBaseFactor
}