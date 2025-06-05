package com.xenon.calculator.ui.layouts.settings

import android.os.Build // Make sure Build is imported
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import com.xenon.calculator.R
import com.xenon.calculator.ui.layouts.ActivityScreen
import com.xenon.calculator.ui.res.ClearDataConfirmationDialog
import com.xenon.calculator.ui.res.CoverDisplaySelectionDialog
import com.xenon.calculator.ui.res.LanguageSelectionDialog // Import your LanguageSelectionDialog
import com.xenon.calculator.ui.res.ThemeSelectionDialog
import com.xenon.calculator.ui.values.LargePadding
import com.xenon.calculator.ui.values.SettingsItems
import com.xenon.calculator.viewmodel.SettingsViewModel

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
    val showCoverSelectionDialog by viewModel.showCoverSelectionDialog.collectAsState()
    val coverThemeEnabled by viewModel.enableCoverTheme.collectAsState()

    // --- Collect Language Dialog States ---
    val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()
    val availableLanguages by viewModel.availableLanguages.collectAsState()
    val selectedLanguageTagInDialog by viewModel.selectedLanguageTagInDialog.collectAsState()


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
        title = { fontSize, color ->
            Text(
                text = stringResource(id = R.string.settings),
                fontSize = fontSize,
                color = color
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_back_description)
                )
            }
        },
        appBarActions = {
        },

        isAppBarCollapsible = true,

        contentModifier = Modifier,
        content = { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = LargePadding,
                        end = LargePadding,
                        top = LargePadding,
                        bottom = WindowInsets.safeDrawing
                            .asPaddingValues()
                            .calculateBottomPadding() + LargePadding
                    )
            ) {
                SettingsItems(
                    viewModel = viewModel,
                    currentThemeTitle = currentThemeTitle,
                    applyCoverTheme = applyCoverTheme,
                    coverThemeEnabled = coverThemeEnabled,
                    currentLanguage = currentLanguage,
                    appVersion = appVersion
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
                    onConfirm = { viewModel.applySelectedTheme() })
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
                    onConfirm = {
                        viewModel.confirmClearData()
                    },
                    onDismiss = { viewModel.dismissClearDataDialog() }
                )
            }

            if (showLanguageDialog && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                LanguageSelectionDialog(
                    availableLanguages = availableLanguages,
                    currentLanguageTag = selectedLanguageTagInDialog,
                    onLanguageSelected = { tag -> viewModel.onLanguageSelectedInDialog(tag) },
                    onDismiss = { viewModel.dismissLanguageDialog() },
                    onConfirm = { viewModel.applySelectedLanguage() }
                )
            }
        })
}