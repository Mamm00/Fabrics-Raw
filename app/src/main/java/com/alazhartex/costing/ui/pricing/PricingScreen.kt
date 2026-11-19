package com.alazhartex.costing.ui.pricing

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
import com.alazhartex.costing.domain.engine.Money
import com.alazhartex.costing.domain.engine.Result
import com.alazhartex.costing.domain.model.SensitivityRow
import com.alazhartex.costing.ui.common.ErrorText
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
    var pFirst by remember { mutableStateOf("") }
    var scenarioLines by remember { mutableStateOf<List<String>>(emptyList()) }
    var sensitivityRows by remember { mutableStateOf<List<SensitivityRow>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    val invalid = stringResource(R.string.error_invalid_number)
    val notAvailable = "N/A"
    val requiredLabel = stringResource(R.string.pricing_required_first_price, "%s")
    val discountLabel = stringResource(R.string.pricing_discount_pct, "%s")

    ScreenScaffold(title = stringResource(R.string.nav_pricing), onBack = onBack) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            LabeledField(stringResource(R.string.pricing_target_revenue), targetRevenue) { targetRevenue = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.pricing_q_first), qFirst) { qFirst = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.pricing_q_second), qSecond) { qSecond = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.pricing_p_second), pSecond) { pSecond = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.pricing_p_first), pFirst) { pFirst = it }
            Spacer(Modifier.height(12.dp))

            Button(onClick = {
                val revenue = targetRevenue.toBigDecimalOrNull()
                val q1 = qFirst.toBigDecimalOrNull()
                val q2 = qSecond.toBigDecimalOrNull()
                val p2 = pSecond.toBigDecimalOrNull()
                val p1 = pFirst.toBigDecimalOrNull()
                if (revenue == null || q1 == null || q2 == null) {
                    error = invalid
                    scenarioLines = emptyList()
                    sensitivityRows = emptyList()
                    return@Button
                }
                error = null
                val computed = buildList {
                    if (p2 != null) {
                        val solved = container.pricingEngine.solveFirstGradePrice(revenue, q1, q2, p2)
                        add(
                            if (solved is Result.Success) requiredLabel.format(solved.value.toPlainString())
                            else notAvailable,
                        )
                    }
                    if (p1 != null && q2.signum() > 0) {
                        val solved = container.pricingEngine.solveSecondGradePrice(revenue, q1, p1, q2)
                        add(
                            if (solved is Result.Success) {
                                val discount = container.pricingEngine.discountPct(p1, solved.value)
                                discountLabel.format(discount.toPlainString())
                            } else {
                                notAvailable
                            },
                        )
                    }
                }
                val total = q1.add(q2)
                val sharePct = if (total.signum() == 0) BigDecimal.ZERO else Money.percent(q2, total)
                scenarioLines = computed
                sensitivityRows = if (p2 != null) {
                    container.pricingEngine.sensitivityTable(revenue, q1, q2, p2, sharePct)
                } else {
                    emptyList()
                }
            }) { Text(stringResource(R.string.action_solve_first_price)) }

            error?.let { ErrorText(it) }
            if (scenarioLines.isNotEmpty()) {
                InfoCard(title = stringResource(R.string.nav_pricing), lines = scenarioLines)
            }
            if (sensitivityRows.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.pricing_sensitivity))
                sensitivityRows.forEach { row ->
                    Text(
                        stringResource(
                            R.string.pricing_sensitivity_row,
                            row.secondGradeDeltaPct.toPlainString(),
                            row.secondGradeSharePct.toPlainString(),
                            row.pFirst.toPlainString(),
                        ),
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onBenchmark) { Text(stringResource(R.string.action_analyze)) }
        }
    }
}
