package com.xenon.calculator.ui.layouts.settings

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xenon.calculator.ui.layouts.CollapsingAppBarLayout
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.SettingsViewModel
import com.xenon.calculator.viewmodel.ThemeSetting


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactSettings(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel,
) {
    val context = LocalContext.current

    val currentThemeTitle by viewModel.currentThemeTitle
    val showThemeDialog by viewModel.showThemeDialog
    val themeOptions = viewModel.themeOptions
    val selectedThemeIndex by viewModel.selectedThemeIndex
    val currentLanguage by viewModel.currentLanguage
    val showClearDataDialog by viewModel.showClearDataDialog

    CollapsingAppBarLayout(
        title = { fontSize, color ->
            Text("Settings", fontSize = fontSize, color = color)
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navigate back")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                ) {
                    SettingsGroupTile(
                        title = "Theme",
                        subtitle = "Current: $currentThemeTitle",
                        onClick = { viewModel.onThemeSettingClicked() },
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    SettingsGroupTile(
                        title = "Language",
                        subtitle = "Current: $currentLanguage",
                        onClick = { viewModel.openLanguageSettings(context) },
                    )
                    LaunchedEffect(Unit) {
                        viewModel.updateCurrentLanguage()
                    }
                    Spacer(modifier = Modifier.height(5.dp))

                    SettingsGroupTile(
                        title = "Data Management",
                        subtitle = "Clear app data and cache",
                        onClick = { viewModel.onClearDataClicked() },
                    )
                }
            }
        }
        if (showThemeDialog) {
            ThemeSelectionDialog(
                themeOptions = themeOptions,
                currentThemeIndex = selectedThemeIndex,
                onThemeSelected = { index -> viewModel.onThemeSelected(index) },
                onDismiss = { viewModel.dismissThemeDialog() },
                onConfirm = { viewModel.applyTheme() }
            )
        }

        if (showClearDataDialog) {
            ClearDataConfirmationDialog(
                onConfirm = { viewModel.confirmClearData() },
                onDismiss = { viewModel.dismissClearDataDialog() }
            )
        }
    }
}

@Composable
fun SettingsGroupTile(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .clickable(onClick = onClick, role = Role.Button)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
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
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (index == currentThemeIndex),
                            onClick = null
                        )
                        Text(
                            text = theme.title,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
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

@Preview(showBackground = true)
@Composable
fun CompactSettingsPreview() {
    val context = LocalContext.current
    val previewViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.SettingsViewModelFactory(context.applicationContext as Application)
    )
    CalculatorTheme {
        CompactSettings(onNavigateBack = {}, viewModel = previewViewModel)
    }
}