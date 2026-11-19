package com.alazhartex.costing.domain.engine

import com.alazhartex.costing.domain.model.SensitivityRow
import java.math.BigDecimal

/**
 * Pricing & compensation engine.
 * P_first = (TargetRevenue - (Q_second * P_second)) / Q_first
 * Reverse: fixed P_first -> required P_second, then discount vs reference.
 */
class PricingEngine {

    sealed interface PricingError {
        data object FirstGradeQtyZero : PricingError
        data object SecondGradeQtyZero : PricingError
        data object InvalidTargetRevenue : PricingError
    }

    fun solveFirstGradePrice(
        targetRevenue: BigDecimal,
        qFirst: BigDecimal,
        qSecond: BigDecimal,
        pSecond: BigDecimal,
    ): Result<BigDecimal, PricingError> {
        if (qFirst.signum() == 0) return Result.Failure(PricingError.FirstGradeQtyZero)
        if (targetRevenue.signum() < 0) return Result.Failure(PricingError.InvalidTargetRevenue)
        val secondRevenue = Money.multiply(qSecond, pSecond)
        val firstRevenue = targetRevenue.subtract(secondRevenue)
        return Result.Success(Money.divide(firstRevenue, qFirst))
    }

    fun solveSecondGradePrice(
        targetRevenue: BigDecimal,
        qFirst: BigDecimal,
        pFirst: BigDecimal,
        qSecond: BigDecimal,
    ): Result<BigDecimal, PricingError> {
        if (qSecond.signum() == 0) return Result.Failure(PricingError.SecondGradeQtyZero)
        val firstRevenue = Money.multiply(qFirst, pFirst)
        val secondRevenue = targetRevenue.subtract(firstRevenue)
        return Result.Success(Money.divide(secondRevenue, qSecond))
    }

    fun discountPct(referencePrice: BigDecimal, discountedPrice: BigDecimal): BigDecimal {
        if (referencePrice.signum() == 0) return BigDecimal.ZERO.setScale(Money.SCALE, Money.ROUNDING)
        return Money.percent(referencePrice.subtract(discountedPrice), referencePrice)
    }

    /** Sensitivity: P_first as second-grade share moves by -5,-2,-1,0,+1,+2,+5 points. */
    fun sensitivityTable(
        targetRevenue: BigDecimal,
        qFirst: BigDecimal,
        qSecond: BigDecimal,
        pSecond: BigDecimal,
        baseSecondSharePct: BigDecimal,
    ): List<SensitivityRow> {
        val deltas = listOf(-5, -2, -1, 0, 1, 2, 5).map { BigDecimal(it) }
        val total = qFirst.add(qSecond)
        if (total.signum() == 0) return emptyList()

        return deltas.map { delta ->
            val share = baseSecondSharePct.add(delta)
                .coerceAtLeast(BigDecimal.ZERO)
                .coerceAtMost(BigDecimal(100))
            // Re-split quantities proportionally to the new share, keep total output fixed.
            val newQSecond = Money.multiply(Money.divide(share, BigDecimal(100)), total)
            val newQFirst = total.subtract(newQSecond)
            val pFirst = if (newQFirst.signum() == 0) {
                null
            } else {
                val secondRevenue = Money.multiply(newQSecond, pSecond)
                Money.divide(targetRevenue.subtract(secondRevenue), newQFirst)
            }
            SensitivityRow(
                secondGradeDeltaPct = delta,
                secondGradeSharePct = share,
                pFirst = pFirst ?: BigDecimal.ZERO.setScale(Money.SCALE, Money.ROUNDING),
            )
        }
    }
}
