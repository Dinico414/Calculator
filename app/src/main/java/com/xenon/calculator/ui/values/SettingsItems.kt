package com.xenon.calculator.ui.values

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.xenon.calculator.ui.res.SettingsGroupTile
import com.xenon.calculator.ui.res.SettingsSwitchTile
import com.xenon.calculator.viewmodel.SettingsViewModel

@Composable
fun SettingsItems(
    viewModel: SettingsViewModel,
    currentThemeTitle: String,
    applyCoverTheme: Boolean,
    coverThemeEnabled: Boolean,
    currentLanguage: String,
    appVersion: String,
) {
    val context = LocalContext.current

    SettingsGroupTile(
        title = "Theme",
        subtitle = "Current: $currentThemeTitle",
        onClick = { viewModel.onThemeSettingClicked() },
    )
    Spacer(modifier = Modifier.height(LargeSpacing))

    SettingsSwitchTile(
        title = "Cover Screen Theme",
        subtitle = "Select Cover Screen (${if (applyCoverTheme) "Active" else "Inactive"})",
        checked = coverThemeEnabled,
        onCheckedChange = { viewModel.setCoverThemeEnabled(!coverThemeEnabled) },
        onClick = { viewModel.onCoverThemeClicked() },
    )
    Spacer(modifier = Modifier.height(LargeSpacing))

    SettingsGroupTile(
        title = "Language",
        subtitle = "Current: $currentLanguage",
        onClick = { viewModel.openLanguageSettings(context) },
    )
    LaunchedEffect(Unit) {
        viewModel.updateCurrentLanguage()
    }
    Spacer(modifier = Modifier.height(LargeSpacing))

    SettingsGroupTile(
        title = "Data Management",
        subtitle = "Clear app data and cache",
        onClick = { viewModel.onClearDataClicked() },
    )
    Spacer(modifier = Modifier.height(LargeSpacing))

    SettingsGroupTile(
        title = "Version",
        subtitle = "Version $appVersion",
        onClick = null,
    )
}