package com.alazhartex.costing.ui.cost

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.domain.engine.CostBuildUpEngine
import com.alazhartex.costing.ui.common.InfoCard
import com.alazhartex.costing.ui.common.LabeledField
import com.alazhartex.costing.ui.common.ScreenScaffold
import java.math.BigDecimal

@Composable
fun CostScreen(container: AppContainer, batchId: String, onNext: () -> Unit, onBack: () -> Unit) {
    var raw by remember { mutableStateOf("") }
    var weaving by remember { mutableStateOf("") }
    var dyeingPct by remember { mutableStateOf(container.defaultRules.dyeingFinishingMinPct.toPlainString()) }
    var overhead by remember { mutableStateOf("") }
    var wastagePct by remember { mutableStateOf(container.defaultRules.defaultWastagePct.toPlainString()) }
    var inspection by remember { mutableStateOf("") }
    var vatPct by remember { mutableStateOf(container.defaultRules.defaultVatPct.toPlainString()) }
    var meters by remember { mutableStateOf("") }
    var batchKg by remember { mutableStateOf("") }
    var lines by remember { mutableStateOf<List<String>?>(null) }

    ScreenScaffold(title = stringResource(R.string.nav_cost), onBack = onBack) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            LabeledField(stringResource(R.string.cost_raw_material), raw) { raw = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.cost_weaving), weaving) { weaving = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.cost_dyeing_pct), dyeingPct) { dyeingPct = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.cost_overhead), overhead) { overhead = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.cost_wastage_pct), wastagePct) { wastagePct = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.cost_inspection), inspection) { inspection = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.cost_vat_pct), vatPct) { vatPct = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.cost_meters), meters) { meters = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.cost_batch_kg), batchKg) { batchKg = it }
            Spacer(Modifier.height(12.dp))

            Text(
                stringResource(
                    R.string.cost_dyeing_guidance,
                    container.defaultRules.dyeingFinishingMinPct.toPlainString(),
                    container.defaultRules.dyeingFinishingMaxPct.toPlainString(),
                ),
            )
            Spacer(Modifier.height(12.dp))

            Button(onClick = {
                val input = CostBuildUpEngine.Input(
                    batchId = batchId,
                    rawMaterialCost = raw.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    shrinkageAdjustedRawCost = raw.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    weavingProcessing = weaving.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    dyeingFinishingPct = dyeingPct.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    factoryOverhead = overhead.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    wastagePct = wastagePct.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    inspectionPackaging = inspection.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    vatPct = vatPct.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    meters = meters.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    batchKg = batchKg.toBigDecimalOrNull(),
                )
                val breakdown = container.costBuildUpEngine.build(input)
                lines = buildList {
                    breakdown.layers.forEach { layer ->
                        add(layer.type.name.replace('_', ' ') + ": " + layer.amount.toPlainString())
                    }
                    add("TOTAL: " + breakdown.totalCost.toPlainString())
                    add("PER METER: " + breakdown.costPerMeter.toPlainString())
                    breakdown.costPerKg?.let { add("PER KG: " + it.toPlainString()) }
                }
            }) { Text(stringResource(R.string.action_calculate)) }

            lines?.let { InfoCard(title = stringResource(R.string.nav_cost), lines = it) }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onNext) { Text(stringResource(R.string.action_next_pricing)) }
        }
    }
}
