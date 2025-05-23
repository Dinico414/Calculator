package com.xenon.calculator


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xenon.calculator.ui.layouts.ButtonLayout
import com.xenon.calculator.ui.layouts.CalculatorScreen
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.CalculatorViewModel
import com.xenon.calculator.viewmodel.LayoutType

class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private var activeThemeForMainActivity: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedPreferenceManager = SharedPreferenceManager(applicationContext)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        sharedPreferenceManager = SharedPreferenceManager(applicationContext)
        activeThemeForMainActivity = sharedPreferenceManager.theme

        if (activeThemeForMainActivity >= 0 && activeThemeForMainActivity < sharedPreferenceManager.themeFlag.size) {
            AppCompatDelegate.setDefaultNightMode(sharedPreferenceManager.themeFlag[activeThemeForMainActivity])
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            activeThemeForMainActivity = 2
        }
        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
        startActivity(intent)
        setContent {

            val appIsDarkTheme = when (activeThemeForMainActivity) {
                0 -> false
                1 -> true
                else -> isSystemInDarkTheme()
            }

            CalculatorTheme(darkTheme = appIsDarkTheme) {
                val systemUiController = rememberSystemUiController()
                val view = LocalView.current

                BoxWithConstraints {
                    val screenWidth = this.maxWidth
                    val targetWidth = 418.30066.dp
                    val tolerance = 0.5.dp
                    val isTargetWidthMet = (screenWidth >= targetWidth - tolerance) && (screenWidth <= targetWidth + tolerance)

                    val systemBarColor = if (isTargetWidthMet) Color.Black else MaterialTheme.colorScheme.background //Notification- and Navigationbar color SDK 34-
                    val darkIcons = !isTargetWidthMet && !appIsDarkTheme

                    if (!view.isInEditMode) {
                        SideEffect {
                            systemUiController.setSystemBarsColor(
                                color = systemBarColor,
                                darkIcons = darkIcons,
                                isNavigationBarContrastEnforced = false
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .then(
                                if (isTargetWidthMet) {
                                    Modifier
                                        .background(Color.Black) //Cover screen App Background
                                        .padding(horizontal = 0.dp)
                                        .clip(RoundedCornerShape(0.dp))
                                } else {
                                    Modifier
                                        .background(MaterialTheme.colorScheme.background) //App Background
                                        .padding(horizontal = 15.dp)
                                        .clip(RoundedCornerShape(30.dp))
                                }
                            ),
                        color = if (isTargetWidthMet) Color.Black else MaterialTheme.colorScheme.background //Color of Calculator Screen padding
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(WindowInsets.safeDrawing.asPaddingValues())
                                .clip(RoundedCornerShape(30.dp))
                                .then(
                                    if (!isTargetWidthMet) {
                                        Modifier.background(MaterialTheme.colorScheme.surfaceContainer) //LayoutContainer Background
                                    } else {
                                        Modifier
                                    }
                                )
                        ) {
                            CalculatorApp(
                                viewModel = calculatorViewModel,
                                onOpenSettings = {
                                    val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                                    startActivity(intent)
                                }
                            )
                        }
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

@Composable
fun CalculatorApp(
    viewModel: CalculatorViewModel,
    onOpenSettings: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    LocalContext.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var showMenu by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = this.maxWidth
        val targetWidth = 418.30066.dp
        val tolerance = 0.5.dp
        val isTargetWidthMet = (screenWidth >= targetWidth - tolerance) && (screenWidth <= targetWidth + tolerance)


        val layoutType = when {
            isTargetWidthMet -> LayoutType.COVER
            screenWidth < 320.dp -> LayoutType.SMALL
            screenWidth < 600.dp -> LayoutType.COMPACT
            screenWidth < 840.dp -> LayoutType.MEDIUM
            else -> LayoutType.EXPANDED
        }

        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
                    .padding(horizontal = 10.dp, vertical = 0.dp)
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
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
                        .padding(top = 8.dp, end = 8.dp)

                ) {
                    IconButton(onClick = { showMenu = !showMenu }) {


                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },

                        offset = DpOffset(x = 0.dp, y = (-48).dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                showMenu = false
                                onOpenSettings()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Converter(Placeholder)") },
                            onClick = {
                                showMenu = false
                                onOpenSettings()
                            }
                        )
                        // DropdownMenuItem(text = { Text("Item 3") }, onClick = { showMenu = false })
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
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Mode")

@Composable
fun DefaultPreviewPortraitLight() {
    CalculatorTheme(darkTheme = false) {

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val previewViewModel = remember { CalculatorViewModel() }
            CalculatorApp(viewModel = previewViewModel, onOpenSettings = {})
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun DefaultPreviewPortraitDark() {
    CalculatorTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val previewViewModel = remember { CalculatorViewModel() }
            CalculatorApp(viewModel = previewViewModel, onOpenSettings = {})
                    }
    }
}

@Preview(showBackground = true, widthDp = 700, heightDp = 400, name = "Landscape")
@Composable
fun CalculatorAppPreviewLandscape() {
    val fakeViewModel = remember { CalculatorViewModel() }
    CalculatorTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            CalculatorApp(
                viewModel = fakeViewModel,
                onOpenSettings = {}
            )
        }
    }
}