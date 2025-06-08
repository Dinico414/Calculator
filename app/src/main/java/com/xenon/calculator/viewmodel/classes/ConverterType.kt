package com.xenon.calculator.viewmodel.classes

import androidx.annotation.StringRes
import com.xenon.calculator.R

enum class ConverterType(@StringRes val displayNameResId: Int) {
    VOLUME(R.string.converter_type_volume),
    AREA(R.string.converter_type_area),
    LENGTH(R.string.converter_type_length),
    SPEED(R.string.converter_type_speed),
    WEIGHT(R.string.converter_type_weight),
    TEMPERATURE(R.string.converter_type_temperature),
    CURRENCY(R.string.converter_type_currency),
}