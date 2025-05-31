package com.xenon.calculator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.unit.IntSize
import androidx.core.content.edit

class SharedPreferenceManager(context: Context) {

    private val prefsName = "CalculatorPrefs" // Ensure this matches usage in SettingsViewModel
    private val themeKey = "app_theme" // 0: Light, 1: Dark, 2 (or other): System
    private val coverThemeEnabledKey = "cover_theme_enabled"
    private val coverDisplaySizeWKey = "cover_display_size_w"
    private val coverDisplaySizeHKey = "cover_display_size_h"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    // Default to System theme (index 2 in your ThemeSetting enum)
    var theme: Int
        get() = sharedPreferences.getInt(themeKey, 2)
        set(value) = sharedPreferences.edit { putInt(themeKey, value) }

    // themeFlag maps your integer theme value (0, 1, 2) to AppCompatDelegate.MODE_NIGHT_*
    val themeFlag: Array<Int> = arrayOf(
        AppCompatDelegate.MODE_NIGHT_NO,    // Index 0: Light
        AppCompatDelegate.MODE_NIGHT_YES,   // Index 1: Dark
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM // Index 2: System
    )

    var coverThemeEnabled: Boolean
        get() = sharedPreferences.getBoolean(coverThemeEnabledKey, false)
        set(value) = sharedPreferences.edit { putBoolean(coverThemeEnabledKey, value) }

    var coverDisplaySize: IntSize
        get() = IntSize(
            sharedPreferences.getInt(coverDisplaySizeWKey, 0),
            sharedPreferences.getInt(coverDisplaySizeHKey, 0)
        )
        set(value) {
            sharedPreferences.edit {
                putInt(coverDisplaySizeWKey, value.width)
                putInt(coverDisplaySizeHKey, value.height)
            }
        }

    fun isCoverThemeApplied(displaySize: IntSize): Boolean {
        return coverThemeEnabled && displaySize == coverDisplaySize
    }
}