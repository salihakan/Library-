package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = EmeraldLight,
    onPrimary = Color.White,
    primaryContainer = EmeraldContainerDark,
    onPrimaryContainer = EmeraldContainerLight,
    secondary = GoldLight,
    onSecondary = Color.Black,
    tertiary = GoldPrimary,
    background = DarkSlateBg,
    onBackground = Color(0xFFE2E8F0),
    surface = DarkSlateSurface,
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = DarkSlateCard,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF334155),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EmeraldContainerLight,
    onPrimaryContainer = EmeraldDark,
    secondary = GoldPrimary,
    onSecondary = Color.White,
    tertiary = EmeraldMedium,
    background = ParchmentBg,
    onBackground = Color(0xFF1E293B),
    surface = ParchmentSurface,
    onSurface = Color(0xFF1E293B),
    surfaceVariant = ParchmentCard,
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our rich Islamic palette consistently
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

