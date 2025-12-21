package com.xenonware.calculator.ui.layouts.calculator

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xenon.mylibrary.ActivityScreen
import com.xenon.mylibrary.theme.DeviceConfigProvider
import com.xenon.mylibrary.theme.LocalDeviceConfig
import com.xenon.mylibrary.values.CompactButtonSize
import com.xenon.mylibrary.values.LargeCornerRadius
import com.xenon.mylibrary.values.LargePadding
import com.xenon.mylibrary.values.NoElevation
import com.xenon.mylibrary.values.NoPadding
import com.xenon.mylibrary.values.SmallElevation
import com.xenonware.calculator.R
import com.xenonware.calculator.ui.layouts.ButtonLayout
import com.xenonware.calculator.ui.res.CalculatorScreen
import com.xenonware.calculator.ui.res.HistoryLog
import com.xenonware.calculator.ui.res.MenuItem
import com.xenonware.calculator.ui.res.XenonDropDown
import com.xenonware.calculator.viewmodel.CalculatorViewModel
import com.xenonware.calculator.viewmodel.LayoutType
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi


@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalHazeMaterialsApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun CompactCalculator(
    viewModel: CalculatorViewModel = viewModel(),
    layoutType: LayoutType,
    isLandscape: Boolean,
    onOpenSettings: () -> Unit,
    onOpenConverter: () -> Unit,
    appSize: IntSize
) {
    DeviceConfigProvider(appSize = appSize) {
        val deviceConfig = LocalDeviceConfig.current
        val application = LocalContext.current.applicationContext as Application

        var expand by remember { mutableStateOf(false) }

        val configuration = LocalConfiguration.current
        val appHeight = configuration.screenHeightDp.dp

        ActivityScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding()
                .onSizeChanged { newSize ->
                },
            titleText = "",
            collapsedHeight = 0.dp,
            flexModel = "TopContainer",
            expand = expand,
            expandedHeight = LocalConfiguration.current.screenHeightDp.dp.times(0.25f),

        headerContent = {fraction ->
                Box(
                    modifier = Modifier
                        .padding(
                            WindowInsets.safeDrawing
                                .only(WindowInsetsSides.Horizontal)
                                .asPaddingValues()
                        )
                        .padding(horizontal = LargePadding)
                        .padding(top = LargePadding)
                ) {
                    HistoryLog(
                        history = viewModel.history,
                        fraction = fraction,
                        onClearHistory = { viewModel.clearHistory() },
                        hazeState = remember { HazeState() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            },
            content = {
                var showMenu by remember { mutableStateOf(false) }
                val hazeState = remember { HazeState() }
                val isCoverScreenLayout = layoutType == LayoutType.COVER

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .hazeSource(hazeState)
                        .padding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                                .asPaddingValues()
                        ), verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.35f)
                            .then(
                                if (isCoverScreenLayout) {
                                    Modifier.padding(horizontal = NoPadding, vertical = NoPadding)
                                } else {
                                    Modifier.padding(
                                            horizontal = LargePadding, vertical = NoPadding
                                        ).padding(top = LargePadding)
                                }
                            )
                            .clip(RoundedCornerShape(LargeCornerRadius))
                            .background(colorScheme.secondaryContainer)
                    ) {
                        CalculatorScreen(
                            viewModel = viewModel,
                            isLandscape = isLandscape,
                            layoutType = layoutType,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .align (Alignment.TopStart)
                                .padding(top = LargePadding, start = LargePadding)
                        ) {
                            val interactionSource = remember { MutableInteractionSource() }

                            Box(
                                modifier = Modifier
                                    .clip(shape = CircleShape)
                                    .size(CompactButtonSize)
                                    .clickable(
                                        onClick = { expand = !expand },
                                        interactionSource = interactionSource,
                                        indication = LocalIndication.current,
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.History,
                                    contentDescription = "History",
                                    tint = colorScheme.onSecondaryContainer,
                                )
                            }
                        }


                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = LargePadding, end = LargePadding)
                        ) {
                            val interactionSource = remember { MutableInteractionSource() }

                            // Define menu items
                            val converterText = stringResource(id = R.string.converter)
                            val settingsText = stringResource(id = R.string.settings)
                            val textColor = if (isCoverScreenLayout) Color.White else colorScheme.onSurface

                            val menuItems = remember(isCoverScreenLayout) {
                                listOf(
                                    MenuItem(
                                        icon = { Icon(Icons.Rounded.CurrencyExchange, contentDescription = "Menu") },
                                        text = converterText,
                                        textColor = textColor,
                                        onClick = { onOpenConverter() }
                                    ),
                                    MenuItem(
                                        icon = { Icon(Icons.Rounded.Settings, contentDescription = "Menu") },
                                        text = settingsText,
                                        textColor = textColor,
                                        onClick = { onOpenSettings() }
                                    )
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .shadow(elevation = if (showMenu) NoElevation else SmallElevation, shape = CircleShape)
                                    .clip(shape = CircleShape)
                                    .size(CompactButtonSize)
                                    .background(color = colorScheme.surfaceContainer)
                                    .clickable(
                                        onClick = { showMenu = !showMenu },
                                        interactionSource = interactionSource,
                                        indication = LocalIndication.current,
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.MoreVert,
                                    contentDescription = "Menu",
                                    tint = colorScheme.onSurface,
                                )

                                XenonDropDown(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }, items = menuItems,
                                    hazeState = hazeState,
                                    paddingValues = PaddingValues(0.dp),
                                    radius = 20.dp,
                                    width = 170.dp,
                                    bgcSDK12a = colorScheme.surfaceContainer,
                                    bgcSDK11b = colorScheme.surfaceDim
                                )
                            }
                        }
                    }

                    ButtonLayout(
                        viewModel = viewModel,
                        isLandscape = isLandscape,
                        layoutType = layoutType,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.65f),

                        )
                }
            })
    }
}