package com.alazhartex.costing.domain.engine

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * All monetary and percentage accumulation goes through here.
 * HALF_UP, fixed scale; never use Double for money.
 */
object Money {
    const val SCALE: Int = 2
    val ROUNDING: RoundingMode = RoundingMode.HALF_UP

    fun of(value: String): BigDecimal = BigDecimal(value).setScale(SCALE, ROUNDING)

    fun scale(value: BigDecimal): BigDecimal = value.setScale(SCALE, ROUNDING)

    fun add(a: BigDecimal, b: BigDecimal): BigDecimal = scale(a.add(b))

    fun multiply(a: BigDecimal, b: BigDecimal): BigDecimal = scale(a.multiply(b))

    fun divide(numerator: BigDecimal, denominator: BigDecimal): BigDecimal =
        if (denominator.signum() == 0) {
            throw ArithmeticException("division by zero")
        } else {
            numerator.divide(denominator, SCALE, ROUNDING)
        }

    fun percent(part: BigDecimal, whole: BigDecimal): BigDecimal =
        if (whole.signum() == 0) BigDecimal.ZERO.setScale(SCALE, ROUNDING)
        else scale(part.divide(whole, 6, ROUNDING).multiply(BigDecimal(100)))
}
