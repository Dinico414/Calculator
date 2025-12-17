package com.xenonware.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.xenonware.calculator.data.SharedPreferenceManager
import com.xenonware.calculator.ui.layouts.ConverterLayout
import com.xenonware.calculator.ui.theme.ScreenEnvironment
import com.xenonware.calculator.viewmodel.ConverterViewModel

class ConverterActivity : ComponentActivity() {

    private lateinit var viewModel: ConverterViewModel

    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    private var lastAppliedTheme: Int = -1
    private var lastAppliedCoverThemeEnabled: Boolean = false
    private var lastAppliedBlackedOutMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedPreferenceManager = SharedPreferenceManager(applicationContext)
        viewModel = ViewModelProvider(
            this, ConverterViewModel.ConverterViewModelFactory(application)
        )[ConverterViewModel::class.java]

        val initialThemePref = sharedPreferenceManager.theme
        val initialCoverThemeEnabledSetting = sharedPreferenceManager.coverThemeEnabled
        val initialBlackedOutMode = sharedPreferenceManager.blackedOutModeEnabled

        updateAppCompatDelegateTheme(initialThemePref)

        lastAppliedTheme = initialThemePref
        lastAppliedCoverThemeEnabled = initialCoverThemeEnabledSetting
        lastAppliedBlackedOutMode = initialBlackedOutMode

        enableEdgeToEdge()

        setContent {
            val currentContainerSize = LocalWindowInfo.current.containerSize
            val applyCoverTheme = sharedPreferenceManager.isCoverThemeApplied(currentContainerSize)

            ScreenEnvironment(
                themePreference = lastAppliedTheme,
                coverTheme = applyCoverTheme,
                blackedOutModeEnabled = lastAppliedBlackedOutMode
            ) { layoutType, isLandscape ->
                ConverterLayout(
                    onNavigateBack = { finish() },
                    viewModel = viewModel,
                    isLandscape = isLandscape,
                    layoutType = layoutType,
                )
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