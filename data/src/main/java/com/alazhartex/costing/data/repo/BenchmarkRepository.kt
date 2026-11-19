package com.alazhartex.costing.data.repo

import com.alazhartex.costing.data.local.dao.BenchmarkDao
import com.alazhartex.costing.data.local.entity.BenchmarkInsightEntity
import com.alazhartex.costing.domain.model.BenchmarkInsight

/** Caches benchmark insights per batch so the app works offline after one fetch. */
class BenchmarkRepository(private val dao: BenchmarkDao) {

    suspend fun latest(batchId: String): BenchmarkInsight? = dao.latest(batchId)?.toDomain()

    suspend fun cache(batchId: String, summaryEn: String, summaryAr: String, citations: List<String>) {
        val now = System.currentTimeMillis()
        dao.upsert(
            BenchmarkInsightEntity(
                id = "bi-" + batchId,
                batchId = batchId,
                queryHash = batchId,
                summaryEn = summaryEn,
                summaryAr = summaryAr,
                citations = citations,
                fetchedAt = now,
            ),
        )
    }

    private fun BenchmarkInsightEntity.toDomain(): BenchmarkInsight = BenchmarkInsight(
        id = id,
        batchId = batchId,
        queryHash = queryHash,
        summaryEn = summaryEn,
        summaryAr = summaryAr,
        citations = citations,
        fetchedAt = fetchedAt,
    )
}
