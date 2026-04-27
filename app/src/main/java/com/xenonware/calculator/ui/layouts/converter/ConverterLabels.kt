package com.xenonware.calculator.ui.layouts.converter

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.xenonware.calculator.R
import com.xenonware.calculator.util.ConverterType

@Composable
internal fun fromUnitLabel(type: ConverterType): String {
    val typeName = stringResource(id = type.displayNameResId)
    return stringResource(id = R.string.label_from, typeName.lowercase())
}

@Composable
internal fun toUnitLabel(type: ConverterType): String {
    val typeName = stringResource(id = type.displayNameResId)
    return stringResource(id = R.string.label_to, typeName.lowercase())
}
