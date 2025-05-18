package com.xenon.calculator.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat

import com.xenon.commons.accesspoint.R as CommonsR

@Composable
fun CalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme(
            background = colorResource(id = CommonsR.color.background),
            surface = colorResource(id = CommonsR.color.surface),
            surfaceContainerLowest = colorResource(id = CommonsR.color.surfaceContainerLowest),
            onSurface = colorResource(id = CommonsR.color.onSurface),
            onSurfaceVariant = colorResource(id = CommonsR.color.onSurfaceVariant),
            onSecondaryContainer = colorResource(id = CommonsR.color.onSecondaryContainer),
            inverseSurface = colorResource(id = CommonsR.color.inverseSurface),
            inverseOnSurface = colorResource(id = CommonsR.color.inverseOnSurface),
            outline = colorResource(id = CommonsR.color.outline),
            surfaceVariant = colorResource(id = CommonsR.color.surfaceVariant),
            surfaceContainerHighest = colorResource(id = CommonsR.color.surfaceContainerHighest),
            surfaceContainer = colorResource(id = CommonsR.color.surfaceContainer),
            onSecondary = colorResource(id = CommonsR.color.onSecondary),
            onTertiary = colorResource(id = CommonsR.color.onTertiary),
            onTertiaryContainer = colorResource(id = CommonsR.color.onTertiaryContainer),
            onBackground = colorResource(id = CommonsR.color.onBackground),
            onPrimary = colorResource(id = CommonsR.color.onPrimary),
            onPrimaryContainer = colorResource(id = CommonsR.color.onPrimaryContainer),
            outlineVariant = colorResource(id = CommonsR.color.outlineVariant),
            primary = colorResource(id = CommonsR.color.primary),
            primaryContainer = colorResource(id = CommonsR.color.primaryContainer),
            secondary = colorResource(id = CommonsR.color.secondary),
            secondaryContainer = colorResource(id = CommonsR.color.secondaryContainer),
            tertiary = colorResource(id = CommonsR.color.tertiary),
            tertiaryContainer = colorResource(id = CommonsR.color.tertiaryContainer),
        )
        else -> lightColorScheme(
            background = colorResource(id = CommonsR.color.background),
            surface = colorResource(id = CommonsR.color.surface),
            surfaceContainerLowest = colorResource(id = CommonsR.color.surfaceContainerLowest),
            onSurface = colorResource(id = CommonsR.color.onSurface),
            onSurfaceVariant = colorResource(id = CommonsR.color.onSurfaceVariant),
            onSecondaryContainer = colorResource(id = CommonsR.color.onSecondaryContainer),
            inverseSurface = colorResource(id = CommonsR.color.inverseSurface),
            inverseOnSurface = colorResource(id = CommonsR.color.inverseOnSurface),
            outline = colorResource(id = CommonsR.color.outline),
            surfaceVariant = colorResource(id = CommonsR.color.surfaceVariant),
            surfaceContainerHighest = colorResource(id = CommonsR.color.surfaceContainerHighest),
            surfaceContainer = colorResource(id = CommonsR.color.surfaceContainer),
            onSecondary = colorResource(id = CommonsR.color.onSecondary),
            onTertiary = colorResource(id = CommonsR.color.onTertiary),
            onTertiaryContainer = colorResource(id = CommonsR.color.onTertiaryContainer),
            onBackground = colorResource(id = CommonsR.color.onBackground),
            onPrimary = colorResource(id = CommonsR.color.onPrimary),
            onPrimaryContainer = colorResource(id = CommonsR.color.onPrimaryContainer),
            outlineVariant = colorResource(id = CommonsR.color.outlineVariant),
            primary = colorResource(id = CommonsR.color.primary),
            primaryContainer = colorResource(id = CommonsR.color.primaryContainer),
            secondary = colorResource(id = CommonsR.color.secondary),
            secondaryContainer = colorResource(id = CommonsR.color.secondaryContainer),
            tertiary = colorResource(id = CommonsR.color.tertiary),
            tertiaryContainer = colorResource(id = CommonsR.color.tertiaryContainer),
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, false)

            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme

            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Make sure Typography is defined
        content = content
    )
}