package com.alazhartex.costing.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

/** Raw fabric batch. Money stored as plain decimal strings via BigDecimal columns. */
@Entity(tableName = "fabric_batch")
data class FabricBatchEntity(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameAr: String,
    val supplierEn: String,
    val supplierAr: String,
    val purchaseUnit: String,
    val quantity: String,
    val gsm: String?,
    val widthCm: String?,
    val unitCost: String,
    val currency: String,
    val metersPerBolt: String?,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long,
)

@Entity(
    tableName = "shrinkage_record",
    foreignKeys = [ForeignKey(
        entity = FabricBatchEntity::class,
        parentColumns = ["id"],
        childColumns = ["batchId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("batchId")],
)
data class ShrinkageRecordEntity(
    @PrimaryKey val id: String,
    val batchId: String,
    val warpOriginal: String,
    val warpFinal: String,
    val weftOriginal: String,
    val weftFinal: String,
    val growthEnabled: Boolean,
    val warpPct: String,
    val weftPct: String,
    val flags: List<String>,
)

@Entity(
    tableName = "grade_yield",
    foreignKeys = [ForeignKey(
        entity = FabricBatchEntity::class,
        parentColumns = ["id"],
        childColumns = ["batchId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("batchId")],
)
data class GradeYieldEntity(
    @PrimaryKey val id: String,
    val batchId: String,
    val totalOutput: String,
    val qFirst: String,
    val qSecond: String,
    val qReject: String,
    val pctFirst: String,
    val pctSecond: String,
    val pctReject: String,
    val compensationRequired: Boolean,
)

@Entity(
    tableName = "cost_layer",
    foreignKeys = [ForeignKey(
        entity = FabricBatchEntity::class,
        parentColumns = ["id"],
        childColumns = ["batchId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("batchId")],
)
data class CostLayerEntity(
    @PrimaryKey val id: String,
    val batchId: String,
    val type: String,
    val amount: String,
    val pct: String?,
    val note: String,
)

@Entity(
    tableName = "pricing_scenario",
    foreignKeys = [ForeignKey(
        entity = FabricBatchEntity::class,
        parentColumns = ["id"],
        childColumns = ["batchId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("batchId")],
)
data class PricingScenarioEntity(
    @PrimaryKey val id: String,
    val batchId: String,
    val targetRevenue: String,
    val qFirst: String,
    val qSecond: String,
    val pFirst: String,
    val pSecond: String,
    val factoryPrice: String?,
    val priceAfterDyeing: String?,
    val perGradePrice: Map<String, String>,
    val sensitivity: List<SensitivityRowDto>,
    val updatedAt: Long,
)

@Entity(tableName = "benchmark_insight")
data class BenchmarkInsightEntity(
    @PrimaryKey val id: String,
    val batchId: String,
    val queryHash: String,
    val summaryEn: String,
    val summaryAr: String,
    val citations: List<String>,
    val fetchedAt: Long,
)

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val locale: String,
    val arabicIndicDigits: Boolean,
    val currency: String,
    val rulesJson: String,
)

/** Embedded DTO for the sensitivity table, serialized inside the scenario row. */
data class SensitivityRowDto(
    val secondGradeDeltaPct: String,
    val secondGradeSharePct: String,
    val pFirst: String,
)
