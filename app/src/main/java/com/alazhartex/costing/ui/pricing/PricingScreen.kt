package com.alazhartex.costing.ui.pricing

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
import com.alazhartex.costing.domain.engine.Result
import com.alazhartex.costing.ui.common.InfoCard
import com.alazhartex.costing.ui.common.LabeledField
import com.alazhartex.costing.ui.common.ScreenScaffold
import java.math.BigDecimal

@Composable
fun PricingScreen(container: AppContainer, batchId: String, onBenchmark: () -> Unit, onBack: () -> Unit) {
    var targetRevenue by remember { mutableStateOf("") }
    var qFirst by remember { mutableStateOf("") }
    var qSecond by remember { mutableStateOf("") }
    var pSecond by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }

    ScreenScaffold(title = "Pricing & Compensation", onBack = onBack) {
        Column {
            LabeledField("Target revenue", targetRevenue) { targetRevenue = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Q first grade", qFirst) { qFirst = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Q second grade", qSecond) { qSecond = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Price second grade", pSecond) { pSecond = it }
            Spacer(Modifier.height(12.dp))
            Button(onClick = {
                val t = targetRevenue.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val q1 = qFirst.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val q2 = qSecond.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val p2 = pSecond.toBigDecimalOrNull() ?: BigDecimal.ZERO
                when (val r = container.pricingEngine.solveFirstGradePrice(t, q1, q2, p2)) {
                    is Result.Success -> result = "Required first-grade price: ${r.value}"
                    is Result.Failure -> result = "Invalid: ${r.error}"
                }
            }) { Text("Solve first-grade price") }
            result?.let { InfoCard("Result", listOf(it)) }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onBenchmark) { Text("Analyze this operation") }
        }
    }
}
