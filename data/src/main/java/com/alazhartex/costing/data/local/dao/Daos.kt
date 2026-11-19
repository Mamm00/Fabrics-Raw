package com.alazhartex.costing.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.alazhartex.costing.data.local.entity.BenchmarkInsightEntity
import com.alazhartex.costing.data.local.entity.CostLayerEntity
import com.alazhartex.costing.data.local.entity.FabricBatchEntity
import com.alazhartex.costing.data.local.entity.GradeYieldEntity
import com.alazhartex.costing.data.local.entity.PricingScenarioEntity
import com.alazhartex.costing.data.local.entity.ShrinkageRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FabricBatchDao {
    @Query("SELECT * FROM fabric_batch ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<FabricBatchEntity>>

    @Query("SELECT * FROM fabric_batch WHERE id = :id")
    fun observeById(id: String): Flow<FabricBatchEntity?>

    @Query("SELECT * FROM fabric_batch WHERE id = :id")
    suspend fun getById(id: String): FabricBatchEntity?

    @Transaction
    @Upsert
    suspend fun upsert(batch: FabricBatchEntity)

    @Query("DELETE FROM fabric_batch WHERE id = :id")
    suspend fun deleteById(id: String)
}

@Dao
interface ShrinkageDao {
    @Query("SELECT * FROM shrinkage_record WHERE batchId = :batchId ORDER BY updatedAt DESC LIMIT 1")
    fun observeForBatch(batchId: String): Flow<ShrinkageRecordEntity?>

    @Transaction
    @Upsert
    suspend fun upsert(record: ShrinkageRecordEntity)
}

@Dao
interface GradeYieldDao {
    @Query("SELECT * FROM grade_yield WHERE batchId = :batchId ORDER BY updatedAt DESC LIMIT 1")
    fun observeForBatch(batchId: String): Flow<GradeYieldEntity?>

    @Transaction
    @Upsert
    suspend fun upsert(yield: GradeYieldEntity)
}

@Dao
interface CostLayerDao {
    @Query("SELECT * FROM cost_layer WHERE batchId = :batchId")
    fun observeForBatch(batchId: String): Flow<List<CostLayerEntity>>

    @Transaction
    suspend fun replaceForBatch(batchId: String, layers: List<CostLayerEntity>) {
        deleteForBatch(batchId)
        layers.forEach { upsert(it) }
    }

    @Query("DELETE FROM cost_layer WHERE batchId = :batchId")
    suspend fun deleteForBatch(batchId: String)

    @Upsert
    suspend fun upsert(layer: CostLayerEntity)
}

@Dao
interface PricingScenarioDao {
    @Query("SELECT * FROM pricing_scenario WHERE batchId = :batchId ORDER BY updatedAt DESC LIMIT 1")
    fun observeForBatch(batchId: String): Flow<PricingScenarioEntity?>

    @Transaction
    @Upsert
    suspend fun upsert(scenario: PricingScenarioEntity)
}

@Dao
interface BenchmarkDao {
    @Query("SELECT * FROM benchmark_insight WHERE batchId = :batchId ORDER BY fetchedAt DESC LIMIT 1")
    fun observeForBatch(batchId: String): Flow<BenchmarkInsightEntity?>

    @Query("SELECT * FROM benchmark_insight WHERE batchId = :batchId ORDER BY fetchedAt DESC LIMIT 1")
    suspend fun latest(batchId: String): BenchmarkInsightEntity?

    @Transaction
    @Upsert
    suspend fun upsert(insight: BenchmarkInsightEntity)
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings WHERE id = 1")
    fun observe(): Flow<com.alazhartex.costing.data.local.entity.AppSettingsEntity?>

    @Upsert
    suspend fun upsert(settings: com.alazhartex.costing.data.local.entity.AppSettingsEntity)
}
