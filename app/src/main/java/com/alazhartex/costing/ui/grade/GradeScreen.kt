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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.domain.engine.GradeYieldEngine
import com.alazhartex.costing.domain.engine.Result
import com.alazhartex.costing.ui.common.ErrorText
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
    var lines by remember { mutableStateOf<List<String>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    ScreenScaffold(title = stringResource(R.string.nav_grade_yield), onBack = onBack) {
        Column {
            LabeledField(stringResource(R.string.grade_total_output), total) { total = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.grade_first), first) { first = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.grade_second), second) { second = it }
            Spacer(Modifier.height(8.dp))
            LabeledField(stringResource(R.string.grade_reject), reject) { reject = it }
            Spacer(Modifier.height(12.dp))

            val errorNegative = stringResource(R.string.grade_error_negative)
            val errorExceeds = stringResource(R.string.grade_error_exceeds)
            val compensationLabel = stringResource(R.string.grade_compensation_required)
            Button(onClick = {
                val outcome = container.gradeYieldEngine.compute(
                    batchId = batchId,
                    totalOutput = total.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    qFirst = first.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    qSecond = second.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    qReject = reject.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                )
                when (outcome) {
                    is Result.Success -> {
                        val g = outcome.value
                        error = null
                        val summary = buildString {
                            append(g.pctFirst.toPlainString()).append("% / ")
                            append(g.pctSecond.toPlainString()).append("% / ")
                            append(g.pctReject.toPlainString()).append("%")
                        }
                        lines = if (g.compensationRequired) {
                            listOf(summary, compensationLabel)
                        } else {
                            listOf(summary)
                        }
                    }
                    is Result.Failure -> {
                        lines = emptyList()
                        error = when (outcome.error) {
                            is GradeYieldEngine.GradeError.NegativeQuantity -> errorNegative
                            is GradeYieldEngine.GradeError.GradesExceedOutput -> errorExceeds
                        }
                    }
                }
            }) { Text(stringResource(R.string.action_calculate)) }

            error?.let { ErrorText(it) }
            if (lines.isNotEmpty()) {
                InfoCard(title = stringResource(R.string.nav_grade_yield), lines = lines)
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onNext) { Text(stringResource(R.string.action_next_cost)) }
        }
    }
}
