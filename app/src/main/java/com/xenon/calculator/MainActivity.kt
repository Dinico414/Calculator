package com.xenon.calculator

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import com.xenon.calculator.ui.layouts.ButtonLayout
import com.xenon.calculator.ui.layouts.CalculatorScreen
import com.xenon.calculator.ui.theme.ScreenEnvironment
import com.xenon.calculator.ui.values.ButtonBoxPadding
import com.xenon.calculator.ui.values.CompactButtonSize
import com.xenon.calculator.ui.values.LargeCornerRadius
import com.xenon.calculator.ui.values.LargePadding
import com.xenon.calculator.ui.values.MediumCornerRadius
import com.xenon.calculator.ui.values.NoCornerRadius
import com.xenon.calculator.ui.values.NoElevation
import com.xenon.calculator.ui.values.NoPadding
import com.xenon.calculator.ui.values.SmallCornerRadius
import com.xenon.calculator.ui.values.SmallElevation
import com.xenon.calculator.ui.values.SmallMediumPadding
import com.xenon.calculator.viewmodel.CalculatorViewModel
import com.xenon.calculator.viewmodel.LayoutType
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.CupertinoMaterials
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi


val OLD_APP_PACKAGE_NAMES = listOf(
    "com.xenon.calculator.compose", "com.xenon.calculator.compose.debug"
)

class MainActivity : ComponentActivity() {
    private val calculatorViewModel: CalculatorViewModel by viewModels()
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private var activeThemeForMainActivity: Int = 2
    private var coverEnabledState = false

    private var showUninstallDialog by mutableStateOf(false)
    private var

            packageNameToDelete by mutableStateOf<String?>(null)


    private val uninstallLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            checkOldAppStatus()
            if (packageNameToDelete == null) {
                showUninstallDialog = false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedPreferenceManager = SharedPreferenceManager(applicationContext)

        activeThemeForMainActivity = sharedPreferenceManager.theme

        if (activeThemeForMainActivity >= 0 && activeThemeForMainActivity < sharedPreferenceManager.themeFlag.size) {
            AppCompatDelegate.setDefaultNightMode(sharedPreferenceManager.themeFlag[activeThemeForMainActivity])
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            activeThemeForMainActivity = 2
        }
        coverEnabledState = sharedPreferenceManager.coverThemeEnabled

        checkOldAppStatus()

        setContent {
            val containerSize = LocalWindowInfo.current.containerSize
            val applyCoverTheme = sharedPreferenceManager.isCoverThemeApplied(containerSize)

            ScreenEnvironment(
                activeThemeForMainActivity, applyCoverTheme
            ) { layoutType, isLandscape ->
                val isCoverScreen = layoutType == LayoutType.COVER

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isCoverScreen) Color.Black
                            else colorScheme.background
                        ), color = if (isCoverScreen) Color.Black else colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                                    .asPaddingValues()
                            )
                            .then(
                                if (isCoverScreen) {
                                    Modifier
                                        .background(Color.Black)
                                        .padding(horizontal = NoPadding)
                                        .clip(RoundedCornerShape(NoCornerRadius))
                                } else {
                                    Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = LargeCornerRadius,
                                                topEnd = LargeCornerRadius
                                            )
                                        )
                                        .background(colorScheme.surfaceContainer)
                                }
                            )
                    ) {
                        if (showUninstallDialog && packageNameToDelete != null) {
                            UninstallOldAppDialog(
                                onConfirm = {
                                    packageNameToDelete?.let { requestUninstallOldApp(it) }
                                })
                        } else {
                            CalculatorApp(
                                viewModel = calculatorViewModel,
                                layoutType = layoutType,
                                isLandscape = isLandscape,
                                onOpenSettings = {
                                    val intent =
                                        Intent(this@MainActivity, SettingsActivity::class.java)
                                    startActivity(intent)
                                },
                                onOpenConverter = {
                                    val intent =
                                        Intent(this@MainActivity, ConverterActivity::class.java)
                                    startActivity(intent)
                                })
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        checkOldAppStatus()
        if (packageNameToDelete != null && !showUninstallDialog) {
            showUninstallDialog = true
        }

        val storedTheme = sharedPreferenceManager.theme
        if (activeThemeForMainActivity != storedTheme) {
            activeThemeForMainActivity = storedTheme
            recreate()
        }
        val applyCoverTheme = sharedPreferenceManager.coverThemeEnabled
        if (applyCoverTheme != coverEnabledState) {
            coverEnabledState = applyCoverTheme
            recreate()
        }
    }

    private fun isPackageInstalled(packageName: String, pm: PackageManager): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION") pm.getPackageInfo(packageName, 0)
            }
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun checkOldAppStatus() {
        var foundPackage: String? = null
        for (packageName in OLD_APP_PACKAGE_NAMES) {
            if (isPackageInstalled(packageName, packageManager)) {
                foundPackage = packageName
                break
            }
        }

        packageNameToDelete = foundPackage
        showUninstallDialog = foundPackage != null
    }

    private fun requestUninstallOldApp(packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = "package:$packageName".toUri()
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        uninstallLauncher.launch(intent)
    }
}

