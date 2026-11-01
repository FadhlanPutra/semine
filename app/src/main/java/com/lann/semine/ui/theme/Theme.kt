package com.lann.semine.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/*
 * Token DESIGN.md §2 dipetakan ke colorScheme Material 3.
 * Aksen -> primary/secondary/tertiary (satu warna aksen, warna lain hanya untuk status).
 * Dynamic color (Material You) sengaja tidak dipakai agar identitas warna tetap konsisten.
 */
private val LightColorScheme = lightColorScheme(
    primary = AccentLight,
    onPrimary = OnAccentLight,
    primaryContainer = SurfaceContainerLight,
    onPrimaryContainer = OnSurfaceLight,
    secondary = AccentLight,
    onSecondary = OnAccentLight,
    secondaryContainer = SurfaceContainerLight,
    onSecondaryContainer = OnSurfaceLight,
    tertiary = AccentLight,
    onTertiary = OnAccentLight,
    tertiaryContainer = SurfaceContainerLight,
    onTertiaryContainer = OnSurfaceLight,
    background = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceContainerLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerLow = SurfaceContainerLight,
    surfaceContainerLowest = SurfaceLight,
    surfaceContainerHigh = SurfaceContainerLight,
    surfaceContainerHighest = SurfaceContainerLight,
    surfaceBright = SurfaceLight,
    surfaceDim = BackgroundLight,
    inverseSurface = OnSurfaceLight,
    inverseOnSurface = SurfaceLight,
    error = StatusError,
    onError = OnAccentLight,
    errorContainer = SurfaceContainerLight,
    onErrorContainer = OnSurfaceLight,
    outline = OutlineLight,
    outlineVariant = OutlineLight
)

private val DarkColorScheme = darkColorScheme(
    primary = AccentDark,
    onPrimary = OnAccentDark,
    primaryContainer = SurfaceContainerDark,
    onPrimaryContainer = OnSurfaceDark,
    secondary = AccentDark,
    onSecondary = OnAccentDark,
    secondaryContainer = SurfaceContainerDark,
    onSecondaryContainer = OnSurfaceDark,
    tertiary = AccentDark,
    onTertiary = OnAccentDark,
    tertiaryContainer = SurfaceContainerDark,
    onTertiaryContainer = OnSurfaceDark,
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceContainerDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerLow = SurfaceContainerDark,
    surfaceContainerLowest = SurfaceDark,
    surfaceContainerHigh = SurfaceContainerDark,
    surfaceContainerHighest = SurfaceContainerDark,
    surfaceBright = SurfaceDark,
    surfaceDim = BackgroundDark,
    inverseSurface = OnSurfaceDark,
    inverseOnSurface = SurfaceDark,
    error = StatusError,
    onError = OnAccentLight,
    errorContainer = SurfaceContainerDark,
    onErrorContainer = OnSurfaceDark,
    outline = OutlineDark,
    outlineVariant = OutlineDark
)

@Composable
fun SemineTheme(
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
