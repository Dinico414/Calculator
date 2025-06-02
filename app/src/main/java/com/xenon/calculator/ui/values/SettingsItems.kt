package com.xenon.calculator.ui.values

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape // Keep this
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp // Keep this
import com.xenon.calculator.R
import com.xenon.calculator.ui.res.SettingsSwitchTile
import com.xenon.calculator.ui.res.SettingsTile
import com.xenon.calculator.viewmodel.SettingsViewModel


@Composable
fun SettingsItems(
    viewModel: SettingsViewModel,
    currentThemeTitle: String,
    applyCoverTheme: Boolean,
    coverThemeEnabled: Boolean,
    currentLanguage: String,
    appVersion: String,
    innerGroupRadius: Dp = SmallestCornerRadius,
    outerGroupRadius: Dp = MediumCornerRadius,
    innerGroupSpacing: Dp = MediumSpacing,
    outerGroupSpacing: Dp = LargeSpacing
) {
    val context = LocalContext.current

    val innerGroupShapeTop = RoundedCornerShape(bottomStart = innerGroupRadius, bottomEnd = innerGroupRadius, topStart = outerGroupRadius, topEnd = outerGroupRadius)
    val innerGroupShapeCenter = RoundedCornerShape(topStart = innerGroupRadius, topEnd = innerGroupRadius, bottomStart = innerGroupRadius, bottomEnd = innerGroupRadius)
    val innerGroupShapeBottom = RoundedCornerShape(topStart = innerGroupRadius, topEnd = innerGroupRadius, bottomStart = outerGroupRadius, bottomEnd = outerGroupRadius)

    val standaloneItemShape = RoundedCornerShape(outerGroupRadius)

    SettingsTile(
        title = stringResource(id = R.string.theme),
        subtitle = "${stringResource(id = R.string.current)} $currentThemeTitle",
        onClick = { viewModel.onThemeSettingClicked() },
        shape = innerGroupShapeTop
    )

    Spacer(modifier = Modifier.height(innerGroupSpacing))

    SettingsSwitchTile(
        title = stringResource(id = R.string.cover_screen_mode),
        subtitle = "${stringResource(id = R.string.cover_screen_mode_description)} (${if (applyCoverTheme) stringResource(id = R.string.enabled) else stringResource(id = R.string.disabled)})",
        checked = coverThemeEnabled,
        onCheckedChange = { viewModel.setCoverThemeEnabled(!coverThemeEnabled) },
        onClick = { viewModel.onCoverThemeClicked() },
        shape = innerGroupShapeBottom
    )

    Spacer(modifier = Modifier.height(outerGroupSpacing))

    SettingsTile(
        title = stringResource(id = R.string.language),
        subtitle = "${stringResource(id = R.string.current)} $currentLanguage",
        onClick = { viewModel.openLanguageSettings(context) },
        shape = standaloneItemShape
    )
    LaunchedEffect(Unit) {
        viewModel.updateCurrentLanguage()
    }

    Spacer(modifier = Modifier.height(outerGroupSpacing))

    SettingsTile(
        title = stringResource(id = R.string.clear_data),
        subtitle = stringResource(id = R.string.clear_data_description),
        onClick = { viewModel.onClearDataClicked() },
        shape = innerGroupShapeTop
    )

    Spacer(modifier = Modifier.height(innerGroupSpacing))

    SettingsTile(
        title = "Version",
        subtitle = "Version $appVersion",
        onClick = null,
        shape = innerGroupShapeBottom
    )

}