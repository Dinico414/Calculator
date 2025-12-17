package com.xenonware.calculator.ui.layouts.calculator

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xenon.mylibrary.ActivityScreen
import com.xenon.mylibrary.theme.DeviceConfigProvider
import com.xenon.mylibrary.theme.LocalDeviceConfig
import com.xenon.mylibrary.values.ExtraLargePadding
import com.xenonware.calculator.viewmodel.CalculatorViewModel
import com.xenonware.calculator.viewmodel.LayoutType
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
    appSize: IntSize
) {
    DeviceConfigProvider(appSize = appSize) {
        val deviceConfig = LocalDeviceConfig.current
        val application = LocalContext.current.applicationContext as Application

        val configuration = LocalConfiguration.current
        val appHeight = configuration.screenHeightDp.dp
        val isAppBarExpandable = when (layoutType) {
            LayoutType.COVER -> false
            LayoutType.SMALL -> false
            LayoutType.COMPACT -> !isLandscape && appHeight >= 460.dp
            LayoutType.MEDIUM -> true
            LayoutType.EXPANDED -> true
        }
        ActivityScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding()
                .onSizeChanged{ newSize ->
                },
            titleText = "",
            expandable = isAppBarExpandable,
            flexModel = "FlexTopContainer",
            headerContent = {
                //TODO History
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = ExtraLargePadding)
                ) {
                    
                }
            }
        )
    }
}