package com.xenonware.calculator.util

import androidx.annotation.StringRes
import com.xenonware.calculator.R

enum class ConverterType(@param:StringRes val displayNameResId: Int) {
    VOLUME(R.string.converter_type_volume),
    AREA(R.string.converter_type_area),
    LENGTH(R.string.converter_type_length),
    SPEED(R.string.converter_type_speed),
    WEIGHT(R.string.converter_type_weight),
    TEMPERATURE(R.string.converter_type_temperature),
    CURRENCY(R.string.converter_type_currency),
}