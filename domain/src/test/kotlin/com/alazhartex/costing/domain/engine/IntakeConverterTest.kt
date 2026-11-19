package com.alazhartex.costing.domain.engine

import com.alazhartex.costing.domain.model.PurchaseUnit
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertInstanceOf

class IntakeConverterTest {

    private val converter = IntakeConverter()

    @Test
    fun `meter unit passes through`() {
        val r = converter.perMeterCost(PurchaseUnit.METER, BigDecimal("45.5"), null, null, null)
        assertEquals("45.50", r.getOrNull().toString())
    }

    @Test
    fun `kg conversion via gsm and width`() {
        // costPerKg 60, GSM 150, width 1.50m -> g/m 225, kg/m 0.225 -> 13.50/m
        val r = converter.perMeterCost(
            PurchaseUnit.KG, BigDecimal("60"), BigDecimal("150"), BigDecimal("150"), null,
        )
        assertEquals("13.50", r.getOrNull().toString())
    }

    @Test
    fun `kg missing gsm fails`() {
        val r = converter.perMeterCost(PurchaseUnit.KG, BigDecimal("60"), null, BigDecimal("150"), null)
        assertInstanceOf(IntakeConverter.ConversionError.MissingGsm::class.java, (r as Result.Failure).error)
    }

    @Test
    fun `bolt conversion divides by meters per bolt`() {
        val r = converter.perMeterCost(PurchaseUnit.BOLT, BigDecimal("900"), null, null, BigDecimal("60"))
        assertEquals("15.00", r.getOrNull().toString())
    }

    @Test
    fun `bolt missing length fails`() {
        val r = converter.perMeterCost(PurchaseUnit.BOLT, BigDecimal("900"), null, null, null)
        assertInstanceOf(IntakeConverter.ConversionError.MissingBoltLength::class.java, (r as Result.Failure).error)
    }
}
