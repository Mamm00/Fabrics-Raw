package com.alazhartex.costing.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alazhartex.costing.data.local.dao.BenchmarkDao
import com.alazhartex.costing.data.local.dao.CostLayerDao
import com.alazhartex.costing.data.local.dao.FabricBatchDao
import com.alazhartex.costing.data.local.dao.GradeYieldDao
import com.alazhartex.costing.data.local.dao.PricingScenarioDao
import com.alazhartex.costing.data.local.dao.SettingsDao
import com.alazhartex.costing.data.local.dao.ShrinkageDao
import com.alazhartex.costing.data.local.entity.AppSettingsEntity
import com.alazhartex.costing.data.local.entity.BenchmarkInsightEntity
import com.alazhartex.costing.data.local.entity.CostLayerEntity
import com.alazhartex.costing.data.local.entity.FabricBatchEntity
import com.alazhartex.costing.data.local.entity.GradeYieldEntity
import com.alazhartex.costing.data.local.entity.PricingScenarioEntity
import com.alazhartex.costing.data.local.entity.ShrinkageRecordEntity

@Database(
    entities = [
        FabricBatchEntity::class,
        ShrinkageRecordEntity::class,
        GradeYieldEntity::class,
        CostLayerEntity::class,
        PricingScenarioEntity::class,
        BenchmarkInsightEntity::class,
        AppSettingsEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fabricBatchDao(): FabricBatchDao
    abstract fun shrinkageDao(): ShrinkageDao
    abstract fun gradeYieldDao(): GradeYieldDao
    abstract fun costLayerDao(): CostLayerDao
    abstract fun pricingScenarioDao(): PricingScenarioDao
    abstract fun benchmarkDao(): BenchmarkDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        const val NAME = "azhartex_costing.db"
    }
}
