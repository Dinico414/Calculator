package com.xenon.calculator.ui.layouts.converter

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.xenon.calculator.R
import com.xenon.calculator.ui.layouts.ActivityScreen
import com.xenon.calculator.ui.res.ConverterTypeDropdown
import com.xenon.calculator.ui.res.XenonTextField
import com.xenon.calculator.ui.values.IconSizeLarge
import com.xenon.calculator.ui.values.IconSizeSmall
import com.xenon.calculator.ui.values.LargerSpacing
import com.xenon.calculator.ui.values.MediumSpacing
import com.xenon.calculator.ui.values.NoCornerRadius
import com.xenon.calculator.ui.values.SmallNarrowButtonWidth
import com.xenon.calculator.ui.values.UnitDropdown
import com.xenon.calculator.viewmodel.ConverterViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun CoverConverter(
    onNavigateBack: (() -> Unit)? = null, viewModel: ConverterViewModel
) {
    LocalContext.current

    val hazeState = remember { HazeState() }

    val selectedType by viewModel.selectedConverterType
    val value1 by viewModel.value1
    val value2 by viewModel.value2

    val fromTemperatureUnit by viewModel.fromTemperatureUnit
    val toTemperatureUnit by viewModel.toTemperatureUnit
    val fromCurrencyUnit by viewModel.fromCurrencyUnit
    val fromVolumeUnit by viewModel.fromVolumeUnit
    val fromLengthUnit by viewModel.fromLengthUnit
    val toCurrencyUnit by viewModel.toCurrencyUnit
    val fromWeightUnit by viewModel.fromWeightUnit
    val toVolumeUnit by viewModel.toVolumeUnit
    val toLengthUnit by viewModel.toLengthUnit
    val fromAreaUnit by viewModel.fromAreaUnit
    val toWeightUnit by viewModel.toWeightUnit
    val toAreaUnit by viewModel.toAreaUnit

    var accumulatedRotation by remember { mutableFloatStateOf(0f) }
    val rotationAngle by animateFloatAsState(
        targetValue = accumulatedRotation,
        animationSpec = tween(durationMillis = 300),
        label = "IconRotation"
    )

    ActivityScreen(
        title = { _, _ ->
        Text(stringResource(id = R.string.converter))
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
        isAppBarCollapsible = false,
        appBarCollapsedContainerColor = Color.Black,
        appBarExpandedContainerColor = Color.Black,
        screenBackgroundColor = Color.Black,
        contentBackgroundColor = Color.Black,

        contentModifier = Modifier.hazeSource(hazeState),
        contentCornerRadius = NoCornerRadius,

        content = { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MediumSpacing)
            ) {
                ConverterTypeDropdown(
                    selectedType = selectedType, onTypeSelected = { newType ->
                        viewModel.onConverterTypeChange(newType)
                    }, hazeState = hazeState
                )

                val spacing = MediumSpacing
                SubcomposeLayout(modifier = Modifier.fillMaxWidth()) { constraints ->
                    val subcomposeContent = @Composable {

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(MediumSpacing)
                        ) {
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


                        val interactionSource = remember { MutableInteractionSource() }
                        Box(
                            modifier = Modifier
                                .width(SmallNarrowButtonWidth)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.tertiary)
                                .clickable(
                                    onClick = {
                                        viewModel.swapUnits()
                                        accumulatedRotation += 180f
                                    },
                                    interactionSource = interactionSource,
                                    indication = LocalIndication.current,
                                ), contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.swap),
                                contentDescription = stringResource(R.string.switch_units_description),
                                tint = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier
                                    .rotate(rotationAngle)
                                    .width(IconSizeSmall)
                                    .height(IconSizeSmall)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(MediumSpacing)
                        ) {
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

                    val referenceHeightForIcon = max(group1Placeable.height, group2Placeable.height)
                    val iconButtonMinIntrinsicHeight =
                        iconButtonMeasurable.minIntrinsicHeight(iconButtonTargetWidth)
                    val iconButtonTargetHeight = (referenceHeightForIcon * 0.5f).roundToInt()
                        .coerceAtLeast(iconButtonMinIntrinsicHeight)
                        .coerceAtMost(referenceHeightForIcon)


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
