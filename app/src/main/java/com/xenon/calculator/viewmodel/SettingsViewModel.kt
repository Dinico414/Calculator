package com.xenon.calculator.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.xenon.calculator.SharedPreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

enum class ThemeSetting(val title: String, val nightModeFlag: Int) {
    LIGHT("Light", AppCompatDelegate.MODE_NIGHT_NO),
    DARK("Dark", AppCompatDelegate.MODE_NIGHT_YES),
    SYSTEM("System", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
}

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferenceManager = SharedPreferenceManager(application)
    private val themeOptions = ThemeSetting.entries.toTypedArray()

    // To signal MainActivity to recreate for theme changes
    private val _themeChanged = MutableStateFlow(false)
    val themeChanged: StateFlow<Boolean> = _themeChanged.asStateFlow()

    fun onThemeApplied() {
        _themeChanged.value = false // Reset after handling
    }

    fun applyTheme() {
        sharedPreferenceManager.theme = selectedThemeIndex
        // This line is crucial for the system to know the new theme preference
        AppCompatDelegate.setDefaultNightMode(themeOptions[selectedThemeIndex].nightModeFlag)
        currentThemeTitle = themeOptions[selectedThemeIndex].title
        showThemeDialog = false
        _themeChanged.value = true // Signal that the theme has changed
    }

    var currentLanguage by mutableStateOf(Locale.getDefault().displayLanguage)
        private set

    var selectedThemeIndex by mutableIntStateOf(sharedPreferenceManager.theme)
        private set

    var currentThemeTitle by mutableStateOf(themeOptions[sharedPreferenceManager.theme].title)
        private set

    var showThemeDialog by mutableStateOf(false)
    var showClearDataDialog by mutableStateOf(false)


    fun updateCurrentLanguage() {
        currentLanguage = Locale.getDefault().displayLanguage
    }
    fun onThemeSettingClicked() {
        selectedThemeIndex = sharedPreferenceManager.theme
        showThemeDialog = true
    }

    fun onThemeSelected(index: Int) {
        selectedThemeIndex = index
    }

    fun dismissThemeDialog() {
        showThemeDialog = false
    }
    fun onClearDataClicked() {
        showClearDataDialog = true
    }

    fun confirmClearData() {
        viewModelScope.launch {
            val appSharedPreferences = getApplication<Application>().getSharedPreferences(
                getApplication<Application>().packageName, Context.MODE_PRIVATE
            )
            appSharedPreferences.edit { clear() }
            restartApplication(getApplication())
        }
        showClearDataDialog = false
    }

    fun dismissClearDataDialog() {
        showClearDataDialog = false
    }

    fun openLanguageSettings(context: Context) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun restartApplication(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent?.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }

    class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}