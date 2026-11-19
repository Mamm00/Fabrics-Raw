package com.alazhartex.costing.data.repo

import com.alazhartex.costing.data.local.dao.FabricBatchDao
import com.alazhartex.costing.data.local.entity.FabricBatchEntity
import com.alazhartex.costing.domain.model.FabricBatch
import com.alazhartex.costing.domain.model.PurchaseUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal

/** Single source of truth for fabric batches; maps domain models <-> Room entities. */
class BatchRepository(private val dao: FabricBatchDao) {

    fun observeAll(): Flow<List<FabricBatch>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeById(id: String): Flow<FabricBatch?> =
        dao.observeById(id).map { it?.toDomain() }

    suspend fun getById(id: String): FabricBatch? = dao.getById(id)?.toDomain()

    suspend fun upsert(batch: FabricBatch) = dao.upsert(batch.toEntity())

    suspend fun delete(id: String) = dao.deleteById(id)

    private fun FabricBatchEntity.toDomain(): FabricBatch = FabricBatch(
        id = id,
        nameEn = nameEn,
        nameAr = nameAr,
        supplierEn = supplierEn,
        supplierAr = supplierAr,
        purchaseUnit = runCatching { PurchaseUnit.valueOf(purchaseUnit) }.getOrDefault(PurchaseUnit.METER),
        quantity = BigDecimal(quantity),
        gsm = gsm?.let(::BigDecimal),
        widthCm = widthCm?.let(::BigDecimal),
        unitCost = BigDecimal(unitCost),
        currency = currency,
        metersPerBolt = metersPerBolt?.let(::BigDecimal),
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    private fun FabricBatch.toEntity(): FabricBatchEntity = FabricBatchEntity(
        id = id,
        nameEn = nameEn,
        nameAr = nameAr,
        supplierEn = supplierEn,
        supplierAr = supplierAr,
        purchaseUnit = purchaseUnit.name,
        quantity = quantity.toPlainString(),
        gsm = gsm?.toPlainString(),
        widthCm = widthCm?.toPlainString(),
        unitCost = unitCost.toPlainString(),
        currency = currency,
        metersPerBolt = metersPerBolt?.toPlainString(),
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
