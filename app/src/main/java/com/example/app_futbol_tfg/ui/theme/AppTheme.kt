package com.example.app_futbol_tfg.ui.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// Clase que agrupa todos los colores dinámicos del tema
data class AppColors(
    val background: Color,
    val card: Color,
    val textPrimary: Color,
    val textSecondary: Color
)

// Colores por defecto (modo claro)
val LightAppColors = AppColors(
    background = BackgroundLight,
    card = CardBackground,
    textPrimary = TextPrimary,
    textSecondary = TextSecondary
)

// Colores para modo oscuro
val DarkAppColors = AppColors(
    background = BackgroundDarkReal,
    card = CardBackgroundDark,
    textPrimary = TextPrimaryDark,
    textSecondary = TextSecondaryDark
)

// CompositionLocal que provee los colores a todo el árbol de composición
val LocalAppColors = compositionLocalOf { LightAppColors }