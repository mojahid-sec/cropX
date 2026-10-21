package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = ForestGreenOnPrimary,
    primaryContainer = ForestGreenContainer,
    onPrimaryContainer = ForestGreenOnContainer,
    secondary = SproutGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD7E8DC),
    onSecondaryContainer = Color(0xFF132F1B),
    tertiary = HarvestGold,
    onTertiary = Color.Black,
    tertiaryContainer = HarvestGoldContainer,
    onTertiaryContainer = Color(0xFF4A3400),
    background = AgriLightBackground,
    onBackground = AgriLightTextPrimary,
    surface = AgriLightSurface,
    onSurface = AgriLightTextPrimary,
    surfaceVariant = AgriLightSurfaceVariant,
    onSurfaceVariant = AgriLightTextSecondary,
    outline = AgriLightOutline,
    outlineVariant = Color(0xFFE0EBE2),
    error = ExpenseRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = AgriDarkPrimary,
    onPrimary = AgriDarkOnPrimary,
    primaryContainer = Color(0xFF1B4D3E),
    onPrimaryContainer = Color(0xFFA5D6A7),
    secondary = SproutGreenLight,
    onSecondary = Color(0xFF0D3316),
    secondaryContainer = Color(0xFF22422C),
    onSecondaryContainer = Color(0xFFC8E6C9),
    tertiary = Color(0xFFFFD54F),
    onTertiary = Color(0xFF422B00),
    tertiaryContainer = Color(0xFF5A3E00),
    onTertiaryContainer = Color(0xFFFFE082),
    background = AgriDarkBackground,
    onBackground = AgriDarkTextPrimary,
    surface = AgriDarkSurface,
    onSurface = AgriDarkTextPrimary,
    surfaceVariant = AgriDarkSurfaceVariant,
    onSurfaceVariant = AgriDarkTextSecondary,
    outline = AgriDarkOutline,
    outlineVariant = Color(0xFF1E3326),
    error = Color(0xFFEF5350),
    onError = Color.Black
)

@Composable
fun CropXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
