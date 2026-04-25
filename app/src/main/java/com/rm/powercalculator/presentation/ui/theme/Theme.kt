package com.rm.powercalculator.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VibrantOrange,
    background = DarkGray,
    surface = MediumGray,
    onSurface = NearlyWhite,
    onBackground = NearlyWhite,
    onPrimary = Color.Black,
    onSurfaceVariant = LightGray
)

@Composable
fun RMTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
