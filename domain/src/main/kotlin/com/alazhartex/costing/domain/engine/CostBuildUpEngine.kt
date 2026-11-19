package com.alazhartex.costing.domain.engine

import com.alazhartex.costing.domain.model.CostBreakdown
import com.alazhartex.costing.domain.model.CostLayer
import com.alazhartex.costing.domain.model.CostLayerType
import com.alazhartex.costing.domain.rules.TextileRules
import java.math.BigDecimal

/**
 * Layered cost build-up. Raw material is adjusted for shrinkage; dyeing/finishing
 * defaults to the rules band (12-25% of greige), editable by the caller.
 */
class CostBuildUpEngine(private val rules: TextileRules) {

    data class Input(
        val batchId: String,
        val rawMaterialCost: BigDecimal,
        val shrinkageAdjustedRawCost: BigDecimal,
        val weavingProcessing: BigDecimal,
        val dyeingFinishingPct: BigDecimal,
        val factoryOverhead: BigDecimal,
        val wastagePct: BigDecimal,
        val inspectionPackaging: BigDecimal,
        val vatPct: BigDecimal,
        val meters: BigDecimal,
        val batchKg: BigDecimal?,
    )

    fun build(input: Input): CostBreakdown {
        val greige = Money.add(input.shrinkageAdjustedRawCost, input.weavingProcessing)
        val dyeing = Money.multiply(greige, Money.divide(input.dyeingFinishingPct, BigDecimal(100)))
        val subtotalBeforeWaste = listOf(
            input.shrinkageAdjustedRawCost,
            input.weavingProcessing,
            dyeing,
            input.factoryOverhead,
            input.inspectionPackaging,
        ).fold(BigDecimal.ZERO, Money::add)

        val wastage = Money.multiply(subtotalBeforeWaste, Money.divide(input.wastagePct, BigDecimal(100)))
        val preTax = Money.add(subtotalBeforeWaste, wastage)
        val vat = Money.multiply(preTax, Money.divide(input.vatPct, BigDecimal(100)))
        val total = Money.add(preTax, vat)

        val layers = listOf(
            CostLayer(input.batchId + "-raw", input.batchId, CostLayerType.RAW_MATERIAL,
                input.shrinkageAdjustedRawCost, null, "shrinkage-adjusted"),
            CostLayer(input.batchId + "-weave", input.batchId, CostLayerType.WEAVING_PROCESSING,
                input.weavingProcessing, null, ""),
            CostLayer(input.batchId + "-dye", input.batchId, CostLayerType.DYEING_FINISHING,
                dyeing, input.dyeingFinishingPct, "guidance ${rules.dyeingFinishingMinPct}-${rules.dyeingFinishingMaxPct}% of greige"),
            CostLayer(input.batchId + "-overhead", input.batchId, CostLayerType.FACTORY_OVERHEAD,
                input.factoryOverhead, null, ""),
            CostLayer(input.batchId + "-waste", input.batchId, CostLayerType.WASTAGE,
                wastage, input.wastagePct, ""),
            CostLayer(input.batchId + "-inspect", input.batchId, CostLayerType.INSPECTION_PACKAGING,
                input.inspectionPackaging, null, ""),
            CostLayer(input.batchId + "-vat", input.batchId, CostLayerType.TAX_VAT,
                vat, input.vatPct, ""),
        )

        val costPerMeter = if (input.meters.signum() == 0) BigDecimal.ZERO else Money.divide(total, input.meters)
        val costPerKg = input.batchKg?.takeIf { it.signum() > 0 }?.let { Money.divide(total, it) }

        return CostBreakdown(layers, costPerMeter, costPerKg, total)
    }
}
