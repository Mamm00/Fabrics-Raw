package com.alazhartex.costing.domain.engine

import com.alazhartex.costing.domain.rules.TextileRules
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertInstanceOf
import kotlin.test.assertTrue

class GradeYieldEngineTest {

    private val engine = GradeYieldEngine(TextileRules.DEFAULT)

    @Test
    fun `percentages sum to output`() {
        val r = engine.compute("b1", BigDecimal("100"), BigDecimal("70"), BigDecimal("20"), BigDecimal("10"))
        val g = (r as Result.Success).value
        assertEquals("70.00", g.pctFirst.toString())
        assertEquals("20.00", g.pctSecond.toString())
        assertEquals("10.00", g.pctReject.toString())
        assertTrue(g.compensationRequired) // 20% > 15% threshold
    }

    @Test
    fun `grades exceeding output rejected`() {
        val r = engine.compute("b1", BigDecimal("100"), BigDecimal("60"), BigDecimal("50"), BigDecimal("10"))
        assertInstanceOf(GradeYieldEngine.GradeError.GradesExceedOutput::class.java,
            (r as Result.Failure).error)
    }

    @Test
    fun `negative quantity rejected`() {
        val r = engine.compute("b1", BigDecimal("100"), BigDecimal("-1"), BigDecimal("20"), BigDecimal("10"))
        assertInstanceOf(GradeYieldEngine.GradeError.NegativeQuantity::class.java,
            (r as Result.Failure).error)
    }

    @Test
    fun `low second grade needs no compensation`() {
        val r = engine.compute("b1", BigDecimal("100"), BigDecimal("90"), BigDecimal("5"), BigDecimal("5"))
        assertFalse((r as Result.Success).value.compensationRequired)
    }
}
