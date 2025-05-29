package com.xenon.calculator

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.core.view.WindowCompat
import com.xenon.calculator.ui.layouts.ButtonLayout
import com.xenon.calculator.ui.layouts.CalculatorScreen
import com.xenon.calculator.ui.res.ButtonBoxPadding
import com.xenon.calculator.ui.res.LargeCornerRadius
import com.xenon.calculator.ui.res.MediumCornerRadius
import com.xenon.calculator.ui.res.MediumPadding
import com.xenon.calculator.ui.res.NoCornerRadius
import com.xenon.calculator.ui.res.NoElevation
import com.xenon.calculator.ui.res.NoPadding
import com.xenon.calculator.ui.res.SmallCornerRadius
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.ui.theme.ScreenEnvironment
import com.xenon.calculator.viewmodel.CalculatorViewModel
import com.xenon.calculator.viewmodel.LayoutType
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.CupertinoMaterials
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi

class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private var activeThemeForMainActivity: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedPreferenceManager = SharedPreferenceManager(applicationContext)

        activeThemeForMainActivity = sharedPreferenceManager.theme

        if (activeThemeForMainActivity >= 0 && activeThemeForMainActivity < sharedPreferenceManager.themeFlag.size) {
            AppCompatDelegate.setDefaultNightMode(sharedPreferenceManager.themeFlag[activeThemeForMainActivity])
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            activeThemeForMainActivity = 2
        }

        setContent {
            ScreenEnvironment(themePreference = activeThemeForMainActivity) { layoutType, isLandscape ->
                val isCoverScreen = layoutType == LayoutType.COVER

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isCoverScreen) Color.Black
                            else MaterialTheme.colorScheme.background
                        ),
                    color = if (isCoverScreen) Color.Black else MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal).asPaddingValues())
                            .then(
                                if (isCoverScreen) {
                                    Modifier
                                        .background(Color.Black)
                                        .padding(horizontal = NoPadding)
                                        .clip(RoundedCornerShape(NoCornerRadius))
                                } else {
                                    Modifier
                                        .clip(RoundedCornerShape(topStart = LargeCornerRadius, topEnd = LargeCornerRadius))
                                        .background(MaterialTheme.colorScheme.surfaceContainer)
                                }
                            )
                    ) {
                        CalculatorApp(
                            viewModel = calculatorViewModel,
                            layoutType = layoutType,
                            isLandscape = isLandscape,
                            onOpenSettings = {
                                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                                startActivity(intent)
                            },
                            onOpenConverter = {
                                val intent = Intent(this@MainActivity, ConverterActivity::class.java)
                                startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val storedTheme = sharedPreferenceManager.theme
        if (activeThemeForMainActivity != storedTheme) {
            activeThemeForMainActivity = storedTheme
            recreate()
        }
    }
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun CalculatorApp(
    viewModel: CalculatorViewModel,
    layoutType: LayoutType,
    isLandscape: Boolean,
    onOpenSettings: () -> Unit,
    onOpenConverter: () -> Unit
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
                        Modifier
                            .padding(horizontal = NoPadding, vertical = NoPadding)
                    } else {
                        Modifier
                            .padding(horizontal = MediumPadding, vertical = NoPadding)
                            .padding(top = MediumPadding)
                    }
                )
                .clip(RoundedCornerShape(if (isCoverScreenLayout) NoCornerRadius else MediumCornerRadius))
                .background(
                    if (isCoverScreenLayout) Color.Black
                    else MaterialTheme.colorScheme.secondaryContainer
                )
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
                    .padding(top = MediumPadding, end = MediumPadding)
                    .clip(shape = CircleShape)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    offset = DpOffset(x = NoPadding, y = ButtonBoxPadding),
                    containerColor = Color.Transparent,
                    shadowElevation = NoElevation,
                    modifier = Modifier
                        .clip(RoundedCornerShape(SmallCornerRadius))
                        .background(Color.Transparent)
                        .hazeEffect(
                            state = hazeState,
                            style = CupertinoMaterials.ultraThin()
                        )
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.converter), color = if (isCoverScreenLayout) Color.White else MaterialTheme.colorScheme.onSurface) },
                        onClick = {
                            showMenu = false
                            onOpenConverter()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.settings), color = if (isCoverScreenLayout) Color.White else MaterialTheme.colorScheme.onSurface) },
                        onClick = {
                            showMenu = false
                            onOpenSettings()
                        }
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
                .weight(0.65f)
        )
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Mode Compact")
@Composable
fun DefaultPreviewPortraitLight() {
    CalculatorTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            CalculatorApp(
                viewModel = remember { CalculatorViewModel() },
                layoutType = LayoutType.COMPACT,
                isLandscape = false,
                onOpenSettings = {},
                onOpenConverter = {}
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode Compact")
@Composable
fun DefaultPreviewPortraitDark() {
    CalculatorTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            CalculatorApp(
                viewModel = remember { CalculatorViewModel() },
                layoutType = LayoutType.COMPACT,
                isLandscape = false,
                onOpenSettings = {},
                onOpenConverter = {}
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 700, heightDp = 400, name = "Landscape Medium")
@Composable
fun CalculatorAppPreviewLandscape() {
    CalculatorTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            CalculatorApp(
                viewModel = remember { CalculatorViewModel() },
                layoutType = LayoutType.MEDIUM,
                isLandscape = true,
                onOpenSettings = {},
                onOpenConverter = {}
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 418, heightDp = 800, name = "Cover Screen")
@Composable
fun CalculatorAppPreviewCover() {

    CalculatorTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black ) {
            CalculatorApp(
                viewModel = remember { CalculatorViewModel() },
                layoutType = LayoutType.COVER,
                isLandscape = false,
                onOpenSettings = {},
                onOpenConverter = {}
            )
        }
    }
}