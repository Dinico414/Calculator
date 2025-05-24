package com.xenon.calculator // Or your activity package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.xenon.calculator.ui.layouts.converter.CompactConverterScreen
import com.xenon.calculator.ui.theme.CalculatorTheme // Assuming you use the same theme

class ConverterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // You might want to apply WindowCompat.setDecorFitsSystemWindows(window, false) here too
        // and potentially use SharedPreferenceManager for theme consistency if needed,
        // similar to MainActivity, or simplify if ConverterActivity always follows system theme.

        setContent {
            // Determine darkTheme based on your app's logic for this activity
            // For simplicity, using isSystemInDarkTheme() or a fixed value.
            // Or, pass theme choice via Intent from MainActivity if needed.
            val appIsDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()

            CalculatorTheme(darkTheme = appIsDarkTheme) { // Or your specific theme setup
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompactConverterScreen() // Your converter screen composable
                }
            }
        }
    }
}