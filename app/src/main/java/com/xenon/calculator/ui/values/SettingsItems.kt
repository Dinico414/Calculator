package com.xenon.calculator.ui.values

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.xenon.calculator.R
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
        title = stringResource(id = R.string.theme),
        subtitle = "${stringResource(id = R.string.current)} $currentThemeTitle",
        onClick = { viewModel.onThemeSettingClicked() },
    )
    Spacer(modifier = Modifier.height(LargeSpacing))

    SettingsSwitchTile(
        title = stringResource(id = R.string.cover_screen_mode),
        subtitle = "${stringResource(id = R.string.cover_screen_mode_description)} (${if (applyCoverTheme) stringResource(id = R.string.enabled) else stringResource(id = R.string.disabled)})",
        checked = coverThemeEnabled,
        onCheckedChange = { viewModel.setCoverThemeEnabled(!coverThemeEnabled) },
        onClick = { viewModel.onCoverThemeClicked() },
    )
    Spacer(modifier = Modifier.height(LargeSpacing))

    SettingsGroupTile(
        title = stringResource(id = R.string.language),
        subtitle = "${stringResource(id = R.string.current)} $currentLanguage",
        onClick = { viewModel.openLanguageSettings(context) },
    )
    LaunchedEffect(Unit) {
        viewModel.updateCurrentLanguage()
    }
    Spacer(modifier = Modifier.height(LargeSpacing))

    SettingsGroupTile(
        title = stringResource(id = R.string.clear_data),
        subtitle = stringResource(id = R.string.clear_data_description),
        onClick = { viewModel.onClearDataClicked() },
    )
    Spacer(modifier = Modifier.height(LargeSpacing))

    SettingsGroupTile(
        title = "Version",
        subtitle = "Version $appVersion",
        onClick = null,
    )
}