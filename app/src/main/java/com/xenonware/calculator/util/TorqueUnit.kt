package com.xenonware.calculator.util

import android.content.Context
import androidx.annotation.StringRes
import com.xenonware.calculator.R

enum class TorqueUnit(
    @param:StringRes val displayNameResId: Int,
    val toBaseFactor: Double
) {
    MILLINEWTON_METERS(R.string.torque_millinewton_meters, 0.001),
    NEWTON_CENTIMETERS(R.string.torque_newton_centimeters, 0.01),
    NEWTON_METERS(R.string.torque_newton_meters, 1.0),
    KILONEWTON_METERS(R.string.torque_kilonewton_meters, 1000.0),
    INCH_POUNDS(R.string.torque_inch_pounds, 0.112984829),
    FOOT_POUNDS(R.string.torque_foot_pounds, 1.35581794833),
    KGF_METERS(R.string.torque_kgf_meters, 9.80665);

    fun getDisplayName(context: Context): String {
        return context.getString(displayNameResId)
    }

    fun fromBase(baseValue: Double): Double = baseValue / toBaseFactor
    fun toBase(value: Double): Double = value * toBaseFactor
}