package com.alazhartex.costing.domain.engine

import com.alazhartex.costing.domain.model.PurchaseUnit
import java.math.BigDecimal

/**
 * Weight-based intake: per-meter cost = (costPerKg * GSM * widthM) / 1000.
 * widthM = widthCm / 100. Bolt: per-meter = bolt cost / metersPerBolt.
 */
class IntakeConverter {

    sealed interface ConversionError {
        data object MissingGsm : ConversionError
        data object MissingWidth : ConversionError
        data object MissingBoltLength : ConversionError
        data object InvalidInput : ConversionError
    }

    fun perMeterCost(
        unit: PurchaseUnit,
        unitCost: BigDecimal,
        gsm: BigDecimal?,
        widthCm: BigDecimal?,
        metersPerBolt: BigDecimal?,
    ): Result<BigDecimal, ConversionError> {
        return when (unit) {
            PurchaseUnit.METER -> Result.Success(Money.scale(unitCost))

            PurchaseUnit.KG -> {
                val g = gsm ?: return Result.Failure(ConversionError.MissingGsm)
                val w = widthCm ?: return Result.Failure(ConversionError.MissingWidth)
                if (g.signum() <= 0 || w.signum() <= 0) {
                    return Result.Failure(ConversionError.InvalidInput)
                }
                val widthM = Money.divide(w, BigDecimal(100))
                val gramsPerMeter = Money.multiply(g, widthM)
                val kgPerMeter = Money.divide(gramsPerMeter, BigDecimal(1000))
                Result.Success(Money.multiply(unitCost, kgPerMeter))
            }

            PurchaseUnit.BOLT -> {
                val m = metersPerBolt ?: return Result.Failure(ConversionError.MissingBoltLength)
                if (m.signum() <= 0) return Result.Failure(ConversionError.InvalidInput)
                Result.Success(Money.divide(unitCost, m))
            }
        }
    }
}
