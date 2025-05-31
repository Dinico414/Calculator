package com.xenon.calculator.ui.layouts.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.xenon.calculator.ui.layouts.CollapsingAppBarLayout
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
    val showCoverSelectionDialog by viewModel.showCoverSelectionDialog.collectAsState()
    val coverThemeEnabled by viewModel.enableCoverTheme.collectAsState()

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

    val containerSize = LocalWindowInfo.current.containerSize
    val applyCoverTheme = remember(containerSize, coverThemeEnabled) {
        viewModel.applyCoverTheme(containerSize)
    }

    CollapsingAppBarLayout(
        title = { fontSize, textColor ->
            Text(
                "Settings",
                fontSize = fontSize,
                color = textColor
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back"
                )
            }
        },
        expandable = false,
        expandedContainerColor = Color.Black,
        collapsedContainerColor = Color.Black,
        collapsedTextColor = Color.White,
        navigationIconContentColor = Color.White
    ) { paddingValuesFromAppBar ->
        Column(
            modifier = Modifier
                .padding(paddingValuesFromAppBar)
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

            CoverSwitchTile(
                title = "Cover Screen Theme",
                subtitle = "Select Cover Screen (${if (applyCoverTheme) "Active" else "Inactive"})",
                checked = coverThemeEnabled,
                onCheckedChange = { viewModel.setCoverThemeEnabled(!coverThemeEnabled) },
                onClick = { viewModel.onCoverThemeClicked() },
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

            if (showCoverSelectionDialog) {
                CoverCoverDisplaySelectionDialog(onConfirm = {
                    viewModel.saveCoverDisplayMetrics(
                        containerSize
                    )
                }, onDismiss = { viewModel.dismissCoverThemeDialog() })
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
fun CoverSwitchTile(
    title: String,
    subtitle: String = "",
    checked: Boolean = false,
    onCheckedChange: ((enabled: Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
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
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title, style = MaterialTheme.typography.titleMedium, color = Color.White
            )
            Text(
                text = subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.White
            )
        }
        VerticalDivider()
        Switch(
            checked = checked, onCheckedChange = onCheckedChange
        )
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
fun CoverCoverDisplaySelectionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        containerColor = Color.Black,
        onDismissRequest = onDismiss,
        title = { Text(text = "Is the current display the cover screen?") },
        text = {
            val containerSize = LocalWindowInfo.current.containerSize
            Column(Modifier.selectableGroup()) {
                Text("Will apply a custom more compact theme when on Cover Display.")
                Spacer(modifier = Modifier.height(2.dp))
                Text("Screen size: ${containerSize.width}x${containerSize.height} px")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
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