package com.alazhartex.costing.domain.engine

import com.alazhartex.costing.domain.model.ShrinkageFlag
import com.alazhartex.costing.domain.rules.TextileRules
import java.math.BigDecimal

/**
 * Shrinkage = ((Original - Final) / Original) * 100.
 * With growth enabled a negative result is allowed and flagged GROWTH.
 */
class ShrinkageEngine(private val rules: TextileRules) {

    data class Result(
        val warpPct: BigDecimal,
        val weftPct: BigDecimal,
        val flags: List<ShrinkageFlag>,
    )

    fun compute(
        warpOriginal: BigDecimal,
        warpFinal: BigDecimal,
        weftOriginal: BigDecimal,
        weftFinal: BigDecimal,
        growthEnabled: Boolean,
    ): Result {
        val warp = shrinkage(warpOriginal, warpFinal, growthEnabled)
        val weft = shrinkage(weftOriginal, weftFinal, growthEnabled)

        val flags = mutableListOf<ShrinkageFlag>()
        flagAxis(warp, rules.shrinkageWarpWarnPct, rules.shrinkageWarpFlagPct,
            ShrinkageFlag.WARP_WARN, ShrinkageFlag.WARP_FLAG, flags)
        flagAxis(weft, rules.shrinkageWeftWarnPct, rules.shrinkageWeftFlagPct,
            ShrinkageFlag.WEFT_WARN, ShrinkageFlag.WEFT_FLAG, flags)
        if (growthEnabled && (warp.signum() < 0 || weft.signum() < 0)) {
            flags.add(ShrinkageFlag.GROWTH)
        }
        return Result(warp, weft, flags)
    }

    private fun shrinkage(original: BigDecimal, final: BigDecimal, growthEnabled: Boolean): BigDecimal {
        if (original.signum() == 0) return BigDecimal.ZERO.setScale(Money.SCALE, Money.ROUNDING)
        val raw = original.subtract(final)
        if (!growthEnabled && raw.signum() < 0) {
            require(final <= original) { "final length exceeds original without growth toggle" }
        }
        return Money.percent(raw, original)
    }

    private fun flagAxis(
        pct: BigDecimal,
        warn: BigDecimal,
        flag: BigDecimal,
        warnFlag: ShrinkageFlag,
        flagFlag: ShrinkageFlag,
        out: MutableList<ShrinkageFlag>,
    ) {
        val abs = pct.abs()
        when {
            abs >= flag -> out.add(flagFlag)
            abs >= warn -> out.add(warnFlag)
        }
    }
}
