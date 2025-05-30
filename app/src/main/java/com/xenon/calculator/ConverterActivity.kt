package com.xenon.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.xenon.calculator.ui.layouts.ConverterLayout
import com.xenon.calculator.ui.theme.ScreenEnvironment
import com.xenon.calculator.viewmodel.ConverterViewModel

class ConverterActivity : ComponentActivity() {

    private lateinit var converterViewModel: ConverterViewModel
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false) // Correctly set for edge-to-edge
        sharedPreferenceManager = SharedPreferenceManager(applicationContext)

        converterViewModel = ViewModelProvider(
            this, ConverterViewModel.ConverterViewModelFactory(application)
        )[ConverterViewModel::class.java]

        val activeThemeForConverterActivity = sharedPreferenceManager.theme

        setContent {
            ScreenEnvironment(themePreference = activeThemeForConverterActivity) { layoutType, isLandscape ->
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