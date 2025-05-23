package com.xenon.calculator.ui.layouts.settings

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xenon.calculator.R
import com.xenon.calculator.viewmodel.SettingsViewModel
import com.xenon.calculator.viewmodel.ThemeSetting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactSettings(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.SettingsViewModelFactory(
            LocalContext.current.applicationContext as Application
        )
    ),
) {
    LocalContext.current
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(Unit) {
        viewModel.updateCurrentLanguage()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.abc_action_bar_up_description),
                            tint = colorResource(id = com.xenon.commons.accesspoint.R.color.onSurface)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = colorResource(id = com.xenon.commons.accesspoint.R.color.primary),
                    navigationIconContentColor = colorResource(id = com.xenon.commons.accesspoint.R.color.onSurface),
                    actionIconContentColor = colorResource(id = com.xenon.commons.accesspoint.R.color.onSurface)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .background(
                    color = colorResource(id = com.xenon.commons.accesspoint.R.color.surfaceContainer),
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            SettingsContent(viewModel = viewModel)
        }
    }

    // --- Dialogs ---
    if (viewModel.showThemeDialog) {
        ThemeSelectionDialog(
            currentThemeIndex = viewModel.selectedThemeIndex,
            onThemeSelected = { index -> viewModel.onThemeSelected(index) },
            onDismiss = { viewModel.dismissThemeDialog() },
            onConfirm = { viewModel.applyTheme() }
        )
    }

    if (viewModel.showClearDataDialog) {
        ClearDataConfirmationDialog(
            onConfirm = { viewModel.confirmClearData() },
            onDismiss = { viewModel.dismissClearDataDialog() }
        )
    }
}

@Composable
fun SettingsContent(viewModel: SettingsViewModel) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp) // Increased spacing
    ) {
        Text(
            text = stringResource(id = R.string.general),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        SettingsTile(
            title = stringResource(id = R.string.language),
            value = viewModel.currentLanguage,
            onClick = { viewModel.openLanguageSettings(context) }
        )

        SettingsTile(
            title = stringResource(id = R.string.theme),
            value = viewModel.currentThemeTitle,
            onClick = { viewModel.onThemeSettingClicked() }
        )

        SettingsTile(
            title = stringResource(id = R.string.clear_data),
            onClick = { viewModel.onClearDataClicked() }
        )
    }
}

@Composable
fun SettingsTile(
    title: String,
    value: String? = null,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant, // Use theme color
                RoundedCornerShape(12.dp) // Slightly more rounded
            )
            .clickable(onClick = onClick) // Standard clickable with ripple
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium // Adjusted style
        )
        value?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ThemeSelectionDialog(
    currentThemeIndex: Int,
    onThemeSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val themeOptions = ThemeSetting.entries.map { it.title } // Get titles from enum

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Select Theme") },
        text = {
            Column(Modifier.selectableGroup()) {
                themeOptions.forEachIndexed { index, label ->
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
                            onClick = null // null recommended for radio buttons when row is selectable
                        )
                        Text(
                            text = label,
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
                Text(stringResource(R.string.cancel)) // Use string resource
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
        title = { Text(text = stringResource(R.string.clear_data)) },
        text = { Text(text = stringResource(R.string.clear_data_dialog)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

// Removed noRippleClickable as standard clickable is generally preferred
// for accessibility and user feedback, unless specifically not desired.

@Preview(showBackground = true)
@Composable
fun CompactSettingsPreview() {
    // For preview, you might need to provide a dummy onNavigateBack
    MaterialTheme { // Wrap in MaterialTheme for previews
        CompactSettings(onNavigateBack = {})
    }
}