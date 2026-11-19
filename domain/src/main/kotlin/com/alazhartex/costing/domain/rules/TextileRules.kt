package com.alazhartex.costing.domain.rules

import java.math.BigDecimal

/**
 * All configurable textile reasonableness thresholds live here.
 * Nothing in the engine may hardcode a magic number; defaults are overridable
 * from Settings and persisted in AppSettings.
 */
data class TextileRules(
    val shrinkageWarpWarnPct: BigDecimal,
    val shrinkageWarpFlagPct: BigDecimal,
    val shrinkageWeftWarnPct: BigDecimal,
    val shrinkageWeftFlagPct: BigDecimal,
    val secondGradeCompensationThresholdPct: BigDecimal,
    val dyeingFinishingMinPct: BigDecimal,
    val dyeingFinishingMaxPct: BigDecimal,
    val defaultWastagePct: BigDecimal,
    val defaultVatPct: BigDecimal,
    val currency: String,
    val moneyScale: Int = 2,
) {
    companion object {
        val DEFAULT = TextileRules(
            shrinkageWarpWarnPct = BigDecimal("3.00"),
            shrinkageWarpFlagPct = BigDecimal("6.00"),
            shrinkageWeftWarnPct = BigDecimal("3.00"),
            shrinkageWeftFlagPct = BigDecimal("6.00"),
            secondGradeCompensationThresholdPct = BigDecimal("15.00"),
            dyeingFinishingMinPct = BigDecimal("12.00"),
            dyeingFinishingMaxPct = BigDecimal("25.00"),
            defaultWastagePct = BigDecimal("3.00"),
            defaultVatPct = BigDecimal("14.00"),
            currency = "EGP",
        )
    }
}
