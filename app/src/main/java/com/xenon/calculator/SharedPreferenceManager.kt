package com.xenon.calculator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class SharedPreferenceManager(context: Context) {

    private val prefsName = "CalculatorPrefs" // Ensure this matches usage in SettingsViewModel
    private val themeKey = "app_theme" // 0: Light, 1: Dark, 2 (or other): System

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    // Default to System theme (index 2 in your ThemeSetting enum)
    var theme: Int
        get() = sharedPreferences.getInt(themeKey, 2)
        set(value) = sharedPreferences.edit().putInt(themeKey, value).apply()

    // themeFlag maps your integer theme value (0, 1, 2) to AppCompatDelegate.MODE_NIGHT_*
    val themeFlag: Array<Int> = arrayOf(
        AppCompatDelegate.MODE_NIGHT_NO,    // Index 0: Light
        AppCompatDelegate.MODE_NIGHT_YES,   // Index 1: Dark
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM // Index 2: System
    )
}