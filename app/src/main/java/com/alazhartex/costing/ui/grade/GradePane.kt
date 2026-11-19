package com.alazhartex.costing.ui.grade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.alazhartex.costing.domain.engine.GradeYieldEngine
import com.alazhartex.costing.domain.engine.Result
import com.alazhartex.costing.domain.model.GradeYield
import com.alazhartex.costing.ui.common.LabeledField
import com.alazhartex.costing.ui.components.AlertBanner
import com.alazhartex.costing.ui.components.KpiCard
import com.alazhartex.costing.ui.components.LedgerRow
import com.alazhartex.costing.ui.components.SectionHeader
import com.alazhartex.costing.ui.components.StackedGradeBar
import com.alazhartex.costing.ui.components.Tone

@Composable
fun GradePane(container: AppContainer, batchId: String, onOpenPricing: () -> Unit) {
    var total by remember { mutableStateOf("") }
    var first by remember { mutableStateOf("") }
    var second by remember { mutableStateOf("") }
    var reject by remember { mutableStateOf("") }
    var outcome by remember { mutableStateOf<GradeYield?>(null) }
    var error by remember { mutableStateOf<Int?>(null) }

    Column {
        LabeledField(stringResource(R.string.grade_total_output), total, Modifier.fillMaxWidth()) { total = it }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth()) {
            LabeledField(stringResource(R.string.grade_first), first, Modifier.weight(1f)) { first = it }
            Spacer(Modifier.height(4.dp))
            LabeledField(stringResource(R.string.grade_second), second, Modifier.weight(1f)) { second = it }
        }
        Spacer(Modifier.height(8.dp))
        LabeledField(stringResource(R.string.grade_reject), reject, Modifier.fillMaxWidth()) { reject = it }

        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            val parsed = listOf(total, first, second, reject).map { it.toBigDecimalOrNull() }
            if (parsed.any { it == null }) { error = R.string.error_invalid_number; return@Button }
            when (val computed = container.gradeYieldEngine.compute(
                batchId = batchId,
                totalOutput = parsed[0]!!,
                qFirst = parsed[1]!!,
                qSecond = parsed[2]!!,
                qReject = parsed[3]!!,
            )) {
                is Result.Success -> { outcome = computed.value; error = null }
                is Result.Failure -> {
                    outcome = null
                    error = when (computed.error) {
                        GradeYieldEngine.GradeError.NegativeQuantity -> R.string.grade_error_negative
                        is GradeYieldEngine.GradeError.GradesExceedOutput -> R.string.grade_error_exceeds
                    }
                }
            }
        }) { Text(stringResource(R.string.action_calculate)) }

        error?.let {
            Spacer(Modifier.height(8.dp))
            AlertBanner(Tone.ERROR, stringResource(it))
        }

        outcome?.let { g ->
            SectionHeader(stringResource(R.string.grade_results))
            Row(Modifier.fillMaxWidth()) {
                KpiCard(stringResource(R.string.grade_first), g.pctFirst.toPlainString() + "%", caption = null, tone = Tone.SUCCESS, modifier = Modifier.weight(1f))
                KpiCard(stringResource(R.string.grade_second), g.pctSecond.toPlainString() + "%", caption = null, tone = Tone.WARNING, modifier = Modifier.weight(1f))
                KpiCard(stringResource(R.string.grade_reject), g.pctReject.toPlainString() + "%", caption = null, tone = Tone.ERROR, modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            StackedGradeBar(g.pctFirst.toFloat(), g.pctSecond.toFloat(), g.pctReject.toFloat())
            Spacer(Modifier.height(12.dp))
            LedgerRow(stringResource(R.string.grade_total_output), g.totalOutput.toPlainString())
            LedgerRow(stringResource(R.string.grade_first), g.qFirst.toPlainString())
            LedgerRow(stringResource(R.string.grade_second), g.qSecond.toPlainString())
            LedgerRow(stringResource(R.string.grade_reject), g.qReject.toPlainString(), emphasis = true)
            if (g.compensationRequired) {
                Spacer(Modifier.height(8.dp))
                AlertBanner(Tone.WARNING, stringResource(R.string.grade_compensation_required))
                Spacer(Modifier.height(8.dp))
                Button(onClick = onOpenPricing, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.grade_open_pricing))
                }
            }
        }
        if (outcome == null && error == null) {
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.grade_empty_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
