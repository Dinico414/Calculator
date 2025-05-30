package com.xenon.calculator.ui.layouts.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.xenon.calculator.ui.layouts.CollapsingAppBarLayout
import com.xenon.calculator.ui.values.ExtraLargePadding
import com.xenon.calculator.ui.values.LargeCornerRadius
import com.xenon.calculator.ui.values.LargePadding
import com.xenon.calculator.ui.values.LargeSpacing
import com.xenon.calculator.ui.values.LargerPadding
import com.xenon.calculator.ui.values.MediumCornerRadius
import com.xenon.calculator.viewmodel.SettingsViewModel
import com.xenon.calculator.viewmodel.ThemeSetting


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactSettings(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel,
) {
    val context = LocalContext.current

    val currentThemeTitle by viewModel.currentThemeTitle.collectAsState()
    val showThemeDialog by viewModel.showThemeDialog.collectAsState()
    val themeOptions = viewModel.themeOptions
    val dialogSelectedThemeIndex by viewModel.dialogPreviewThemeIndex.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val showClearDataDialog by viewModel.showClearDataDialog.collectAsState()

    val packageManager = context.packageManager
    val packageName = context.packageName
    val packageInfo = remember {
        try {
            packageManager.getPackageInfo(packageName, 0)
        } catch (e: Exception) {
            null
        }
    }
    val appVersion = packageInfo?.versionName ?: "N/A"

    CollapsingAppBarLayout(
        title = { fontSize, color ->
            Text("Settings", fontSize = fontSize, color = color)
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navigate back")
            }
        }
    ) { paddingValuesFromAppBar ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValuesFromAppBar.calculateTopPadding())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = LargeCornerRadius, topEnd = LargeCornerRadius))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = LargePadding,
                            end = LargePadding,
                            top = LargePadding,
                            bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding() + LargePadding
                        )
                ) {
                    SettingsGroupTile(
                        title = "Theme",
                        subtitle = "Current: $currentThemeTitle",
                        onClick = { viewModel.onThemeSettingClicked() },
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
            }
        }

        if (showThemeDialog) {
            ThemeSelectionDialog(
                themeOptions = themeOptions,
                currentThemeIndex = dialogSelectedThemeIndex,
                onThemeSelected = { index ->
                    viewModel.onThemeOptionSelectedInDialog(index)
                },
                onDismiss = { viewModel.dismissThemeDialog() },
                onConfirm = { viewModel.applySelectedTheme() }
            )
        }

        if (showClearDataDialog) {
            ClearDataConfirmationDialog(
                onConfirm = {
                    viewModel.confirmClearData()
                },
                onDismiss = { viewModel.dismissClearDataDialog() }
            )
        }
    }
}


@Composable
fun SettingsGroupTile(
    title: String,
    subtitle: String,
    onClick: (() -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MediumCornerRadius))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick, role = Role.Button)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = LargerPadding, vertical = ExtraLargePadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title, style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ThemeSelectionDialog(
    themeOptions: Array<ThemeSetting>,
    currentThemeIndex: Int,
    onThemeSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Select Theme") },
        text = {
            Column(Modifier.selectableGroup()) {
                themeOptions.forEachIndexed { index, theme ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (index == currentThemeIndex),
                                onClick = { onThemeSelected(index) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = LargerPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (index == currentThemeIndex),
                            onClick = null
                        )
                        Text(
                            text = theme.title,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = LargerPadding)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ClearDataConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Clear Data") },
        text = { Text(text = "Are you sure you want to clear all app data? This action cannot be undone and will restart the app.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}