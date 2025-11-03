package com.xenonware.calculator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xenonware.calculator.ui.layouts.SettingsLayout
import com.xenonware.calculator.ui.theme.ScreenEnvironment
import com.xenonware.calculator.viewmodel.SettingsViewModel


object SettingsDestinations {
    const val MAIN_SETTINGS_ROUTE = "main_settings"
    // DEVELOPER_OPTIONS_ROUTE is not used for NavHost here as it's a separate Activity
}

class SettingsActivity : ComponentActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsViewModel = ViewModelProvider(
            this, SettingsViewModel.SettingsViewModelFactory(application)
        )[SettingsViewModel::class.java]

        enableEdgeToEdge()

        setContent {
            val navController =
                rememberNavController() // This NavController is for within SettingsActivity if needed

            val activeNightMode by settingsViewModel.activeNightModeFlag.collectAsState()
            LaunchedEffect(activeNightMode) {
                AppCompatDelegate.setDefaultNightMode(activeNightMode)
            }

            val persistedAppThemeIndex by settingsViewModel.persistedThemeIndex.collectAsState()
            val blackedOutEnabled by settingsViewModel.blackedOutModeEnabled.collectAsState()
            val coverThemeEnabled by settingsViewModel.enableCoverTheme.collectAsState()
            val containerSize = LocalWindowInfo.current.containerSize
            val applyCoverTheme = remember(containerSize, coverThemeEnabled) {
                settingsViewModel.applyCoverTheme(containerSize)
            }

            val currentLanguage by settingsViewModel.currentLanguage.collectAsState()
            LaunchedEffect(currentLanguage) {}

            ScreenEnvironment(
                persistedAppThemeIndex, applyCoverTheme, blackedOutEnabled
            ) { layoutType, isLandscape ->

                val context = LocalContext.current
                NavHost(
                    navController = navController,
                    startDestination = SettingsDestinations.MAIN_SETTINGS_ROUTE
                ) {
                    composable(SettingsDestinations.MAIN_SETTINGS_ROUTE) {
                        SettingsLayout(
                            onNavigateBack = { finish() },
                            viewModel = settingsViewModel,
                            isLandscape = isLandscape,
                            layoutType = layoutType,
                            onNavigateToDeveloperOptions = {
                                val intent = Intent(context, DevSettingsActivity::class.java)
                                context.startActivity(intent)
                            })
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        settingsViewModel.updateCurrentLanguage()
        settingsViewModel.refreshDeveloperModeState()
    }
}
