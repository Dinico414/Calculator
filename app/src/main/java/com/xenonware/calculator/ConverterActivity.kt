package com.xenonware.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.ViewModelProvider
import com.xenonware.calculator.ui.layouts.ConverterLayout
import com.xenonware.calculator.ui.theme.ScreenEnvironment
import com.xenonware.calculator.viewmodel.ConverterViewModel

class ConverterActivity : ComponentActivity() {

    private lateinit var converterViewModel: ConverterViewModel
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private var lastAppliedTheme: Int = -1
    private var lastAppliedCoverThemeEnabled: Boolean =
        false
    private var lastAppliedBlackedOutMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferenceManager = SharedPreferenceManager(applicationContext)

        converterViewModel = ViewModelProvider(
            this, ConverterViewModel.ConverterViewModelFactory(application)
        )[ConverterViewModel::class.java]

        val activeThemeForConverterActivity = sharedPreferenceManager.theme

        enableEdgeToEdge()

        setContent {
            val currentContext = LocalContext.current
            val currentContainerSize = LocalWindowInfo.current.containerSize
            val applyCoverTheme =
                sharedPreferenceManager.isCoverThemeApplied(currentContainerSize)

            ScreenEnvironment(
                themePreference = lastAppliedTheme,
                coverTheme = applyCoverTheme, // Use the dynamically calculated value
                blackedOutModeEnabled = lastAppliedBlackedOutMode
            ) { layoutType, isLandscape ->
                ConverterLayout(
                    onNavigateBack = { finish() },
                    viewModel = converterViewModel,
                    isLandscape = isLandscape,
                    layoutType = layoutType
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val storedTheme = sharedPreferenceManager.theme
         val currentActivityTheme = sharedPreferenceManager.theme
        if (currentActivityTheme != storedTheme) {
            recreate()
        }
    }
}