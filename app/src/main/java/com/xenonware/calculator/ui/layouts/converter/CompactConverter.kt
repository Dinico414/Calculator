package com.xenonware.calculator.ui.layouts.converter

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xenon.mylibrary.ActivityScreen
import com.xenon.mylibrary.res.XenonTextField
import com.xenon.mylibrary.values.LargeCornerRadius
import com.xenon.mylibrary.values.LargerSpacing
import com.xenon.mylibrary.values.LargestPadding
import com.xenon.mylibrary.values.MediumPadding
import com.xenon.mylibrary.values.NoSpacing
import com.xenonware.calculator.R
import com.xenonware.calculator.ui.res.ConverterTypeDropdown
import com.xenonware.calculator.ui.res.InputGroup
import com.xenonware.calculator.viewmodel.ConverterViewModel
import com.xenonware.calculator.viewmodel.LayoutType
import com.xenonware.calculator.viewmodel.classes.UnitItems
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun CompactConverter(
    onNavigateBack: (() -> Unit)? = null,
    layoutType: LayoutType,
    isLandscape: Boolean,
    viewModel: ConverterViewModel
) {
    LocalContext.current

    val configuration = LocalConfiguration.current
    val appHeight = configuration.screenHeightDp.dp
    val isAppBarExpandable = when (layoutType) {
        LayoutType.COVER -> false
        LayoutType.SMALL -> false
        LayoutType.COMPACT -> !isLandscape && appHeight >= 460.dp
        LayoutType.MEDIUM -> true
        LayoutType.EXPANDED -> true
    }

    val hazeState = remember { HazeState() }

    val selectedType by viewModel.selectedConverterType
    val value1 by viewModel.value1
    val value2 by viewModel.value2

    val fromVolumeUnit by viewModel.fromVolumeUnit
    val toVolumeUnit by viewModel.toVolumeUnit

    val fromAreaUnit by viewModel.fromAreaUnit
    val toAreaUnit by viewModel.toAreaUnit

    val fromLengthUnit by viewModel.fromLengthUnit
    val toLengthUnit by viewModel.toLengthUnit

    val fromSpeedUnit by viewModel.fromSpeedUnit
    val toSpeedUnit by viewModel.toSpeedUnit

    val fromWeightUnit by viewModel.fromWeightUnit
    val toWeightUnit by viewModel.toWeightUnit

    val fromTemperatureUnit by viewModel.fromTemperatureUnit
    val toTemperatureUnit by viewModel.toTemperatureUnit

    val fromCurrencyUnit by viewModel.fromCurrencyUnit
    val toCurrencyUnit by viewModel.toCurrencyUnit

    var accumulatedRotation by remember { mutableFloatStateOf(0f) }
    val rotationAngle by animateFloatAsState(
        targetValue = accumulatedRotation,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,  // Gives a nice bounce
            stiffness = Spring.StiffnessLow               // Slower, more playful feel
        ),
        label = "IconRotation"
    )
    ActivityScreen(
        titleText = stringResource(id = R.string.converter),
        expandable = isAppBarExpandable,
        navigationIconStartPadding = MediumPadding,
        navigationIconPadding = MediumPadding,
        navigationIconSpacing = NoSpacing,
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.navigate_back_description),
                modifier = Modifier.size(24.dp)
            )
        },
        onNavigationIconClick = onNavigateBack,
        hasNavigationIconExtraContent = false,
        actions = {},
        contentModifier = Modifier
            .hazeSource(hazeState),
        content = { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = LargeCornerRadius,
                            topEnd = LargeCornerRadius
                        )
                    )
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = LargestPadding,
                        end = LargestPadding,
                        top = LargestPadding,
                        bottom = WindowInsets.safeDrawing
                            .asPaddingValues()
                            .calculateBottomPadding() + LargestPadding
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(LargerSpacing)
            ) {
                InputGroup {
                    ConverterTypeDropdown(
                        selectedType = selectedType,
                        onTypeSelected = { newType ->
                            viewModel.onConverterTypeChange(newType)
                        },
                        hazeState = hazeState
                    )
                }

                InputGroup(modifier = Modifier.fillMaxWidth()) {
                    UnitItems(
                        label = fromUnitLabel(selectedType),
                        selectedConverterType = selectedType,

                        selectedVolumeUnit = fromVolumeUnit,
                        onVolumeUnitSelected = { unit -> viewModel.onFromVolumeUnitChange(unit) },

                        selectedAreaUnit = fromAreaUnit,
                        onAreaUnitSelected = { unit -> viewModel.onFromAreaUnitChange(unit) },

                        selectedLengthUnit = fromLengthUnit,
                        onLengthUnitSelected = { unit -> viewModel.onFromLengthUnitChange(unit) },

                        selectedSpeedUnit = fromSpeedUnit,
                        onSpeedUnitSelected = { unit -> viewModel.onFromSpeedUnitChange(unit) },

                        selectedWeightUnit = fromWeightUnit,
                        onWeightUnitSelected = { unit -> viewModel.onFromWeightUnitChange(unit) },

                        selectedTemperatureUnit = fromTemperatureUnit,
                        onTemperatureUnitSelected = { unit ->
                            viewModel.onFromTemperatureUnitChange(
                                unit
                            )
                        },

                        selectedCurrencyUnit = fromCurrencyUnit,
                        onCurrencyUnitSelected = { unit -> viewModel.onFromCurrencyUnitChange(unit) },

                        hazeState = hazeState,
                        modifier = Modifier.fillMaxWidth()
                    )
                    XenonTextField(
                        value = value1,
                        onValueChange = { newValue ->
                            viewModel.onValueChanged(
                                newValue, ConverterViewModel.EditedField.FIELD1
                            )
                        },
                        placeholder = {Text(stringResource(id = R.string.value_1))},
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                }

                val interactionSource = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier.Companion
                        .height(96.dp)
                        .fillMaxWidth(0.5f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary)
                        .clickable(
                            onClick = {
                                viewModel.swapUnits()
                                accumulatedRotation += 180f
                            },
                            interactionSource = interactionSource,
                            indication = LocalIndication.current,
                        ),
                    contentAlignment = Alignment.Center

                ) {
                    Icon(
                        imageVector = Icons.Rounded.Sync,
                        contentDescription = stringResource(R.string.switch_units_description),
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier
                            .rotate(45f)
                            .rotate(rotationAngle)
                            .graphicsLayer(scaleX = -1f)
                            .size(32.dp)
                    )
                }

                InputGroup(modifier = Modifier.fillMaxWidth()) {
                    UnitItems(
                        label = toUnitLabel(selectedType),
                        selectedConverterType = selectedType,

                        selectedVolumeUnit = toVolumeUnit,
                        onVolumeUnitSelected = { unit -> viewModel.onToVolumeUnitChange(unit) },

                        selectedAreaUnit = toAreaUnit,
                        onAreaUnitSelected = { unit -> viewModel.onToAreaUnitChange(unit) },

                        selectedLengthUnit = toLengthUnit,
                        onLengthUnitSelected = { unit -> viewModel.onToLengthUnitChange(unit) },

                        selectedSpeedUnit = toSpeedUnit,
                        onSpeedUnitSelected = { unit -> viewModel.onToSpeedUnitChange(unit) },

                        selectedWeightUnit = toWeightUnit,
                        onWeightUnitSelected = { unit -> viewModel.onToWeightUnitChange(unit) },

                        selectedTemperatureUnit = toTemperatureUnit,
                        onTemperatureUnitSelected = { unit ->
                            viewModel.onToTemperatureUnitChange(
                                unit
                            )
                        },

                        selectedCurrencyUnit = toCurrencyUnit,
                        onCurrencyUnitSelected = { unit -> viewModel.onToCurrencyUnitChange(unit) },
                        hazeState = hazeState,
                        modifier = Modifier.fillMaxWidth()
                    )
                    XenonTextField(
                        value = value2,
                        onValueChange = { newValue ->
                            viewModel.onValueChanged(
                                newValue, ConverterViewModel.EditedField.FIELD2
                            )
                        },
                        placeholder = {Text(stringResource(id = R.string.value_2))},
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),

                    )
                }
            }
        }
        // dialogs = { }
    )
}
