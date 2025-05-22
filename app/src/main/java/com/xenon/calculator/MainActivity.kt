package com.xenon.calculator


import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xenon.calculator.ui.layouts.ButtonLayout
import com.xenon.calculator.ui.layouts.CalculatorScreen
import com.xenon.calculator.ui.layouts.settings.CompactSettings
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.CalculatorViewModel
import com.xenon.calculator.viewmodel.LayoutType
import com.xenon.calculator.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedPreferenceManager = SharedPreferenceManager(applicationContext)
        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.SettingsViewModelFactory(application)
        )[SettingsViewModel::class.java]

        AppCompatDelegate.setDefaultNightMode(sharedPreferenceManager.themeFlag[sharedPreferenceManager.theme])

        setContent {
            var currentThemeSetting by remember { mutableIntStateOf(sharedPreferenceManager.theme) }

            LaunchedEffect(settingsViewModel) {
                settingsViewModel.themeChanged.collect { changed ->
                    if (changed) {
                        currentThemeSetting = sharedPreferenceManager.theme
                        settingsViewModel.onThemeApplied()
                    }
                }
            }


            val isDarkTheme = when (currentThemeSetting) {
                0 -> false
                1 -> true
                else -> isSystemInDarkTheme()
            }

            CalculatorTheme(darkTheme = isDarkTheme) {
                val systemUiController = rememberSystemUiController()
                val view = LocalView.current
                val navController = rememberNavController()

                BoxWithConstraints {
                    val screenWidth = this.maxWidth
                    val targetWidth = 418.30066.dp
                    val tolerance = 0.5.dp
                    val isTargetWidthMet = (screenWidth >= targetWidth - tolerance) && (screenWidth <= targetWidth + tolerance)

                    val systemBarColor = if (isTargetWidthMet) Color.Black else MaterialTheme.colorScheme.background
                    val darkIcons = !isTargetWidthMet && !isDarkTheme

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
                            .padding(WindowInsets.safeDrawing.asPaddingValues())
                            .then(
                                if (isTargetWidthMet) {
                                    Modifier
                                        .background(Color.Black)
                                        .padding(horizontal = 0.dp)
                                        .clip(RoundedCornerShape(0.dp))
                                } else {
                                    Modifier
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(horizontal = 15.dp)
                                        .clip(RoundedCornerShape(30.dp))
                                }
                            ),
                        color = if (isTargetWidthMet) Color.Black else MaterialTheme.colorScheme.background
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .then(
                                    if (!isTargetWidthMet) {
                                        Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
                                    } else {
                                        Modifier
                                    }
                                )
                        ) {
                            NavHost(navController = navController, startDestination = "calculator") {
                                composable("calculator") {
                                    CalculatorApp(
                                        viewModel = calculatorViewModel,
                                        navController = navController
                                    )
                                }
                                composable("settings") {
                                    CompactSettings(
                                        onNavigateBack = { navController.popBackStack() },
                                        viewModel = settingsViewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorApp(
    viewModel: CalculatorViewModel,
    navController: NavController,
) {
    val configuration = LocalConfiguration.current
    LocalContext.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = this.maxWidth
//        val targetWidth = 418.30066.dp
//        val tolerance = 0.5.dp
//        val isTargetWidthMet = (screenWidth >= targetWidth - tolerance) && (screenWidth <= targetWidth + tolerance)


        val layoutType = when {
//            isTargetWidthMet -> LayoutType.COVER
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
                        .fillMaxSize()
                        .padding(top = 8.dp, end = 8.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = {
                        navController.navigate("settings")
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
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
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun DefaultPreviewPortrait() {
    CalculatorTheme {
        rememberNavController()
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val previewViewModel = CalculatorViewModel()
                previewViewModel.onButtonClick("123")
                CalculatorScreen(
                    viewModel = previewViewModel,
                    isLandscape = false,
                    layoutType = LayoutType.COMPACT,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


@Preview
@Composable
fun CalculatorAppPreview() {
    val fakeViewModel = remember { CalculatorViewModel() }
    val dummyNavController = rememberNavController()
    CalculatorTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 15.dp)
                .clip(RoundedCornerShape(30.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                CalculatorApp(viewModel = fakeViewModel, navController = dummyNavController)
            }
        }
    }
}

@Preview(widthDp = 800, heightDp = 360)
@Composable
fun CalculatorAppPreviewLandscape() {
    val fakeViewModel = remember { CalculatorViewModel() }
    val dummyNavController = rememberNavController()
    CalculatorTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 15.dp)
                .clip(RoundedCornerShape(30.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                CalculatorApp(viewModel = fakeViewModel, navController = dummyNavController)
            }
        }
    }
}
