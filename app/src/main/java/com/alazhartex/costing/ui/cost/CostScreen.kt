package com.alazhartex.costing.ui.cost

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    var dyeingPct by remember { mutableStateOf("18") }
    var overhead by remember { mutableStateOf("") }
    var wastagePct by remember { mutableStateOf("3") }
    var inspection by remember { mutableStateOf("") }
    var vatPct by remember { mutableStateOf("14") }
    var meters by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }

    ScreenScaffold(title = "Cost Breakdown", onBack = onBack) {
        Column {
            LabeledField("Raw material (shrinkage-adjusted)", raw) { raw = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Weaving / processing", weaving) { weaving = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Dyeing / finishing %", dyeingPct) { dyeingPct = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Factory overhead", overhead) { overhead = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Wastage %", wastagePct) { wastagePct = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Inspection / packaging", inspection) { inspection = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("VAT %", vatPct) { vatPct = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Meters", meters) { meters = it }
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
                    batchKg = null,
                )
                val breakdown = container.costBuildUpEngine.build(input)
                result = "Total ${breakdown.totalCost} · /m ${breakdown.costPerMeter}" +
                    (breakdown.costPerKg?.let { " · /kg $it" } ?: "")
            }) { Text("Calculate") }
            result?.let { InfoCard("Result", listOf(it)) }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onNext) { Text("Next: Pricing") }
        }
    }
}
