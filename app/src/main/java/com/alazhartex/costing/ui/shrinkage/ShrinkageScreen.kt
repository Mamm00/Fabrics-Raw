package com.alazhartex.costing.ui.shrinkage

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
import com.alazhartex.costing.domain.engine.Money
import com.alazhartex.costing.ui.common.InfoCard
import com.alazhartex.costing.ui.common.LabeledField
import com.alazhartex.costing.ui.common.ScreenScaffold
import java.math.BigDecimal

@Composable
fun ShrinkageScreen(container: AppContainer, batchId: String, onNext: () -> Unit, onBack: () -> Unit) {
    var warpOriginal by remember { mutableStateOf("") }
    var warpFinal by remember { mutableStateOf("") }
    var weftOriginal by remember { mutableStateOf("") }
    var weftFinal by remember { mutableStateOf("") }
    var growth by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }

    ScreenScaffold(title = "Shrinkage", onBack = onBack) {
        Column {
            LabeledField("Warp original", warpOriginal) { warpOriginal = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Warp final", warpFinal) { warpFinal = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Weft original", weftOriginal) { weftOriginal = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Weft final", weftFinal) { weftFinal = it }
            Spacer(Modifier.height(12.dp))
            Button(onClick = {
                val wo = warpOriginal.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val wf = warpFinal.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val eo = weftOriginal.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val ef = weftFinal.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val r = container.shrinkageEngine.compute(wo, wf, eo, ef, growth)
                result = "Warp ${r.warpPct}% · Weft ${r.weftPct}% · Flags: ${r.flags.joinToString()}"
            }) { Text("Calculate") }
            result?.let { InfoCard("Result", listOf(it)) }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onNext) { Text("Next: Grade yield") }
        }
    }
}
