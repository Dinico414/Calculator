package com.xenon.calculator

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.area.WindowAreaController
import androidx.window.area.WindowAreaInfo
import androidx.window.core.ExperimentalWindowApi
import com.xenon.calculator.ui.layouts.SettingsLayout
import com.xenon.calculator.ui.theme.ScreenEnvironment
import com.xenon.calculator.viewmodel.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest

class SettingsActivity : ComponentActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    @OptIn(ExperimentalWindowApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.SettingsViewModelFactory(application)
        )[SettingsViewModel::class.java]

        enableEdgeToEdge()

        setContent {
            val info = WindowAreaController.getOrCreate().windowAreaInfos.collectAsState(emptyList())
            if (info.value.isNotEmpty()) {
                val i = info.value.last()
                Toast.makeText(this, i.toString(), Toast.LENGTH_LONG).show()
            }

            val persistedAppThemeIndex by settingsViewModel.persistedThemeIndex.collectAsState()

            ScreenEnvironment(themePreference = persistedAppThemeIndex) { layoutType, isLandscape ->
                SettingsLayout(
                    onNavigateBack = { finish() },
                    viewModel = settingsViewModel,
                    isLandscape = isLandscape,
                    layoutType = layoutType
                )
            }
        }
    }
}