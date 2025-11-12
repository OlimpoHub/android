package com.app.arcabyolimpo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.app.arcabyolimpo.presentation.theme.Typography

private val DarkColorScheme =
    darkColorScheme(
        primary = PrimaryBlue,
        onPrimary = White,
        secondary = SecondaryBlue,
        onSecondary = White,
        tertiary = ButtonBlue,
        background = Background,
        surface = HeaderBackground,
        onBackground = White,
        onSurface = White,
        error = ErrorRed,
        onError = White,
        outline = DangerGray,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = PrimaryBlue,
        onPrimary = White,
        secondary = SecondaryBlue,
        onSecondary = White,
        tertiary = ButtonBlue,
        background = White,
        surface = Background,
        onBackground = Color(0xFF0F142C), // tono oscuro suave
        onSurface = Color(0xFF0F142C),
        error = ErrorRed,
        onError = White,
        outline = DangerGray,
    )

/**
 * Tema principal de Arca By Olimpo basado en la nueva paleta de colores.
 */
@Composable
fun ArcaByOlimpoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
