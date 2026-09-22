package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CorporateLightColorScheme = lightColorScheme(
  primary = CorporatePrimary,
  onPrimary = CorporateOnPrimary,
  primaryContainer = CorporatePrimaryContainer,
  onPrimaryContainer = CorporateOnPrimaryContainer,
  secondary = CorporateSecondary,
  onSecondary = CorporateOnSecondary,
  secondaryContainer = CorporateSecondaryContainer,
  onSecondaryContainer = CorporateOnSecondaryContainer,
  tertiary = CorporateTertiary,
  onTertiary = CorporateOnTertiary,
  background = CorporateBackground,
  onBackground = CorporateTextPrimary,
  surface = CorporateSurface,
  onSurface = CorporateTextPrimary,
  surfaceVariant = CorporateSurfaceVariant,
  onSurfaceVariant = CorporateTextSecondary,
  outline = CorporateOutline,
  outlineVariant = CorporateOutlineVariant,
)

private val CorporateDarkColorScheme = darkColorScheme(
  primary = Color(0xFF60A5FA),
  onPrimary = Color(0xFF0F172A),
  primaryContainer = Color(0xFF1E3A8A),
  onPrimaryContainer = Color(0xFFDBEAFE),
  secondary = Color(0xFF38BDF8),
  onSecondary = Color(0xFF0F172A),
  secondaryContainer = Color(0xFF0369A1),
  onSecondaryContainer = Color(0xFFE0F2FE),
  tertiary = Color(0xFF2DD4BF),
  onTertiary = Color(0xFF0F172A),
  background = Color(0xFF0B1329),
  onBackground = Color(0xFFF1F5F9),
  surface = Color(0xFF111C38),
  onSurface = Color(0xFFF1F5F9),
  surfaceVariant = Color(0xFF1E293B),
  onSurfaceVariant = Color(0xFF94A3B8),
  outline = Color(0xFF334155),
  outlineVariant = Color(0xFF1E293B),
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) CorporateDarkColorScheme else CorporateLightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

