package com.xenon.calculator.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    val themeOptions = ThemeSetting.entries.toTypedArray()

    private val _currentThemeStateUpdated = MutableStateFlow(Unit) // For SettingsActivity immediate UI update
    val currentThemeStateUpdated: StateFlow<Unit> = _currentThemeStateUpdated.asStateFlow()

    // --- Current Language ---
    private val _currentLanguage = mutableStateOf(Locale.getDefault().displayLanguage)
    val currentLanguage: State<String> = _currentLanguage
    // Updated via updateCurrentLanguage()

    // --- Selected Theme Index (for the dialog) ---
    private val _selectedThemeIndex = mutableIntStateOf(sharedPreferenceManager.theme)
    val selectedThemeIndex: State<Int> = _selectedThemeIndex
    // Setter is internal to ViewModel via onThemeSelected or init/dismiss

    // --- Current Theme Title (reflects applied theme) ---
    private val _currentThemeTitle = mutableStateOf(themeOptions[sharedPreferenceManager.theme].title)
    val currentThemeTitle: State<String> = _currentThemeTitle
    // Setter is internal to ViewModel via applyTheme or init/dismiss

    // --- Show Theme Dialog ---
    private val _showThemeDialog = mutableStateOf(false)
    val showThemeDialog: State<Boolean> = _showThemeDialog
    // Setter is internal to ViewModel

    // --- Show Clear Data Dialog ---
    private val _showClearDataDialog = mutableStateOf(false)
    val showClearDataDialog: State<Boolean> = _showClearDataDialog
    // Setter is internal to ViewModel

    init {
        // Ensure ViewModel starts with the correct theme title & index from SharedPreferences
        _currentThemeTitle.value = themeOptions[sharedPreferenceManager.theme].title
        _selectedThemeIndex.value = sharedPreferenceManager.theme
        updateCurrentLanguage() // Set initial language
    }

    fun applyTheme() {
        _showThemeDialog.value = false
        val index = _selectedThemeIndex.value
        sharedPreferenceManager.theme = index
        AppCompatDelegate.setDefaultNightMode(themeOptions[index].nightModeFlag)
    }

    fun updateCurrentLanguage() {
        _currentLanguage.value = Locale.getDefault().displayLanguage
    }

    fun onThemeSettingClicked() {
        // When opening the dialog, ensure selectedThemeIndex reflects the persisted theme
        _selectedThemeIndex.value = sharedPreferenceManager.theme
        // _currentThemeTitle reflects the *applied* theme, so it shouldn't change here until applyTheme
        _showThemeDialog.value = true
    }

    fun onThemeSelected(index: Int) {
        _selectedThemeIndex.value = index
        AppCompatDelegate.setDefaultNightMode(themeOptions[index].nightModeFlag)
        _currentThemeTitle.value = themeOptions[index].title
        _currentThemeStateUpdated.value = Unit // Signal that theme state was updated for SettingsActivity
    }

    fun dismissThemeDialog() {
        _showThemeDialog.value = false
        val index = sharedPreferenceManager.theme
        _currentThemeTitle.value = themeOptions[index].title
        // If user dismissed without applying, reset selectedThemeIndex to actual persisted theme
        _selectedThemeIndex.value = index
    }

    fun onClearDataClicked() {
        _showClearDataDialog.value = true
    }

    fun confirmClearData() {
        viewModelScope.launch {
            // Clear app-specific SharedPreferences (the one used by SharedPreferenceManager)
            val customPrefs = getApplication<Application>().getSharedPreferences(
                "CalculatorPrefs", // Make sure this matches SharedPreferenceManager
                Context.MODE_PRIVATE
            )
            customPrefs.edit().clear().apply()

            // Optional: Clear default SharedPreferences if your app uses them for other things
            // val defaultAppSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
            // defaultAppSharedPreferences.edit().clear().apply()

            // After clearing, re-initialize theme in ViewModel to default (or whatever is appropriate)
            // and restart.
            // For example, if system theme (index 2) is your default after clearing:
            sharedPreferenceManager.theme = 2 // Assuming 2 is "System"
            _selectedThemeIndex.value = sharedPreferenceManager.theme
            _currentThemeTitle.value = themeOptions[sharedPreferenceManager.theme].title
            AppCompatDelegate.setDefaultNightMode(themeOptions[sharedPreferenceManager.theme].nightModeFlag)

            restartApplication(getApplication())
        }
        _showClearDataDialog.value = false
    }

    fun dismissClearDataDialog() {
        _showClearDataDialog.value = false
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
        Runtime.getRuntime().exit(0) // Ensure the old process is killed
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