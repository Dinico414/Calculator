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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xenon.mylibrary.ActivityScreen
import com.xenon.mylibrary.theme.DeviceConfigProvider
import com.xenon.mylibrary.theme.LocalDeviceConfig
import com.xenon.mylibrary.values.ButtonBoxPadding
import com.xenon.mylibrary.values.CompactButtonSize
import com.xenon.mylibrary.values.LargeCornerRadius
import com.xenon.mylibrary.values.LargePadding
import com.xenon.mylibrary.values.NoPadding
import com.xenon.mylibrary.values.SmallCornerRadius
import com.xenon.mylibrary.values.SmallElevation
import com.xenonware.calculator.R
import com.xenonware.calculator.ui.layouts.ButtonLayout
import com.xenonware.calculator.ui.res.CalculatorScreen
import com.xenonware.calculator.viewmodel.CalculatorViewModel
import com.xenonware.calculator.viewmodel.LayoutType
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials


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

        val configuration = LocalConfiguration.current
        val appHeight = configuration.screenHeightDp.dp
        val isAppBarExpandable = false
//            when (layoutType) {
//            LayoutType.COVER -> false
//            LayoutType.SMALL -> false
//            LayoutType.COMPACT -> !isLandscape && appHeight >= 460.dp
//            LayoutType.MEDIUM -> true
//            LayoutType.EXPANDED -> true
//        }
        ActivityScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding()
                .onSizeChanged { newSize ->
                },
            titleText = "",
            expandable = isAppBarExpandable,
            collapsedHeight = 0.dp,
            flexModel = "FlexTopContainer",
            headerContent = {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(Modifier
                        .padding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                                .asPaddingValues()
                            )
                        .fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Rounded.History,
                            contentDescription = stringResource(R.string.navigate_back_description),
                            modifier = Modifier.padding(16.dp).size(24.dp)
                        )
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")
                        Text(text = "Test")

                    }
                }}
            },
            content = {
                var showMenu by remember { mutableStateOf(false) }
                val hazeState = remember { HazeState() }
                val isCoverScreenLayout = layoutType == LayoutType.COVER

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .hazeSource(hazeState),
                    verticalArrangement = Arrangement.Bottom
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
                                .align(Alignment.TopEnd)
                                .padding(top = LargePadding, end = LargePadding)
                        ) {
                            val interactionSource = remember { MutableInteractionSource() }

                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = SmallElevation, shape = CircleShape
                                    )
                                    .clip(shape = CircleShape)
                                    .size(CompactButtonSize)
                                    .background(color = colorScheme.surfaceContainer)
                                    .clickable(
                                        onClick = { showMenu = !showMenu },
                                        interactionSource = interactionSource,
                                        indication = LocalIndication.current,
                                    ), contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = "Menu",
                                    tint = colorScheme.onSurface,
                                )
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
                                offset = DpOffset(x = NoPadding, y = ButtonBoxPadding),
                                containerColor = Color.Transparent,
                                shadowElevation = SmallElevation,
                                shape = RoundedCornerShape(SmallCornerRadius),
                                modifier = Modifier
                                    .background(colorScheme.surfaceContainer)
                                    .hazeEffect(
                                        state = hazeState, style = HazeMaterials.ultraThin()
                                    )
                            ) {
                                DropdownMenuItem(text = {
                                    Text(
                                        stringResource(id = R.string.converter),
                                        color = if (isCoverScreenLayout) Color.White else colorScheme.onSurface
                                    )
                                }, onClick = {
                                    showMenu = false
                                    onOpenConverter()
                                })
                                DropdownMenuItem(text = {
                                    Text(
                                        stringResource(id = R.string.settings),
                                        color = if (isCoverScreenLayout) Color.White else colorScheme.onSurface
                                    )
                                }, onClick = {
                                    showMenu = false
                                    onOpenSettings()
                                })
                            }
                        }
                    }

                    ButtonLayout(
                        viewModel = viewModel,
                        isLandscape = isLandscape,
                        layoutType = layoutType,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.65f)
                            .padding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                                    .asPaddingValues()
                            )
                    )
                }
            })
    }
}