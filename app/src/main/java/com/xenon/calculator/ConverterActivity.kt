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
    // activeThemeForConverterActivity will be initialized in onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This is key: Allows content to draw behind system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedPreferenceManager = SharedPreferenceManager(applicationContext)

        converterViewModel = ViewModelProvider(
            this, ConverterViewModel.ConverterViewModelFactory(application)
        )[ConverterViewModel::class.java]

        val activeThemeForConverterActivity = sharedPreferenceManager.theme

        setContent {
            ScreenEnvironment(themePreference = activeThemeForConverterActivity) { layoutType, isLandscape ->
                // ConverterLayout will now be responsible for handling insets internally
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
        // It seems like there's a small typo here, should likely be:
        // val currentActivityTheme = sharedPreferenceManager.theme // This line is redundant
        // if (activeThemeForConverterActivity != storedTheme) { // Assuming activeThemeForConverterActivity is a member variable
        // For now, I'll keep your original logic, but you might want to review it.
        val currentActivityTheme = sharedPreferenceManager.theme
        if (currentActivityTheme != storedTheme) {
            recreate()
        }
    }
}