package com.alazhartex.costing.ui.shrinkage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.domain.model.ShrinkageFlag
import com.alazhartex.costing.ui.common.InfoCard
import com.alazhartex.costing.ui.common.LabeledField
import com.alazhartex.costing.ui.common.ScreenScaffold
import java.math.BigDecimal

private data class ShrinkageOutcome(
    val warpPct: String,
    val weftPct: String,
    val flags: List<ShrinkageFlag>,
)

@Composable
fun ShrinkageScreen(container: AppContainer, batchId: String, onNext: () -> Unit, onBack: () -> Unit) {
    var warpOriginal by remember { mutableStateOf("") }
    var warpFinal by remember { mutableStateOf("") }
    var weftOriginal by remember { mutableStateOf("") }
    var weftFinal by remember { mutableStateOf("") }
    var growth by remember { mutableStateOf(false) }
    var outcome by remember { mutableStateOf<ShrinkageOutcome?>(null) }

    ScreenScaffold(title = stringResource(R.string.nav_shrinkage), onBack = onBack) {
        Column {
            LabeledField(stringResource(R.string.shrinkage_warp_original), warpOriginal) { warpOriginal = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.shrinkage_warp_final), warpFinal) { warpFinal = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.shrinkage_weft_original), weftOriginal) { weftOriginal = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.shrinkage_weft_final), weftFinal) { weftFinal = it }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = growth, onCheckedChange = { growth = it })
                Text(stringResource(R.string.shrinkage_allow_growth))
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = {
                outcome = runCatching {
                    container.shrinkageEngine.compute(
                        warpOriginal = warpOriginal.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        warpFinal = warpFinal.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        weftOriginal = weftOriginal.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        weftFinal = weftFinal.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        growthEnabled = growth,
                    )
                }.getOrNull()?.let { result ->
                    ShrinkageOutcome(
                        warpPct = result.warpPct.toPlainString(),
                        weftPct = result.weftPct.toPlainString(),
                        flags = result.flags,
                    )
                }
            }) { Text(stringResource(R.string.action_calculate)) }

            val computed = outcome
            if (computed != null) {
                val flagsText = computed.flags
                    .joinToString(", ") { it.name }
                    .ifBlank { stringResource(R.string.shrinkage_flag_none) }
                InfoCard(
                    title = stringResource(R.string.nav_shrinkage),
                    lines = listOf(
                        stringResource(R.string.shrinkage_result, computed.warpPct, computed.weftPct, flagsText),
                    ),
                )
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onNext) { Text(stringResource(R.string.action_next_grade)) }
        }
    }
}
