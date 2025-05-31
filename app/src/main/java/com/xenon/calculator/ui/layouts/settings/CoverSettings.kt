package com.xenon.calculator.ui.layouts.settings

// Import WindowInsets and asPaddingValues if needed, ActivityScreen may handle some aspects
// Import SwitchDefaults if you need to customize Switch colors for dark theme
// Replace CollapsingAppBarLayout with ActivityScreen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.xenon.calculator.R
import com.xenon.calculator.ui.res.ActivityScreen
import com.xenon.calculator.ui.res.ClearDataConfirmationDialog
import com.xenon.calculator.ui.res.CoverDisplaySelectionDialog
import com.xenon.calculator.ui.res.ThemeSelectionDialog
import com.xenon.calculator.ui.values.ExtraLargePadding
import com.xenon.calculator.ui.values.LargePadding
import com.xenon.calculator.ui.values.LargerSpacing
import com.xenon.calculator.ui.values.MediumPadding
import com.xenon.calculator.viewmodel.SettingsViewModel

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

    ActivityScreen(
        title = { _, _ -> // FontSize and color are overridden by appBarTitleContentColor
            Text("Settings") // Direct string, or use stringResource(R.string.settings_title)
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_back_description) // Use string resource
                )
            }
        },
        appBarActions = {
        },
        isAppBarCollapsible = false,
        appBarCollapsedContainerColor = Color.Black,


        contentModifier = Modifier
        ,
        content = { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()

                    .verticalScroll(rememberScrollState())
                    .padding(vertical = MediumPadding)

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
            }
        },
        dialogs = {
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

            if (showCoverSelectionDialog) {
                CoverDisplaySelectionDialog(
                    onConfirm = {
                        viewModel.saveCoverDisplayMetrics(containerSize)
                    },
                    onDismiss = { viewModel.dismissCoverThemeDialog() }
                )
            }

            if (showClearDataDialog) {
                ClearDataConfirmationDialog(
                    onConfirm = { viewModel.confirmClearData() },
                    onDismiss = { viewModel.dismissClearDataDialog() }
                )
            }
        }
    )
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
            .padding(horizontal = LargePadding) // Padding for the tile itself
            // .background(Color.Black) // Background is handled by the parent Column/ActivityScreen
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick, role = Role.Button)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = LargerSpacing, vertical = ExtraLargePadding), // Inner padding for content
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White // Explicitly white for cover theme
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White // Explicitly white for cover theme
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
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
        VerticalDivider()
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }
}

