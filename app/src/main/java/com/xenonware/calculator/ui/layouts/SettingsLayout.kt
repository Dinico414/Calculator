package com.xenonware.calculator.ui.layouts

import androidx.compose.runtime.Composable
import com.xenonware.calculator.presentation.sign_in.GoogleAuthUiClient
import com.xenonware.calculator.presentation.sign_in.SignInState
import com.xenonware.calculator.ui.layouts.settings.CoverSettings
import com.xenonware.calculator.ui.layouts.settings.DefaultSettings
import com.xenonware.calculator.viewmodel.LayoutType
import com.xenonware.calculator.viewmodel.SettingsViewModel


@Composable
fun SettingsLayout(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel,
    isLandscape: Boolean,
    layoutType: LayoutType,
    onNavigateToDeveloperOptions: () -> Unit,
    state: SignInState,
    googleAuthUiClient: GoogleAuthUiClient,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onConfirmSignOut: () -> Unit,
) {
    when (layoutType) {
        LayoutType.COVER -> {
            CoverSettings(
                onNavigateBack = onNavigateBack,
                viewModel = viewModel,
                onNavigateToDeveloperOptions = onNavigateToDeveloperOptions,
                state = state,
                googleAuthUiClient = googleAuthUiClient,
                onSignInClick = onSignInClick,
                onSignOutClick = onSignOutClick,
                onConfirmSignOut = onConfirmSignOut
            )

        }

        LayoutType.SMALL, LayoutType.COMPACT, LayoutType.MEDIUM, LayoutType.EXPANDED -> {
            DefaultSettings(
                onNavigateBack = onNavigateBack,
                viewModel = viewModel,
                isLandscape = isLandscape,
                layoutType = layoutType,
                onNavigateToDeveloperOptions = onNavigateToDeveloperOptions,
                state = state,
                googleAuthUiClient = googleAuthUiClient,
                onSignInClick = onSignInClick,
                onSignOutClick = onSignOutClick,
                onConfirmSignOut = onConfirmSignOut
            )
        }
    }
}
