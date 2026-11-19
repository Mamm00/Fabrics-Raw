package com.alazhartex.costing.ui.shrinkage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.ui.common.LabeledField
import com.alazhartex.costing.ui.components.AlertBanner
import com.alazhartex.costing.ui.components.FormulaCard
import com.alazhartex.costing.ui.components.KpiCard
import com.alazhartex.costing.ui.components.SectionHeader
import com.alazhartex.costing.ui.components.Tone
import java.math.BigDecimal
import java.math.RoundingMode

/** Live shrinkage workspace: every keystroke recalculates, flags come from TextileRules. */
@Composable
fun ShrinkagePane(container: AppContainer, batchId: String) {
    var warpOriginal by remember { mutableStateOf("") }
    var warpFinal by remember { mutableStateOf("") }
    var weftOriginal by remember { mutableStateOf("") }
    var weftFinal by remember { mutableStateOf("") }
    var growth by remember { mutableStateOf(false) }

    val warp = shrink(warpOriginal, warpFinal, growth)
    val weft = shrink(weftOriginal, weftFinal, growth)
    val rules = container.defaultRules

    Column {
        Text(stringResource(R.string.shrinkage_warp_section), style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LabeledField(stringResource(R.string.shrinkage_warp_original), warpOriginal, Modifier.weight(1f)) { warpOriginal = it }
            LabeledField(stringResource(R.string.shrinkage_warp_final), warpFinal, Modifier.weight(1f)) { warpFinal = it }
        }
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.shrinkage_weft_section), style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LabeledField(stringResource(R.string.shrinkage_weft_original), weftOriginal, Modifier.weight(1f)) { weftOriginal = it }
            LabeledField(stringResource(R.string.shrinkage_weft_final), weftFinal, Modifier.weight(1f)) { weftFinal = it }
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = growth, onCheckedChange = { growth = it })
            Spacer(Modifier.height(4.dp))
            Text(stringResource(R.string.shrinkage_allow_growth), modifier = Modifier.weight(1f))
        }

        SectionHeader(stringResource(R.string.shrinkage_results))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KpiCard(
                label = stringResource(R.string.shrinkage_warp_section),
                value = warp?.let { "${it.toPlainString()}%" } ?: "—",
                tone = if (warp != null && warp > rules.shrinkageWarpWarnPct) Tone.WARNING else Tone.NEUTRAL,
                modifier = Modifier.weight(1f),
            )
            KpiCard(
                label = stringResource(R.string.shrinkage_weft_section),
                value = weft?.let { "${it.toPlainString()}%" } ?: "—",
                tone = if (weft != null && weft > rules.shrinkageWeftWarnPct) Tone.WARNING else Tone.NEUTRAL,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(12.dp))
        FabricDiagram(warpPct = warp ?: BigDecimal.ZERO, weftPct = weft ?: BigDecimal.ZERO, growth = growth)

        if (warp != null && warp > rules.shrinkageWarpWarnPct) {
            Spacer(Modifier.height(8.dp))
            AlertBanner(Tone.WARNING, stringResource(R.string.shrinkage_warp_exceeds))
        }
        if (weft != null && weft > rules.shrinkageWeftWarnPct) {
            Spacer(Modifier.height(8.dp))
            AlertBanner(Tone.WARNING, stringResource(R.string.shrinkage_weft_exceeds))
        }
        if (warp != null && warp < BigDecimal.ZERO && !growth) {
            Spacer(Modifier.height(8.dp))
            AlertBanner(Tone.WARNING, stringResource(R.string.shrinkage_growth_hint))
        }

        SectionHeader(stringResource(R.string.shrinkage_formula_title))
        FormulaCard(
            title = stringResource(R.string.shrinkage_formula_title),
            formula = "Shrinkage % = (Original − Final) ÷ Original × 100",
            note = stringResource(R.string.shrinkage_formula_note, rules.shrinkageWarpWarnPct.toPlainString()),
        )
    }
}

private fun shrink(original: String, final: String, growth: Boolean): BigDecimal? {
    val o = original.toBigDecimalOrNull() ?: return null
    val f = final.toBigDecimalOrNull() ?: return null
    if (o.compareTo(BigDecimal.ZERO) <= 0) return null
    if (!growth && f < BigDecimal.ZERO) return null
    return (o - f).divide(o, 6, RoundingMode.HALF_UP).multiply(BigDecimal(100)).setScale(2, RoundingMode.HALF_UP)
}

@Composable
private fun FabricDiagram(warpPct: BigDecimal, weftPct: BigDecimal, growth: Boolean) {
    val outline = MaterialTheme.colorScheme.outline
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    Canvas(Modifier.fillMaxWidth().height(140.dp)) {
        val inset = 24f
        val w = size.width
        val h = size.height
        val shrinkX = (weftPct.toFloat() / 100f).coerceIn(-0.2f, 0.5f)
        val shrinkY = (warpPct.toFloat() / 100f).coerceIn(-0.2f, 0.5f)
        drawRect(
            color = outline,
            topLeft = Offset(inset, inset),
            size = androidx.compose.ui.geometry.Size(w - inset * 2, h - inset * 2),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f),
        )
        val fw = (w - inset * 2) * (1f - shrinkX)
        val fh = (h - inset * 2) * (1f - shrinkY)
        drawRect(
            color = primary.copy(alpha = 0.18f),
            topLeft = Offset(inset + shrinkX * (w - inset * 2) / 2f, inset + shrinkY * (h - inset * 2) / 2f),
            size = androidx.compose.ui.geometry.Size(fw, fh),
        )
        drawLine(secondary, Offset(w / 2, inset + 6f), Offset(w / 2, h - inset - 6f), strokeWidth = 2f)
        drawLine(primary, Offset(inset + 6f, h / 2), Offset(w - inset - 6f, h / 2), strokeWidth = 2f)
    }
}
