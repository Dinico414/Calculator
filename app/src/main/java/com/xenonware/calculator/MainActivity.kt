package com.xenonware.calculator

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntSize
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.Identity
import com.xenonware.calculator.data.SharedPreferenceManager
import com.xenonware.calculator.presentation.sign_in.GoogleAuthUiClient
import com.xenonware.calculator.presentation.sign_in.SignInEvent
import com.xenonware.calculator.presentation.sign_in.SignInViewModel
import com.xenonware.calculator.ui.layouts.MainLayout
import com.xenonware.calculator.ui.theme.ScreenEnvironment
import com.xenonware.calculator.viewmodel.CalculatorViewModel
import com.xenonware.calculator.viewmodel.LayoutType
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import kotlinx.coroutines.launch
import java.util.Locale


class MainActivity : ComponentActivity() {

    private val viewModel: CalculatorViewModel by viewModels()
    private val signInViewModel: SignInViewModel by viewModels {
        SignInViewModel.SignInViewModelFactory(application)
    }

    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private var lastAppliedTheme: Int = -1
    private var lastAppliedCoverThemeEnabled: Boolean = false
    private var lastAppliedBlackedOutMode: Boolean = false

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferenceManager = SharedPreferenceManager(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val initialThemePref = sharedPreferenceManager.theme
        val initialCoverThemeEnabledSetting = sharedPreferenceManager.coverThemeEnabled
        val initialBlackedOutMode = sharedPreferenceManager.blackedOutModeEnabled

        updateAppCompatDelegateTheme(initialThemePref)

        lastAppliedTheme = initialThemePref
        lastAppliedCoverThemeEnabled = initialCoverThemeEnabledSetting
        lastAppliedBlackedOutMode = initialBlackedOutMode

        setContent {
            val currentContext = LocalContext.current
            val currentContainerSize = LocalWindowInfo.current.containerSize
            val applyCoverTheme = sharedPreferenceManager.isCoverThemeApplied(currentContainerSize)

            LaunchedEffect(Unit) {
                signInViewModel.signInEvent.collect { event ->
                    if (event is SignInEvent.SignedInSuccessfully) {
                        viewModel.onSignedIn()
                    }
                }
            }
            ScreenEnvironment(
                themePreference = lastAppliedTheme,
                coverTheme = applyCoverTheme,
                blackedOutModeEnabled = lastAppliedBlackedOutMode
            ) { layoutType, isLandscape ->
                XenonApp(
                    viewModel = viewModel,
                    layoutType = layoutType,
                    isLandscape = isLandscape,
                    onOpenSettings = {
                        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                        currentContext.startActivity(intent)
                    },
                    onOpenConverter = {
                        val intent = Intent(this@MainActivity, ConverterActivity::class.java)
                        currentContext.startActivity(intent)
                    },
                    appSize = currentContainerSize
                )
            }
        }
    }


    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val user = googleAuthUiClient.getSignedInUser()
            val isSignedIn = user != null

            sharedPreferenceManager.isUserLoggedIn = isSignedIn
            signInViewModel.updateSignInState(isSignedIn)

            if (isSignedIn) {
                viewModel.onSignedIn()
            }
        }

        val currentThemePref = sharedPreferenceManager.theme
        val currentCoverThemeEnabledSetting = sharedPreferenceManager.coverThemeEnabled
        val currentBlackedOutMode = sharedPreferenceManager.blackedOutModeEnabled

        if (currentThemePref != lastAppliedTheme || currentCoverThemeEnabledSetting != lastAppliedCoverThemeEnabled || currentBlackedOutMode != lastAppliedBlackedOutMode) {
            if (currentThemePref != lastAppliedTheme) {
                updateAppCompatDelegateTheme(currentThemePref)
            }

            lastAppliedTheme = currentThemePref
            lastAppliedCoverThemeEnabled = currentCoverThemeEnabledSetting
            lastAppliedBlackedOutMode = currentBlackedOutMode

            recreate()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        var context = newBase
        val prefs = SharedPreferenceManager(newBase)
        val savedTag = prefs.languageTag
        if (savedTag.isNotEmpty() && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val locale = Locale.forLanguageTag(savedTag)
            Locale.setDefault(locale)
            val config = Configuration(newBase.resources.configuration)
            config.setLocale(locale)
            config.setLayoutDirection(locale)
            context = newBase.createConfigurationContext(config)
        }
        super.attachBaseContext(ContextWrapper(context))
    }

    private fun updateAppCompatDelegateTheme(themePref: Int) {
        if (themePref >= 0 && themePref < sharedPreferenceManager.themeFlag.size) {
            AppCompatDelegate.setDefaultNightMode(sharedPreferenceManager.themeFlag[themePref])
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun XenonApp(
    viewModel: CalculatorViewModel,
    isLandscape: Boolean,
    layoutType: LayoutType,
    onOpenSettings: () -> Unit,
    onOpenConverter: () -> Unit,
    appSize: IntSize
) {
    MainLayout(
        viewModel = viewModel,
        isLandscape = isLandscape,
        layoutType = layoutType,
        onOpenSettings = onOpenSettings,
        onOpenConverter = onOpenConverter,
        appSize = appSize
    )
}