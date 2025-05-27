package com.xenon.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.xenon.calculator.ui.theme.ScreenEnvironment // Import
import com.xenon.calculator.ui.layouts.ConverterLayout
import com.xenon.calculator.viewmodel.ConverterViewModel

class ConverterActivity : ComponentActivity() {

    private lateinit var converterViewModel: ConverterViewModel
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    // activeThemeForConverterActivity will be initialized in onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedPreferenceManager = SharedPreferenceManager(applicationContext)

        converterViewModel = ViewModelProvider(
            this,
            ConverterViewModel.ConverterViewModelFactory(application)
        )[ConverterViewModel::class.java]

        val activeThemeForConverterActivity = sharedPreferenceManager.theme // 0, 1, or 2 (system)

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
        // Recreate if theme changed, MainActivity handles this well.
        // You might need similar logic if theme can change while ConverterActivity is visible.
        val storedTheme = sharedPreferenceManager.theme
        val currentActivityTheme = sharedPreferenceManager.theme // re-fetch or use a member variable
        if (currentActivityTheme != storedTheme) { // Simple check, might need to store initial theme
            recreate()
        }
    }
}