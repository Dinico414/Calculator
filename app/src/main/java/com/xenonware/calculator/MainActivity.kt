package com.xenonware.calculator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
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
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.core.view.WindowCompat
import com.xenon.mylibrary.values.ButtonBoxPadding
import com.xenon.mylibrary.values.CompactButtonSize
import com.xenon.mylibrary.values.LargeCornerRadius
import com.xenon.mylibrary.values.LargePadding
import com.xenon.mylibrary.values.MediumCornerRadius
import com.xenon.mylibrary.values.NoCornerRadius
import com.xenon.mylibrary.values.NoPadding
import com.xenon.mylibrary.values.SmallCornerRadius
import com.xenon.mylibrary.values.SmallElevation
import com.xenonware.calculator.data.SharedPreferenceManager
import com.xenonware.calculator.ui.layouts.ButtonLayout
import com.xenonware.calculator.ui.res.CalculatorScreen
import com.xenonware.calculator.ui.theme.ScreenEnvironment
import com.xenonware.calculator.viewmodel.CalculatorViewModel
import com.xenonware.calculator.viewmodel.LayoutType
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials


class MainActivity : ComponentActivity() {
    private val viewModel: CalculatorViewModel by viewModels()

    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    private var lastAppliedTheme: Int = -1
    private var lastAppliedCoverThemeEnabled: Boolean = false
    private var lastAppliedBlackedOutMode: Boolean = false


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedPreferenceManager = SharedPreferenceManager(applicationContext)

        val initialThemePref = sharedPreferenceManager.theme
        val initialCoverThemeEnabledSetting = sharedPreferenceManager.coverThemeEnabled
        val initialBlackedOutMode = sharedPreferenceManager.blackedOutModeEnabled

        updateAppCompatDelegateTheme(initialThemePref)

        lastAppliedTheme = initialThemePref
        lastAppliedCoverThemeEnabled = initialCoverThemeEnabledSetting
        lastAppliedBlackedOutMode = initialBlackedOutMode

        setContent {
            val currentContainerSize = LocalWindowInfo.current.containerSize
            val applyCoverTheme = sharedPreferenceManager.isCoverThemeApplied(currentContainerSize)

            ScreenEnvironment(
                themePreference = lastAppliedTheme,
                coverTheme = applyCoverTheme,
                blackedOutModeEnabled = lastAppliedBlackedOutMode
            ) { layoutType, isLandscape ->
                val isCoverScreen = layoutType == LayoutType.COVER

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isCoverScreen) Color.Black
                            else colorScheme.background
                        ), color = if (isCoverScreen) Color.Black else colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                                    .asPaddingValues()
                            )
                            .then(
                                if (isCoverScreen) {
                                    Modifier.background(Color.Black).padding(horizontal = NoPadding)
                                        .clip(RoundedCornerShape(NoCornerRadius))
                                } else {
                                    Modifier.clip(
                                            RoundedCornerShape(
                                                topStart = LargeCornerRadius,
                                                topEnd = LargeCornerRadius
                                            )
                                        ).background(colorScheme.surfaceContainer)
                                }
                            )
                    ) {
                        XenonApp(
                            viewModel = viewModel,
                            layoutType = layoutType,
                            isLandscape = isLandscape,
                            onOpenSettings = {
                                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                                startActivity(intent)
                            },
                            onOpenConverter = {
                                val intent =
                                    Intent(this@MainActivity, ConverterActivity::class.java)
                                startActivity(intent)
                            },
                            appSize = currentContainerSize
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val currentThemePref = sharedPreferenceManager.theme
        val currentCoverThemeEnabledSetting = sharedPreferenceManager.coverThemeEnabled
        val currentBlackedOutMode = sharedPreferenceManager.blackedOutModeEnabled

        if (currentThemePref != lastAppliedTheme || currentCoverThemeEnabledSetting != lastAppliedCoverThemeEnabled || currentBlackedOutMode != lastAppliedBlackedOutMode) {
            if (currentThemePref != lastAppliedTheme) {
                updateAppCompatDelegateTheme(currentThemePref)
            }

            lastAppliedTheme = currentThemePref
            lastAppliedCoverThemeEnabled = currentCoverThemeEnabledSetting
            lastAppliedBlackedOutMode = currentBlackedOutMode

            recreate()
        }
    }

    private fun updateAppCompatDelegateTheme(themePref: Int) {
        if (themePref >= 0 && themePref < sharedPreferenceManager.themeFlag.size) {
            AppCompatDelegate.setDefaultNightMode(sharedPreferenceManager.themeFlag[themePref])
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun XenonApp(
    viewModel: CalculatorViewModel,
    isLandscape: Boolean,
    layoutType: LayoutType,
    onOpenSettings: () -> Unit,
    onOpenConverter: () -> Unit,
    appSize: IntSize
) {
    var showMenu by remember { mutableStateOf(false) }
    val hazeState = remember { HazeState() }
    val isCoverScreenLayout = layoutType == LayoutType.COVER

    Column(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState)
            .padding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom).asPaddingValues()),
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
                        Modifier.padding(horizontal = LargePadding, vertical = NoPadding)
                            .padding(top = LargePadding)
                    }
                )
                .clip(RoundedCornerShape(MediumCornerRadius))
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
                        .shadow(elevation = SmallElevation, shape = CircleShape)
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
        )
    }
}