@Composable
fun UninstallOldAppDialog(
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("With Something Bad comes Something better") },
        text = {
            Text(
                "Hello there,\n" + "Say Goodbye to the old Version of Calculator, we are moving in to a newer, better and from the ground up newly build Version. The old one will not be supported anymore."
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Good Bye")
            }
        },
        dismissButton = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}


@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun CalculatorApp(
    viewModel: CalculatorViewModel,
    layoutType: LayoutType,
    isLandscape: Boolean,
    onOpenSettings: () -> Unit,
    onOpenConverter: () -> Unit
) {

    var showMenu by remember { mutableStateOf(false) }
    val hazeState = remember { HazeState() }
    val isCoverScreenLayout = layoutType == LayoutType.COVER

    Column(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState)
            .padding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom).asPaddingValues()),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f)
                .then(
                    if (isCoverScreenLayout) {
                        Modifier.padding(horizontal = NoPadding, vertical = NoPadding)
                    } else {
                        Modifier
                            .padding(horizontal = LargePadding, vertical = NoPadding)
                            .padding(top = LargePadding)
                    }
                )
                .clip(RoundedCornerShape(MediumCornerRadius))
                .background(colorScheme.secondaryContainer)
        ) {
            CalculatorScreen(
                viewModel = viewModel,
                isLandscape = isLandscape,
                layoutType = layoutType,
                modifier = Modifier.fillMaxSize()
            )


            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = LargePadding, end = LargePadding)
            ) {
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .shadow(elevation = SmallElevation, shape = CircleShape)
                        .clip(shape = CircleShape)
                        .size(CompactButtonSize)
                        .background(color = colorScheme.surfaceContainer)
                        .clickable(
                            onClick = { showMenu = !showMenu },
                            interactionSource = interactionSource,
                            indication = LocalIndication.current,
                        ), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = colorScheme.onSurface,
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    offset = DpOffset(x = NoPadding, y = ButtonBoxPadding),
                    containerColor = Color.Transparent,
                    shadowElevation = NoElevation,
                    modifier = Modifier
                        .padding(
                            top = SmallMediumPadding, bottom = SmallMediumPadding
                        )
                        .clip(RoundedCornerShape(SmallCornerRadius))
                        .background(colorScheme.surfaceContainer)
                        .hazeEffect(
                            state = hazeState, style = CupertinoMaterials.ultraThin()
                        )
                ) {
                    DropdownMenuItem(text = {
                        Text(
                            stringResource(id = R.string.converter),
                            color = if (isCoverScreenLayout) Color.White else colorScheme.onSurface
                        )
                    }, onClick = {
                        showMenu = false
                        onOpenConverter()
                    })
                    DropdownMenuItem(text = {
                        Text(
                            stringResource(id = R.string.settings),
                            color = if (isCoverScreenLayout) Color.White else colorScheme.onSurface
                        )
                    }, onClick = {
                        showMenu = false
                        onOpenSettings()
                    })
                }
            }
        }

        ButtonLayout(
            viewModel = viewModel,
            isLandscape = isLandscape,
            layoutType = layoutType,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f)
        )
    }
}