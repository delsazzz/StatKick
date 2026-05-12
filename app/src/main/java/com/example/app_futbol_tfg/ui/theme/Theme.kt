package com.example.app_futbol_tfg.ui.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryBlue,
    background = BackgroundDarkReal,
    surface = CardBackgroundDark,
    onPrimary = Color.White,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)

private val LightColorScheme = lightColorScheme(

    primary = PrimaryBlue,
    secondary = SecondaryBlue,

    background = BackgroundLight,
    surface = CardBackground,

    onPrimary = CardBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun App_Futbol_TFGTheme(
    // Modo oscuro desactivado
    darkTheme: Boolean = false,
    // Colores dinámicos en función del fondo del móvil desactivado por si acaso
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = when {

        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme)
                dynamicDarkColorScheme(context)
            else
                dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}