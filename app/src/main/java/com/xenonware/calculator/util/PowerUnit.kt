package com.xenonware.calculator.util

import android.content.Context
import androidx.annotation.StringRes
import com.xenonware.calculator.R

enum class PowerUnit(
    @param:StringRes val displayNameResId: Int,
    val toBaseFactor: Double
) {
    MILLIWATTS(R.string.power_milliwatts, 0.001),
    WATTS(R.string.power_watts, 1.0),
    KILOWATTS(R.string.power_kilowatts, 1000.0),
    MEGAWATTS(R.string.power_megawatts, 1e6),
    GIGAWATTS(R.string.power_gigawatts, 1e9),
    HORSEPOWER_MECHANICAL(R.string.power_horsepower_mechanical, 745.699872),
    HORSEPOWER_METRIC(R.string.power_horsepower_metric, 735.49875),
    BTU_PER_HOUR(R.string.power_btu_per_hour, 0.29307107),
    TONS_OF_REFRIGERATION(R.string.power_tons_of_refrigeration, 3516.85284),
    CALORIES_PER_SECOND(R.string.power_calories_per_second, 4.184),
    FOOT_POUNDS_PER_SECOND(R.string.power_foot_pounds_per_second, 1.35581795);

    fun getDisplayName(context: Context): String {
        return context.getString(displayNameResId)
    }

    fun fromBase(baseValue: Double): Double = baseValue / toBaseFactor
    fun toBase(value: Double): Double = value * toBaseFactor
}