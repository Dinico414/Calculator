package com.xenonware.calculator.util

import android.content.Context
import androidx.annotation.StringRes
import com.xenonware.calculator.R

enum class EnergyUnit(
    @param:StringRes val displayNameResId: Int,
    val toBaseFactor: Double
) {
    ELECTRONVOLTS(R.string.energy_electronvolts, 1.602176634e-19),
    JOULES(R.string.energy_joules, 1.0),
    KILOJOULES(R.string.energy_kilojoules, 1000.0),
    MEGAJOULES(R.string.energy_megajoules, 1e6),
    GIGAJOULES(R.string.energy_gigajoules, 1e9),
    CALORIES(R.string.energy_calories, 4.184),
    KILOCALORIES(R.string.energy_kilocalories, 4184.0),
    WATTHOURS(R.string.energy_watthours, 3600.0),
    KILOWATTHOURS(R.string.energy_kilowatthours, 3.6e6),
    BTU(R.string.energy_btu, 1055.05585262),
    FOOT_POUNDS(R.string.energy_foot_pounds, 1.35581794833);

    fun getDisplayName(context: Context): String {
        return context.getString(displayNameResId)
    }

    fun fromBase(baseValue: Double): Double = baseValue / toBaseFactor
    fun toBase(value: Double): Double = value * toBaseFactor
}