package com.alazhartex.costing.domain.model

import java.math.BigDecimal

/** Unit the raw purchase was measured in. */
enum class PurchaseUnit { METER, KG, BOLT }

/** Stock / grade status stored as a neutral enum; labels are localized in UI. */
enum class StockStatus { IN_STOCK, LOW_STOCK, OUT_OF_STOCK, DISCONTINUED }

enum class CostLayerType {
    RAW_MATERIAL, WEAVING_PROCESSING, DYEING_FINISHING,
    FACTORY_OVERHEAD, WASTAGE, INSPECTION_PACKAGING, TAX_VAT,
}

/** A raw fabric batch. Bilingual names are stored as-is; display uses BidiFormatter. */
data class FabricBatch(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val supplierEn: String,
    val supplierAr: String,
    val purchaseUnit: PurchaseUnit,
    val quantity: BigDecimal,
    val gsm: BigDecimal?,
    val widthCm: BigDecimal?,
    val unitCost: BigDecimal,
    val currency: String,
    val metersPerBolt: BigDecimal?,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long,
)

data class ShrinkageRecord(
    val id: String,
    val batchId: String,
    val warpOriginal: BigDecimal,
    val warpFinal: BigDecimal,
    val weftOriginal: BigDecimal,
    val weftFinal: BigDecimal,
    val growthEnabled: Boolean,
    val warpPct: BigDecimal,
    val weftPct: BigDecimal,
    val flags: List<ShrinkageFlag>,
)

enum class ShrinkageFlag { WARP_WARN, WARP_FLAG, WEFT_WARN, WEFT_FLAG, GROWTH }

data class GradeYield(
    val id: String,
    val batchId: String,
    val totalOutput: BigDecimal,
    val qFirst: BigDecimal,
    val qSecond: BigDecimal,
    val qReject: BigDecimal,
    val pctFirst: BigDecimal,
    val pctSecond: BigDecimal,
    val pctReject: BigDecimal,
    val compensationRequired: Boolean,
)

data class CostLayer(
    val id: String,
    val batchId: String,
    val type: CostLayerType,
    val amount: BigDecimal,
    val pct: BigDecimal?,
    val note: String,
)

data class CostBreakdown(
    val layers: List<CostLayer>,
    val costPerMeter: BigDecimal,
    val costPerKg: BigDecimal?,
    val totalCost: BigDecimal,
)

data class PricingScenario(
    val id: String,
    val batchId: String,
    val targetRevenue: BigDecimal,
    val qFirst: BigDecimal,
    val qSecond: BigDecimal,
    val pFirst: BigDecimal,
    val pSecond: BigDecimal,
    val factoryPrice: BigDecimal?,
    val priceAfterDyeing: BigDecimal?,
    val perGradePrice: Map<String, BigDecimal>,
    val sensitivity: List<SensitivityRow>,
)

data class SensitivityRow(
    val secondGradeDeltaPct: BigDecimal,
    val secondGradeSharePct: BigDecimal,
    val pFirst: BigDecimal,
)

data class BenchmarkInsight(
    val id: String,
    val batchId: String,
    val queryHash: String,
    val summaryEn: String,
    val summaryAr: String,
    val citations: List<String>,
    val fetchedAt: Long,
)
