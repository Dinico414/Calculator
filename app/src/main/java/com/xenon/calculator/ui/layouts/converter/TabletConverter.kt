package com.xenon.calculator.ui.layouts.converter

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.xenon.calculator.R
import com.xenon.calculator.ui.layouts.ActivityScreen
import com.xenon.calculator.ui.res.ConverterTypeDropdown
import com.xenon.calculator.ui.res.InputGroup
import com.xenon.calculator.ui.res.XenonTextField
import com.xenon.calculator.ui.values.IconSizeLarge
import com.xenon.calculator.ui.values.LargeCornerRadius
import com.xenon.calculator.ui.values.LargePadding
import com.xenon.calculator.ui.values.LargerSpacing
import com.xenon.calculator.ui.values.UnitDropdown
import com.xenon.calculator.viewmodel.ConverterViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun TabletConverter(
    onNavigateBack: (() -> Unit)? = null, viewModel: ConverterViewModel
) {
    LocalContext.current

    val hazeState = remember { HazeState() }

    val selectedType by viewModel.selectedConverterType
    val value1 by viewModel.value1
    val value2 by viewModel.value2

    val fromVolumeUnit by viewModel.fromVolumeUnit
    val toVolumeUnit by viewModel.toVolumeUnit
    val fromLengthUnit by viewModel.fromLengthUnit
    val toLengthUnit by viewModel.toLengthUnit
    val fromTemperatureUnit by viewModel.fromTemperatureUnit
    val toTemperatureUnit by viewModel.toTemperatureUnit
    val fromCurrencyUnit by viewModel.fromCurrencyUnit
    val toCurrencyUnit by viewModel.toCurrencyUnit
    val fromAreaUnit by viewModel.fromAreaUnit
    val toAreaUnit by viewModel.toAreaUnit
    val fromWeightUnit by viewModel.fromWeightUnit
    val toWeightUnit by viewModel.toWeightUnit

    var accumulatedRotation by remember { mutableFloatStateOf(0f) }
    val rotationAngle by animateFloatAsState(
        targetValue = accumulatedRotation,
        animationSpec = tween(durationMillis = 300),
        label = "IconRotation"
    )

    ActivityScreen(
        title = { fontSize, color ->
            Text(
                stringResource(id = R.string.converter), fontSize = fontSize, color = color
            )
        },
        navigationIcon = if (onNavigateBack != null) {
            {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.navigate_back_description)
                    )
                }
            }
        } else {
            null
        },
        appBarActions = {},
        isAppBarCollapsible = true,
        contentModifier = Modifier.hazeSource(hazeState),
        content = { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(LargeCornerRadius))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = LargePadding, vertical = LargePadding
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(LargerSpacing)
            ) {
                InputGroup {
                    ConverterTypeDropdown(
                        selectedType = selectedType, onTypeSelected = { newType ->
                            viewModel.onConverterTypeChange(newType)
                        }, hazeState = hazeState
                    )
                }

                val spacing = LargerSpacing
                SubcomposeLayout(modifier = Modifier.fillMaxWidth()) { constraints ->
                    val subcomposeContent = @Composable {
                        InputGroup(modifier = Modifier.weight(1f)) {
                            UnitDropdown(
                                label = fromUnitLabel(selectedType),
                                selectedConverterType = selectedType,
                                selectedVolumeUnit = fromVolumeUnit,
                                onVolumeUnitSelected = { unit ->
                                    viewModel.onFromVolumeUnitChange(
                                        unit
                                    )
                                },
                                selectedLengthUnit = fromLengthUnit,
                                onLengthUnitSelected = { unit ->
                                    viewModel.onFromLengthUnitChange(
                                        unit
                                    )
                                },
                                selectedTemperatureUnit = fromTemperatureUnit,
                                onTemperatureUnitSelected = { unit ->
                                    viewModel.onFromTemperatureUnitChange(
                                        unit
                                    )
                                },
                                selectedCurrencyUnit = fromCurrencyUnit,
                                onCurrencyUnitSelected = { unit ->
                                    viewModel.onFromCurrencyUnitChange(
                                        unit
                                    )
                                },
                                selectedAreaUnit = fromAreaUnit,
                                onAreaUnitSelected = { unit -> viewModel.onFromAreaUnitChange(unit) },
                                selectedWeightUnit = fromWeightUnit,
                                onWeightUnitSelected = { unit ->
                                    viewModel.onFromWeightUnitChange(
                                        unit
                                    )
                                },
                                hazeState = hazeState,
                                modifier = Modifier.fillMaxWidth()
                            )
                            XenonTextField(
                                value = value1, onValueChange = { newValue ->
                                    viewModel.onValueChanged(
                                        newValue, ConverterViewModel.EditedField.FIELD1
                                    )
                                }, label = stringResource(id = R.string.value_1)
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.onUnitsSwitch()
                                accumulatedRotation += 180f
                            },
                            modifier = Modifier
                                .width(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.tertiary)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.swap),
                                contentDescription = stringResource(R.string.switch_units_description),
                                tint = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier
                                    .rotate(rotationAngle)
                                    .width(IconSizeLarge)
                                    .height(IconSizeLarge)
                            )
                        }

                        InputGroup(modifier = Modifier.weight(1f)) {
                            UnitDropdown(
                                label = toUnitLabel(selectedType),
                                selectedConverterType = selectedType,
                                selectedVolumeUnit = toVolumeUnit,
                                onVolumeUnitSelected = { unit -> viewModel.onToVolumeUnitChange(unit) },
                                selectedLengthUnit = toLengthUnit,
                                onLengthUnitSelected = { unit -> viewModel.onToLengthUnitChange(unit) },
                                selectedTemperatureUnit = toTemperatureUnit,
                                onTemperatureUnitSelected = { unit ->
                                    viewModel.onToTemperatureUnitChange(
                                        unit
                                    )
                                },
                                selectedCurrencyUnit = toCurrencyUnit,
                                onCurrencyUnitSelected = { unit ->
                                    viewModel.onToCurrencyUnitChange(
                                        unit
                                    )
                                },
                                selectedAreaUnit = toAreaUnit,
                                onAreaUnitSelected = { unit -> viewModel.onToAreaUnitChange(unit) },
                                selectedWeightUnit = toWeightUnit,
                                onWeightUnitSelected = { unit -> viewModel.onToWeightUnitChange(unit) },
                                hazeState = hazeState,
                                modifier = Modifier.fillMaxWidth()
                            )
                            XenonTextField(
                                value = value2, onValueChange = { newValue ->
                                    viewModel.onValueChanged(
                                        newValue, ConverterViewModel.EditedField.FIELD2
                                    )
                                }, label = stringResource(id = R.string.value_2)
                            )
                        }
                    }

                    val measurables = subcompose(0, subcomposeContent)

                    if (measurables.size != 3) {
                        return@SubcomposeLayout layout(0, 0) {}
                    }

                    val group1Measurable = measurables[0]
                    val iconButtonMeasurable = measurables[1]
                    val group2Measurable = measurables[2]

                    val spacingPx = spacing.toPx().roundToInt()

                    val iconButtonTargetWidth = 64.dp.toPx().roundToInt()

                    val availableWidthForGroups =
                        (constraints.maxWidth - iconButtonTargetWidth - (2 * spacingPx)).coerceAtLeast(
                            0
                        )
                    val groupWidth =
                        if (availableWidthForGroups > 0) availableWidthForGroups / 2 else 0

                    val group1Placeable = group1Measurable.measure(
                        constraints.copy(minWidth = groupWidth, maxWidth = groupWidth)
                    )
                    val group2Placeable = group2Measurable.measure(
                        constraints.copy(minWidth = groupWidth, maxWidth = groupWidth)
                    )

                    val referenceHeight =
                        max(group1Placeable.height, group2Placeable.height)

                    val iconButtonMinIntrinsicHeight =
                        iconButtonMeasurable.minIntrinsicHeight(iconButtonTargetWidth)
                    val iconButtonTargetHeight = (referenceHeight * 0.5f).roundToInt()
                        .coerceAtLeast(iconButtonMinIntrinsicHeight).coerceAtMost(referenceHeight)


                    val iconButtonPlaceable = iconButtonMeasurable.measure(
                        Constraints(
                            minWidth = iconButtonTargetWidth,
                            maxWidth = iconButtonTargetWidth,
                            minHeight = iconButtonTargetHeight,
                            maxHeight = iconButtonTargetHeight
                        )
                    )

                    val totalWidth =
                        group1Placeable.width + spacingPx + iconButtonPlaceable.width + spacingPx + group2Placeable.width
                    val maxHeight = max(
                        group1Placeable.height,
                        max(iconButtonPlaceable.height, group2Placeable.height)
                    )

                    layout(totalWidth, maxHeight) {
                        var currentX = 0
                        group1Placeable.placeRelative(
                            currentX, (maxHeight - group1Placeable.height) / 2
                        )
                        currentX += group1Placeable.width + spacingPx

                        iconButtonPlaceable.placeRelative(
                            currentX, (maxHeight - iconButtonPlaceable.height) / 2
                        )
                        currentX += iconButtonPlaceable.width + spacingPx

                        group2Placeable.placeRelative(
                            currentX, (maxHeight - group2Placeable.height) / 2
                        )
                    }
                }
            }
        }
        // dialogs = { }
    )
}
