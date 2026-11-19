package com.alazhartex.costing.domain.rules

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

/** BigDecimal serialized as its plain decimal string so rules persist exactly. */
object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("java.math.BigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toPlainString())
    }

    override fun deserialize(decoder: Decoder): BigDecimal =
        BigDecimal(decoder.decodeString())
}

/**
 * All configurable textile reasonableness thresholds live here.
 * Nothing in the engine may hardcode a magic number; defaults are overridable
 * from Settings and persisted in AppSettings.
 */
@Serializable
data class TextileRules(
    @Serializable(with = BigDecimalSerializer::class)
    val shrinkageWarpWarnPct: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val shrinkageWarpFlagPct: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val shrinkageWeftWarnPct: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val shrinkageWeftFlagPct: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val secondGradeCompensationThresholdPct: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val dyeingFinishingMinPct: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val dyeingFinishingMaxPct: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val defaultWastagePct: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
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
