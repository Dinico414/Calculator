package com.xenon.calculator.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.xenon.calculator.SharedPreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    private val _persistedThemeIndexFlow = MutableStateFlow(sharedPreferenceManager.theme)
    val persistedThemeIndex: StateFlow<Int> = _persistedThemeIndexFlow.asStateFlow()

    private val _dialogPreviewThemeIndex = MutableStateFlow(sharedPreferenceManager.theme)
    val dialogPreviewThemeIndex: StateFlow<Int> = _dialogPreviewThemeIndex.asStateFlow()

    private val _currentThemeTitleFlow = MutableStateFlow(themeOptions[sharedPreferenceManager.theme].title)
    val currentThemeTitle: StateFlow<String> = _currentThemeTitleFlow.asStateFlow()

    private val _currentLanguage = MutableStateFlow(Locale.getDefault().displayLanguage)
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _showThemeDialog = MutableStateFlow(false)
    val showThemeDialog: StateFlow<Boolean> = _showThemeDialog.asStateFlow()

    private val _showClearDataDialog = MutableStateFlow(false)
    val showClearDataDialog: StateFlow<Boolean> = _showClearDataDialog.asStateFlow()

    // --- NEW: Flow for the currently active theme's night mode flag ---
    // This will drive the actual theme application in Compose
    val activeNightModeFlag: StateFlow<Int> = combine(
        _persistedThemeIndexFlow,
        _dialogPreviewThemeIndex,
        _showThemeDialog
    ) { persistedIndex, previewIndex, isDialogShowing ->
        val themeIndexToUse = if (isDialogShowing) {
            previewIndex // Use preview theme if dialog is showing
        } else {
            persistedIndex // Otherwise, use the persisted theme
        }
        if (themeIndexToUse >= 0 && themeIndexToUse < themeOptions.size) {
            themeOptions[themeIndexToUse].nightModeFlag
        } else {
            // Fallback to a default if index is somehow out of bounds
            themeOptions.firstOrNull { it == ThemeSetting.SYSTEM }?.nightModeFlag
                ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = themeOptions[sharedPreferenceManager.theme].nightModeFlag // Initial value
    )


    init {
        // Apply the active night mode flag whenever it changes
        viewModelScope.launch {
            activeNightModeFlag.collect { nightMode ->
                // This is the single point where we command the theme change
                AppCompatDelegate.setDefaultNightMode(nightMode)
            }
        }

        // Update current theme title based on the persisted theme
        viewModelScope.launch {
            _persistedThemeIndexFlow.collect { index ->
                if (index >= 0 && index < themeOptions.size) {
                    _currentThemeTitleFlow.value = themeOptions[index].title
                }
            }
        }
        updateCurrentLanguage()
    }

    fun onThemeOptionSelectedInDialog(index: Int) {
        if (index >= 0 && index < themeOptions.size) {
            _dialogPreviewThemeIndex.value = index
            // The 'activeNightModeFlag' flow will automatically update and trigger theme change
        }
    }

    fun applySelectedTheme() {
        val indexToApply = _dialogPreviewThemeIndex.value
        if (indexToApply >= 0 && indexToApply < themeOptions.size) {
            sharedPreferenceManager.theme = indexToApply
            _persistedThemeIndexFlow.value = indexToApply // Update persisted theme
        }
        _showThemeDialog.value = false // This will also cause 'activeNightModeFlag' to switch to persisted
    }

    fun updateCurrentLanguage() {
        _currentLanguage.value = Locale.getDefault().displayLanguage
    }

    fun onThemeSettingClicked() {
        _dialogPreviewThemeIndex.value = _persistedThemeIndexFlow.value // Start preview with current persisted
        _showThemeDialog.value = true // This will cause 'activeNightModeFlag' to switch to preview
    }

    fun dismissThemeDialog() {
        _showThemeDialog.value = false // This will cause 'activeNightModeFlag' to switch to persisted
        // No need to manually revert AppCompatDelegate, 'activeNightModeFlag' handles it by
        // falling back to _persistedThemeIndexFlow when dialog is not shown.
        // We can ensure the dialog preview is reset if needed for next time.
        _dialogPreviewThemeIndex.value = _persistedThemeIndexFlow.value
    }

    fun onClearDataClicked() {
        _showClearDataDialog.value = true
    }

    fun confirmClearData() {
        viewModelScope.launch {
            val customPrefs = getApplication<Application>().getSharedPreferences(
                "CalculatorPrefs",
                Context.MODE_PRIVATE
            )
            customPrefs.edit { clear() }

            val defaultThemeIndex = themeOptions.indexOfFirst { it == ThemeSetting.SYSTEM }
                .takeIf { it != -1 } ?: 0

            sharedPreferenceManager.theme = defaultThemeIndex
            _persistedThemeIndexFlow.value = defaultThemeIndex
            // _dialogPreviewThemeIndex will be updated by activeNightModeFlag logic if dialog was open
            // or by onThemeSettingClicked next time. Better to keep it consistent:
            _dialogPreviewThemeIndex.value = defaultThemeIndex

            restartApplication(getApplication()) // This will inherently re-apply the new default
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
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent?.component
        if (componentName != null) {
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            context.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
    }

    class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}