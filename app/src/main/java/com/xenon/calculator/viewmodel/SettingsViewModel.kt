package com.xenon.calculator.viewmodel

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process // Added for Process.myPid()
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.unit.IntSize
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.xenon.calculator.R
import com.xenon.calculator.SharedPreferenceManager
import com.xenon.calculator.ui.res.LanguageOption
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
enum class LayoutType {
    COVER, SMALL, COMPACT, MEDIUM, EXPANDED
}

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferenceManager = SharedPreferenceManager(application)
    val themeOptions = ThemeSetting.entries.toTypedArray()

    private val _persistedThemeIndexFlow = MutableStateFlow(sharedPreferenceManager.theme)
    val persistedThemeIndex: StateFlow<Int> = _persistedThemeIndexFlow.asStateFlow()

    private val _dialogPreviewThemeIndex = MutableStateFlow(sharedPreferenceManager.theme)
    val dialogPreviewThemeIndex: StateFlow<Int> = _dialogPreviewThemeIndex.asStateFlow()

    private val _currentThemeTitleFlow =
        MutableStateFlow(themeOptions[sharedPreferenceManager.theme].title)
    val currentThemeTitle: StateFlow<String> = _currentThemeTitleFlow.asStateFlow()

    private val _currentLanguage = MutableStateFlow(getCurrentLocaleDisplayName())
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _showThemeDialog = MutableStateFlow(false)
    val showThemeDialog: StateFlow<Boolean> = _showThemeDialog.asStateFlow()

    private val _showClearDataDialog = MutableStateFlow(false)
    val showClearDataDialog: StateFlow<Boolean> = _showClearDataDialog.asStateFlow()

    private val _showCoverSelectionDialog = MutableStateFlow(false)
    val showCoverSelectionDialog: StateFlow<Boolean> = _showCoverSelectionDialog.asStateFlow()

    private val _enableCoverTheme = MutableStateFlow(sharedPreferenceManager.coverThemeEnabled)
    val enableCoverTheme: StateFlow<Boolean> = _enableCoverTheme.asStateFlow()

    private val _showLanguageDialog = MutableStateFlow(false)
    val showLanguageDialog: StateFlow<Boolean> = _showLanguageDialog.asStateFlow()

    private val _availableLanguages = MutableStateFlow<List<LanguageOption>>(emptyList())
    val availableLanguages: StateFlow<List<LanguageOption>> = _availableLanguages.asStateFlow()

    private val _selectedLanguageTagInDialog = MutableStateFlow(getAppLocaleTag())
    val selectedLanguageTagInDialog: StateFlow<String> = _selectedLanguageTagInDialog.asStateFlow()


    val activeNightModeFlag: StateFlow<Int> = combine(
        _persistedThemeIndexFlow,
        _dialogPreviewThemeIndex,
        _showThemeDialog
    ) { persistedIndex, previewIndex, isDialogShowing ->
        val themeIndexToUse = if (isDialogShowing) {
            previewIndex
        } else {
            persistedIndex
        }
        if (themeIndexToUse >= 0 && themeIndexToUse < themeOptions.size) {
            themeOptions[themeIndexToUse].nightModeFlag
        } else {
            themeOptions.firstOrNull { it == ThemeSetting.SYSTEM }?.nightModeFlag
                ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = themeOptions[sharedPreferenceManager.theme].nightModeFlag
    )


    init {
        viewModelScope.launch {
            activeNightModeFlag.collect { nightMode ->
                AppCompatDelegate.setDefaultNightMode(nightMode)
            }
        }

        viewModelScope.launch {
            _persistedThemeIndexFlow.collect { index ->
                if (index >= 0 && index < themeOptions.size) {
                    _currentThemeTitleFlow.value = themeOptions[index].title
                }
            }
        }
        updateCurrentLanguage()
        prepareLanguageOptions()
    }
    fun onThemeOptionSelectedInDialog(index: Int) {
        if (index >= 0 && index < themeOptions.size) {
            _dialogPreviewThemeIndex.value = index
            _persistedThemeIndexFlow.value = index
        }
    }

    fun applySelectedTheme() {
        val indexToApply = _dialogPreviewThemeIndex.value
        if (indexToApply >= 0 && indexToApply < themeOptions.size) {
            sharedPreferenceManager.theme = indexToApply
            _persistedThemeIndexFlow.value = indexToApply
        }
        _showThemeDialog.value = false
    }

    fun onThemeSettingClicked() {
        _dialogPreviewThemeIndex.value = _persistedThemeIndexFlow.value
        _showThemeDialog.value = true
    }

    fun dismissThemeDialog() {
        _showThemeDialog.value = false
        val index = sharedPreferenceManager.theme
        _persistedThemeIndexFlow.value = index
        _dialogPreviewThemeIndex.value = index
    }

    fun setCoverThemeEnabled(enabled: Boolean) {
        _enableCoverTheme.value = enabled
        sharedPreferenceManager.coverThemeEnabled = enabled
    }

    fun onCoverThemeClicked() {
        _showCoverSelectionDialog.value = true
    }

    fun dismissCoverThemeDialog() {
        _showCoverSelectionDialog.value = false
    }

    fun saveCoverDisplayMetrics(displaySize: IntSize) {
        sharedPreferenceManager.coverDisplaySize = displaySize
        setCoverThemeEnabled(true)
        _showCoverSelectionDialog.value = false
    }

    fun applyCoverTheme(displaySize: IntSize): Boolean {
        return sharedPreferenceManager.isCoverThemeApplied(displaySize)
    }

    fun onClearDataClicked() {
        _showClearDataDialog.value = true
    }

    fun confirmClearData() {
        viewModelScope.launch {
            val context = getApplication<Application>()
            try {
                // Attempt to clear all application data
                val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val success = activityManager.clearApplicationUserData()

                if (success) {
                    // Data cleared successfully.
                    // The app will effectively be in a fresh install state.
                    // Restarting is crucial.

                    // While clearApplicationUserData should reset SharedPreferences,
                    // explicitly setting defaults here can be a fallback
                    // or ensure the UI reflects the reset immediately if the restart is delayed.
                    val defaultThemeIndex = themeOptions.indexOfFirst { it == ThemeSetting.SYSTEM }
                        .takeIf { it != -1 } ?: 0

                    // These lines might become redundant if clearApplicationUserData works fully,
                    // but they ensure the ViewModel's state is updated for any brief moment
                    // before the app fully restarts or if there's a slight delay.
                    sharedPreferenceManager.theme = defaultThemeIndex // Updates the persisted value
                    _persistedThemeIndexFlow.value = defaultThemeIndex // Updates UI-bound flow
                    _dialogPreviewThemeIndex.value = defaultThemeIndex // Updates dialog preview flow

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        setAppLocale("") // Reset locale for pre-Tiramisu
                    }
                    // No need to clear "TodolistPrefs" manually, clearApplicationUserData should handle it.

                    restartApplication(context)
                } else {
                    // Failed to clear data. This might happen if the system prevents it
                    // or due to an issue with the ActivityManager.
                    Toast.makeText(context, context.getString(R.string.error_clearing_data_failed), Toast.LENGTH_LONG).show()
                    // Fallback: guide user to clear data manually via app settings
                    openAppInfo(context)
                }
            } catch (e: SecurityException) {
                // This catch block is important, especially if MANAGE_EXTERNAL_STORAGE is required
                // and not granted, or if the system otherwise denies the operation.
                Toast.makeText(context, context.getString(R.string.error_clearing_data_permission), Toast.LENGTH_LONG).show()
                // Guide user to clear data manually via app settings
                openAppInfo(context)
                e.printStackTrace() // Log the exception for debugging
            } finally {
                _showClearDataDialog.value = false // Ensure dialog is dismissed
            }
        }
    }

    fun dismissClearDataDialog() {
        _showClearDataDialog.value = false
    }


    private fun getCurrentLocaleDisplayName(): String {
        val appLocale = AppCompatDelegate.getApplicationLocales().get(0)
        return appLocale?.displayName ?: getApplication<Application>().getString(R.string.system_default)
    }

    private fun getAppLocaleTag(): String {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        return if (appLocales.isEmpty) "" else appLocales.toLanguageTags()
    }

    fun updateCurrentLanguage() {
        _currentLanguage.value = getCurrentLocaleDisplayName()
        _selectedLanguageTagInDialog.value = getAppLocaleTag()
    }

    private fun prepareLanguageOptions() {
        val application = getApplication<Application>()
        val languages = mutableListOf(
            LanguageOption(
                application.getString(R.string.system_default),
                ""
            )
        )

        // Example: Adding English and German. Populate this from your app's supported languages.
        val en = Locale("en")
        languages.add(LanguageOption(en.getDisplayLanguage(en), en.toLanguageTag()))
        val de = Locale("de")
        languages.add(LanguageOption(de.getDisplayLanguage(de), de.toLanguageTag()))
        // Add other supported languages here

        _availableLanguages.value = languages
    }


    fun onLanguageSettingClicked(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
                setData(Uri.fromParts("package", context.packageName, null))
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Necessary if calling from non-Activity context
            }
            context.startActivity(intent)
        } else {
            _selectedLanguageTagInDialog.value = getAppLocaleTag() // Ensure dialog shows current selection
            _showLanguageDialog.value = true
        }
    }

    fun onLanguageSelectedInDialog(localeTag: String) {
        _selectedLanguageTagInDialog.value = localeTag
    }

    fun applySelectedLanguage() {
        setAppLocale(_selectedLanguageTagInDialog.value)
        _showLanguageDialog.value = false
        updateCurrentLanguage() // Update display name after applying
        restartApplication(getApplication()) // Restart to apply locale change fully
    }

    private fun setAppLocale(localeTag: String) {
        val appLocale: LocaleListCompat = if (localeTag.isEmpty()) {
            LocaleListCompat.getEmptyLocaleList() // For system default
        } else {
            LocaleListCompat.forLanguageTags(localeTag)
        }
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun dismissLanguageDialog() {
        _showLanguageDialog.value = false
        // Reset selection in dialog to currently active language if user cancels
        _selectedLanguageTagInDialog.value = getAppLocaleTag()
    }


    fun openAppInfo(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            setData(Uri.fromParts("package", context.packageName, null))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Necessary if calling from non-Activity context
        }
        context.startActivity(intent)
    }

    fun openImpressum(context: Context) {
        Toast.makeText(context, "xenonware.com/impressum", Toast.LENGTH_LONG).show()

//         val url = "http://xenonware.com/impressum"
//         val intent = Intent(Intent.ACTION_VIEW).apply {
//             data = url.toUri()
//             addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//         }
//         context.startActivity(intent)
    }

    private fun restartApplication(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent?.component
        if (componentName != null) {
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // Ensure stack is cleared
            context.startActivity(mainIntent)
            // Forcefully killing the current process after scheduling a restart
            // can help ensure the old instance is gone.
            Process.killProcess(Process.myPid())
        } else {
            // Fallback or error logging if launch intent can't be found
            Toast.makeText(context, context.getString(R.string.error_restarting_app), Toast.LENGTH_LONG).show()
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