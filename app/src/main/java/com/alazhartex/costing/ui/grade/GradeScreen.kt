package com.alazhartex.costing.ui.grade

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
fun GradeScreen(container: AppContainer, batchId: String, onNext: () -> Unit, onBack: () -> Unit) {
    var total by remember { mutableStateOf("") }
    var first by remember { mutableStateOf("") }
    var second by remember { mutableStateOf("") }
    var reject by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }

    ScreenScaffold(title = "Grade Yield & Loss", onBack = onBack) {
        Column {
            LabeledField("Total output", total) { total = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("First grade", first) { first = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Second grade", second) { second = it }
            Spacer(Modifier.height(8.dp))
            LabeledField("Reject", reject) { reject = it }
            Spacer(Modifier.height(12.dp))
            Button(onClick = {
                val t = total.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val q1 = first.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val q2 = second.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val qr = reject.toBigDecimalOrNull() ?: BigDecimal.ZERO
                when (val r = container.gradeYieldEngine.compute(batchId, t, q1, q2, qr)) {
                    is Result.Success -> {
                        val g = r.value
                        result = "First ${g.pctFirst}% · Second ${g.pctSecond}% · Reject ${g.pctReject}%" +
                            if (g.compensationRequired) " · Compensation required" else ""
                    }
                    is Result.Failure -> result = "Invalid: ${r.error}"
                }
            }) { Text("Calculate") }
            result?.let { InfoCard("Result", listOf(it)) }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onNext) { Text("Next: Cost breakdown") }
        }
    }
}
