package com.alazhartex.costing.domain.engine

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf
import java.math.BigDecimal
import kotlin.test.assertEquals

class PricingEngineTest {

    private val engine = PricingEngine()

    @Test
    fun `solve first grade price normal case`() {
        // Target 10,000; second 20m @ 50 = 1,000; first 80m -> 112.50
        val result = engine.solveFirstGradePrice(
            targetRevenue = BigDecimal("10000"),
            qFirst = BigDecimal("80"),
            qSecond = BigDecimal("20"),
            pSecond = BigDecimal("50"),
        )
        assertEquals("112.50", result.getOrNull().toString())
    }

    @Test
    fun `solve first grade zero quantity fails`() {
        val result = engine.solveFirstGradePrice(
            BigDecimal("1000"), BigDecimal("0"), BigDecimal("10"), BigDecimal("50"),
        )
        assertInstanceOf(PricingEngine.PricingError.FirstGradeQtyZero::class.java,
            (result as Result.Failure).error)
    }

    @Test
    fun `reverse solve second grade price`() {
        val result = engine.solveSecondGradePrice(
            targetRevenue = BigDecimal("10000"),
            qFirst = BigDecimal("80"),
            pFirst = BigDecimal("112.50"),
            qSecond = BigDecimal("20"),
        )
        assertEquals("50.00", result.getOrNull().toString())
    }

    @Test
    fun `discount pct computes against reference`() {
        val d = engine.discountPct(BigDecimal("100"), BigDecimal("80"))
        assertEquals("20.00", d.toString())
    }

    @Test
    fun `sensitivity table covers seven deltas`() {
        val rows = engine.sensitivityTable(
            targetRevenue = BigDecimal("10000"),
            qFirst = BigDecimal("80"),
            qSecond = BigDecimal("20"),
            pSecond = BigDecimal("50"),
            baseSecondSharePct = BigDecimal("20"),
        )
        assertEquals(7, rows.size)
        assertEquals(BigDecimal.ZERO.setScale(2, Money.ROUNDING), rows.first().secondGradeDeltaPct)
    }
}
