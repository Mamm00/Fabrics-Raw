package com.alazhartex.costing.domain.engine

import com.alazhartex.costing.domain.model.GradeYield
import com.alazhartex.costing.domain.rules.TextileRules
import java.math.BigDecimal

class GradeYieldEngine(private val rules: TextileRules) {

    sealed interface GradeError {
        data object NegativeQuantity : GradeError
        data class GradesExceedOutput(val sum: BigDecimal, val output: BigDecimal) : GradeError
    }

    fun compute(
        batchId: String,
        totalOutput: BigDecimal,
        qFirst: BigDecimal,
        qSecond: BigDecimal,
        qReject: BigDecimal,
    ): Result<GradeYield, GradeError> {
        if (totalOutput.signum() < 0 || qFirst.signum() < 0 ||
            qSecond.signum() < 0 || qReject.signum() < 0
        ) return Result.Failure(GradeError.NegativeQuantity)

        val sum = qFirst.add(qSecond).add(qReject)
        if (sum > totalOutput) {
            return Result.Failure(GradeError.GradesExceedOutput(sum, totalOutput))
        }

        val pctFirst = Money.percent(qFirst, totalOutput)
        val pctSecond = Money.percent(qSecond, totalOutput)
        val pctReject = Money.percent(qReject, totalOutput)
        val compensationRequired = pctSecond > rules.secondGradeCompensationThresholdPct

        return Result.Success(
            GradeYield(
                id = batchId,
                batchId = batchId,
                totalOutput = totalOutput,
                qFirst = qFirst,
                qSecond = qSecond,
                qReject = qReject,
                pctFirst = pctFirst,
                pctSecond = pctSecond,
                pctReject = pctReject,
                compensationRequired = compensationRequired,
            ),
        )
    }
}
