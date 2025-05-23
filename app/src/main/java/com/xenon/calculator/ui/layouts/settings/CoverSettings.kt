package com.xenon.calculator.ui.layouts.settings

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xenon.calculator.ui.theme.CalculatorTheme
import com.xenon.calculator.viewmodel.SettingsViewModel
import com.xenon.calculator.viewmodel.ThemeSetting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoverSettings(
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

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        containerColor = Color.Black, // Set Scaffold background to black
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", color = Color.White) }, // Set title text to white
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                            tint = Color.White // Set icon tint to white
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black, // Set TopAppBar background to black
                    titleContentColor = Color.White, // Ensure title text is white
                    navigationIconContentColor = Color.White // Ensure nav icon is white
                ),
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp)
                .fillMaxWidth() // Ensure column takes full width for background
                .background(Color.Black) // Set Column background to black
        ) {
            CoverSettingsGroupTile(
                title = "Theme",
                subtitle = "Current: $currentThemeTitle",
                onClick = { viewModel.onThemeSettingClicked() },
            )
            Spacer(modifier = Modifier.height(5.dp))

            CoverSettingsGroupTile(
                title = "Language",
                subtitle = "Current: $currentLanguage",
                onClick = { viewModel.openLanguageSettings(context) },
            )
            LaunchedEffect(Unit) {
                viewModel.updateCurrentLanguage()
            }
            Spacer(modifier = Modifier.height(5.dp))

            CoverSettingsGroupTile(
                title = "Data Management",
                subtitle = "Clear app data and cache",
                onClick = { viewModel.onClearDataClicked() },
            )

            if (showThemeDialog) {
                CoverThemeSelectionDialog(
                    themeOptions = themeOptions,
                    currentThemeIndex = selectedThemeIndex,
                    onThemeSelected = { index -> viewModel.onThemeSelected(index) },
                    onDismiss = { viewModel.dismissThemeDialog() },
                    onConfirm = { viewModel.applyTheme() }
                )
            }

            if (showClearDataDialog) {
                CoverClearDataConfirmationDialog(
                    onConfirm = { viewModel.confirmClearData() },
                    onDismiss = { viewModel.dismissClearDataDialog() }
                )
            }
        }
    }
}

@Composable
fun CoverSettingsGroupTile(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black) // Set Row background to black (or a dark grey if you want some separation)
            .clickable(onClick = onClick, role = Role.Button)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White // Set title text to white
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White // Set subtitle text to white
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
        containerColor = Color.Black, // Set AlertDialog background to black
        onDismissRequest = onDismiss,
        title = { Text(text = "Select Theme", color = Color.White) }, // Set title text to white
        text = {
            Column(
                Modifier
                    .selectableGroup()
                    .background(Color.Black) // Set Column background to black
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
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (index == currentThemeIndex),
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White, // Radio button selected color
                                unselectedColor = Color.Gray, // Radio button unselected color
                                disabledSelectedColor = Color.DarkGray,
                                disabledUnselectedColor = Color.DarkGray
                            )
                        )
                        Text(
                            text = theme.title,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.White // Set option text to white
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK", color = Color.White) // Set button text to white
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White) // Set button text to white
            }
        }
    )
}

@Composable
fun CoverClearDataConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        containerColor = Color.Black, // Set AlertDialog background to black
        onDismissRequest = onDismiss,
        title = { Text(text = "Clear Data", color = Color.White) }, // Set title text to white
        text = { Text(text = "Are you sure you want to clear all app data? This action cannot be undone and will restart the app.", color = Color.White) }, // Set body text to white
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm", color = Color.White) // Set button text to white
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White) // Set button text to white
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CoverSettingsPreview() {
    val context = LocalContext.current
    val previewViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.SettingsViewModelFactory(context.applicationContext as Application)
    )
    // For preview, you might want to wrap with a theme that has black background
    // or apply background directly to the CoverSettings if CalculatorTheme doesn't default to black
    CalculatorTheme { // Or a custom theme for this preview
        // To ensure the preview also reflects the black background,
        // you might need to apply a background modifier here too,
        // or ensure your CalculatorTheme's surface is black for this preview.
        // Forcing it here for clarity if CalculatorTheme isn't strictly black.
        Column(Modifier.background(Color.Black)) {
            CoverSettings(onNavigateBack = {}, viewModel = previewViewModel)
        }
    }
}