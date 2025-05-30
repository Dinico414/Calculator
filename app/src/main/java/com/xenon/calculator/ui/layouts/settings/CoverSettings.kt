package com.xenon.calculator.ui.layouts.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.xenon.calculator.ui.values.ExtraLargePadding
import com.xenon.calculator.ui.values.LargePadding
import com.xenon.calculator.ui.values.LargerPadding
import com.xenon.calculator.ui.values.LargerSpacing
import com.xenon.calculator.ui.values.MediumPadding
import com.xenon.calculator.viewmodel.SettingsViewModel
import com.xenon.calculator.viewmodel.ThemeSetting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoverSettings(
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

    Scaffold(
        containerColor = Color.Black, topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(vertical = MediumPadding)
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            CoverSettingsGroupTile(
                title = "Theme",
                subtitle = "Current: $currentThemeTitle",
                onClick = { viewModel.onThemeSettingClicked() },
            )

            CoverSettingsGroupTile(
                title = "Language",
                subtitle = "Current: $currentLanguage",
                onClick = { viewModel.openLanguageSettings(context) },
            )
            LaunchedEffect(Unit) {
                viewModel.updateCurrentLanguage()
            }

            CoverSettingsGroupTile(
                title = "Data Management",
                subtitle = "Clear app data and cache",
                onClick = { viewModel.onClearDataClicked() },
            )

            CoverSettingsGroupTile(
                title = "Version",
                subtitle = "Version $appVersion",
                onClick = null,
            )


            if (showThemeDialog) {
                CoverThemeSelectionDialog(
                    themeOptions = themeOptions,
                    currentThemeIndex = dialogSelectedThemeIndex,
                    onThemeSelected = { index ->
                        viewModel.onThemeOptionSelectedInDialog(index)
                    },
                    onDismiss = { viewModel.dismissThemeDialog() },
                    onConfirm = { viewModel.applySelectedTheme() })
            }

            if (showClearDataDialog) {
                CoverClearDataConfirmationDialog(
                    onConfirm = { viewModel.confirmClearData() },
                    onDismiss = { viewModel.dismissClearDataDialog() })
            }
        }
    }
}

@Composable
fun CoverSettingsGroupTile(
    title: String,
    subtitle: String,
    onClick: (() -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LargePadding)
            .background(Color.Black)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick, role = Role.Button)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = LargerSpacing, vertical = ExtraLargePadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title, style = MaterialTheme.typography.titleMedium, color = Color.White
            )
            Text(
                text = subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.White
            )
        }
    }
}

@Composable
fun CoverThemeSelectionDialog(
    themeOptions: Array<ThemeSetting>,
    currentThemeIndex: Int,
    onThemeSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        containerColor = Color.Black,
        onDismissRequest = onDismiss,
        title = { Text(text = "Select Theme", color = Color.White) },
        text = {
            Column(
                Modifier
                    .selectableGroup()
                    .background(Color.Black)
            ) {
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
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White,
                                unselectedColor = Color.Gray,
                                disabledSelectedColor = Color.DarkGray,
                                disabledUnselectedColor = Color.DarkGray
                            )
                        )
                        Text(
                            text = theme.title,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = LargerPadding),
                            color = Color.White
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White)
            }
        })
}

@Composable
fun CoverClearDataConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        containerColor = Color.Black,
        onDismissRequest = onDismiss,
        title = { Text(text = "Clear Data", color = Color.White) },
        text = {
            Text(
                text = "Are you sure you want to clear all app data? This action cannot be undone and will restart the app.",
                color = Color.White
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White)
            }
        })
}