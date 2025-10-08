package com.xenonware.calculator.viewmodel.classes

import android.content.Context
import androidx.annotation.StringRes
import com.xenon.calculator.R

enum class SpeedUnit(
    @StringRes val displayNameResId: Int,
    val toBaseFactor: Double
) {
    METERS_PER_SECOND(R.string.speed_meters_per_second, 1.0),
    METERS_PER_MINUTE(R.string.speed_meters_per_minute, 0.016666666666666666),
    METERS_PER_HOUR(R.string.speed_meters_per_hour, 0.002777777777777778),
    KILOMETERS_PER_SECOND(R.string.speed_kilometers_per_second, 1000.0),
    KILOMETERS_PER_MINUTE(R.string.speed_kilometers_per_minute, 0.6666666666666666),
    KILOMETERS_PER_HOUR(R.string.speed_kilometers_per_hour, 0.2777777777777778),
    MILES_PER_HOUR(R.string.speed_miles_per_hour, 0.44704),
    FEET_PER_SECOND(R.string.speed_feet_per_second, 0.3048),
    INCHES_PER_SECOND(R.string.speed_inches_per_second, 0.0254),
    YARDS_PER_HOUR(R.string.speed_yards_per_hour, 0.000254),
    KNOTS(R.string.speed_knots, 0.5144444444444444),
    MACH(R.string.speed_mach, 343.0),
    LIGHT_SPEED(R.string.speed_light_speed, 299792458.0);


    fun getDisplayName(context: Context): String {
        return context.getString(displayNameResId)
    }

    fun fromBase(baseMetersPerSecondValue: Double): Double = baseMetersPerSecondValue / toBaseFactor
    fun toBase(valueInThisUnit: Double): Double = valueInThisUnit * toBaseFactor
}