package com.alazhartex.costing.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Brand: industrial teal with slate/indigo support and warm neutral surfaces.
val Teal = Color(0xFF0F5C5C)
val TealLight = Color(0xFF3F8A88)
val Indigo = Color(0xFF2F3E6E)
val Amber = Color(0xFFB26B00)
val Rose = Color(0xFFB3261E)
val Green = Color(0xFF2E7D5B)
val Cyan = Color(0xFF1B6FA8)

val SurfaceWarm = Color(0xFFF7F6F3)
val SurfaceWarmVariant = Color(0xFFEDEAE4)
val Charcoal = Color(0xFF14171A)
val CharcoalVariant = Color(0xFF20252A)

val LightScheme = lightColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC9E6E4),
    onPrimaryContainer = Color(0xFF04302F),
    secondary = Indigo,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDDE2F3),
    onSecondaryContainer = Color(0xFF12193A),
    tertiary = Amber,
    onTertiary = Color.White,
    surface = SurfaceWarm,
    onSurface = Color(0xFF1B1C1B),
    surfaceVariant = SurfaceWarmVariant,
    onSurfaceVariant = Color(0xFF444845),
    surfaceContainerLow = Color(0xFFFBFAF7),
    surfaceContainer = Color(0xFFF1EFEA),
    surfaceContainerHigh = Color(0xFFE9E6E0),
    outline = Color(0xFF767B76),
    outlineVariant = Color(0xFFC6C9C2),
    error = Rose,
    onError = Color.White,
)

val DarkScheme = darkColorScheme(
    primary = Color(0xFF7FD0CB),
    onPrimary = Color(0xFF00302E),
    primaryContainer = Color(0xFF0B4443),
    onPrimaryContainer = Color(0xFFB9EDE9),
    secondary = Color(0xFFB8C4EF),
    onSecondary = Color(0xFF1B2547),
    secondaryContainer = Color(0xFF2B3557),
    onSecondaryContainer = Color(0xFFDDE2F3),
    tertiary = Color(0xFFE9B44C),
    onTertiary = Color(0xFF3F2C00),
    surface = Charcoal,
    onSurface = Color(0xFFE6E3DE),
    surfaceVariant = CharcoalVariant,
    onSurfaceVariant = Color(0xFFBFC4BE),
    surfaceContainerLow = Color(0xFF171B1E),
    surfaceContainer = Color(0xFF1C2124),
    surfaceContainerHigh = Color(0xFF252B2F),
    outline = Color(0xFF8A8F8A),
    outlineVariant = Color(0xFF3F464B),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)
