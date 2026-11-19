package com.alazhartex.costing.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alazhartex.costing.R
import com.alazhartex.costing.ui.theme.LocalSemanticColors
import com.alazhartex.costing.ui.theme.tabular

enum class Tone { NEUTRAL, SUCCESS, WARNING, ERROR, INFO }

@Composable
fun toneColor(tone: Tone): Color = when (tone) {
    Tone.SUCCESS -> LocalSemanticColors.current.success
    Tone.WARNING -> LocalSemanticColors.current.warning
    Tone.INFO -> LocalSemanticColors.current.info
    Tone.ERROR -> MaterialTheme.colorScheme.error
    Tone.NEUTRAL -> LocalSemanticColors.current.neutral
}

private fun toneIcon(tone: Tone): ImageVector = when (tone) {
    Tone.SUCCESS -> Icons.Filled.CheckCircle
    Tone.WARNING -> Icons.Filled.Warning
    Tone.ERROR -> Icons.Filled.Warning
    else -> Icons.Filled.Info
}

/** Status chip: icon + text + tint, never colour alone. */
@Composable
fun StatusChip(label: String, tone: Tone = Tone.NEUTRAL, modifier: Modifier = Modifier) {
    val color = toneColor(tone)
    Surface(
        color = color.copy(alpha = 0.14f),
        contentColor = color,
        shape = RoundedCornerShape(999.dp),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
        ) {
            Icon(toneIcon(tone), contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(Modifier.size(6.dp))
            Text(label, style = MaterialTheme.typography.labelMedium.tabular)
        }
    }
}

@Composable
fun KpiCard(label: String, value: String, caption: String? = null, tone: Tone = Tone.NEUTRAL, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
        Column(Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall.tabular,
                color = if (tone == Tone.NEUTRAL) MaterialTheme.colorScheme.onSurface else toneColor(tone),
            )
            caption?.let {
                Spacer(Modifier.height(4.dp))
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(top = 20.dp, bottom = 8.dp),
    )
}

@Composable
fun AlertBanner(tone: Tone, text: String, modifier: Modifier = Modifier) {
    val color = toneColor(tone)
    Surface(
        color = color.copy(alpha = 0.10f),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth().semantics { contentDescription = text },
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(toneIcon(tone), contentDescription = null, tint = color)
            Spacer(Modifier.size(10.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun FormulaCard(title: String, formula: String, note: String? = null) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(6.dp))
            Text(formula, style = MaterialTheme.typography.bodyMedium.tabular, color = MaterialTheme.colorScheme.primary)
            note?.let {
                Spacer(Modifier.height(6.dp))
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun LedgerRow(label: String, value: String, emphasis: Boolean = false) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            style = if (emphasis) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
        )
        Text(
            value,
            style = (if (emphasis) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium).tabular,
            fontWeight = if (emphasis) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}

/** Stacked grade bar: first / second / reject / unaccounted shares. */
@Composable
fun StackedGradeBar(
    firstPct: Float,
    secondPct: Float,
    rejectPct: Float,
    modifier: Modifier = Modifier,
) {
    val unaccounted = (100f - firstPct - secondPct - rejectPct).coerceAtLeast(0f)
    val semantic = LocalSemanticColors.current
    Column(modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().height(14.dp).background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(999.dp))) {
            listOf(
                firstPct to semantic.success,
                secondPct to semantic.warning,
                rejectPct to MaterialTheme.colorScheme.error,
                unaccounted to semantic.neutral,
            ).forEach { (pct, color) ->
                if (pct > 0f) {
                    Box(
                        Modifier.weight(pct).fillMaxHeight().background(color),
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            LegendDot(stringResource(R.string.grade_first), semantic.success, firstPct)
            LegendDot(stringResource(R.string.grade_second), semantic.warning, secondPct)
            LegendDot(stringResource(R.string.grade_reject), MaterialTheme.colorScheme.error, rejectPct)
        }
    }
}

@Composable
private fun LegendDot(label: String, color: Color, pct: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(8.dp).background(color, RoundedCornerShape(4.dp)))
        Spacer(Modifier.size(4.dp))
        Text(String.format(java.util.Locale.US, "%s %.2f%%", label, pct), style = MaterialTheme.typography.labelSmall.tabular)
    }
}

@Composable
fun EmptyState(title: String, body: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(6.dp))
        Text(
            body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
