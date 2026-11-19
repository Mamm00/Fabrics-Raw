package com.alazhartex.costing.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.staticCompositionLocalOf

/** Tabular figures keep columns of numbers aligned across rows. */
val TextStyle.tabular: TextStyle get() = copy(fontFeatureSettings = "tnum")

val AzharTypography = Typography().let { base ->
    base.copy(
        headlineMedium = base.headlineMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 30.sp),
        headlineSmall = base.headlineSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
        titleLarge = base.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        titleMedium = base.titleMedium.copy(fontWeight = FontWeight.Medium, fontFeatureSettings = "tnum"),
        bodyLarge = base.bodyLarge.copy(lineHeight = 26.sp),
        bodyMedium = base.bodyMedium.copy(lineHeight = 22.sp),
        labelLarge = base.labelLarge.copy(fontWeight = FontWeight.Medium),
        labelMedium = base.labelMedium.copy(fontFeatureSettings = "tnum"),
    )
}

/** Semantic colours beyond the M3 roles, always paired with icon + text. */
data class SemanticColors(
    val success: Color,
    val warning: Color,
    val info: Color,
    val neutral: Color,
)

val LightSemantic = SemanticColors(
    success = Color(0xFF2E7D5B),
    warning = Color(0xFFB26B00),
    info = Color(0xFF1B6FA8),
    neutral = Color(0xFF6E7370),
)

val DarkSemantic = SemanticColors(
    success = Color(0xFF7FD0A5),
    warning = Color(0xFFE9B44C),
    info = Color(0xFF8CC8F0),
    neutral = Color(0xFFA5AAA5),
)

val LocalSemanticColors = staticCompositionLocalOf { LightSemantic }
