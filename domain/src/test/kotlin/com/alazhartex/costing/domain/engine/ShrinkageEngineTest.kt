package com.alazhartex.costing.domain.engine

import com.alazhartex.costing.domain.model.ShrinkageFlag
import com.alazhartex.costing.domain.rules.TextileRules
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import java.math.BigDecimal

class ShrinkageEngineTest {

    private val engine = ShrinkageEngine(TextileRules.DEFAULT)

    @Test
    fun `shrinkage percent warp and weft`() {
        val r = engine.compute(
            warpOriginal = BigDecimal("100"), warpFinal = BigDecimal("96"),
            weftOriginal = BigDecimal("100"), weftFinal = BigDecimal("98"),
            growthEnabled = false,
        )
        assertEquals("4.00", r.warpPct.toString())
        assertEquals("2.00", r.weftPct.toString())
        assertTrue(r.flags.contains(ShrinkageFlag.WARP_WARN))
        assertFalse(r.flags.contains(ShrinkageFlag.WEFT_WARN))
    }

    @Test
    fun `growth toggle allows negative shrinkage and flags growth`() {
        val r = engine.compute(
            BigDecimal("100"), BigDecimal("102"),
            BigDecimal("100"), BigDecimal("100"),
            growthEnabled = true,
        )
        assertEquals("-2.00", r.warpPct.toString())
        assertTrue(r.flags.contains(ShrinkageFlag.GROWTH))
    }

    @Test
    fun `flag level above threshold`() {
        val r = engine.compute(
            BigDecimal("100"), BigDecimal("93"),
            BigDecimal("100"), BigDecimal("100"),
            growthEnabled = false,
        )
        assertTrue(r.flags.contains(ShrinkageFlag.WARP_FLAG))
    }
}
