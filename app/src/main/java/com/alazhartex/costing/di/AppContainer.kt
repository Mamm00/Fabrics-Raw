package com.alazhartex.costing.di

import android.content.Context
import com.alazhartex.costing.client.remote.BenchmarkClient
import com.alazhartex.costing.client.remote.HttpClientFactory
import com.alazhartex.costing.data.local.DatabaseFactory
import com.alazhartex.costing.data.repo.BatchRepository
import com.alazhartex.costing.data.settings.SettingsRepository
import com.alazhartex.costing.domain.engine.CostBuildUpEngine
import com.alazhartex.costing.domain.engine.GradeYieldEngine
import com.alazhartex.costing.domain.engine.IntakeConverter
import com.alazhartex.costing.domain.engine.PricingEngine
import com.alazhartex.costing.domain.engine.ShrinkageEngine
import com.alazhartex.costing.domain.rules.TextileRules

/**
 * Manual DI container (no Hilt, per project convention). Built once at app start
 * and shared across ViewModels.
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext
    private val database = DatabaseFactory.build(appContext)

    val batchRepository: BatchRepository = BatchRepository(database.fabricBatchDao())
    val settingsRepository: SettingsRepository = SettingsRepository(database.settingsDao())

    private val rules: TextileRules = TextileRules.DEFAULT

    val intakeConverter = IntakeConverter()
    val shrinkageEngine = ShrinkageEngine(rules)
    val gradeYieldEngine = GradeYieldEngine(rules)
    val costBuildUpEngine = CostBuildUpEngine(rules)
    val pricingEngine = PricingEngine()

    val benchmarkClient: BenchmarkClient by lazy {
        BenchmarkClient(HttpClientFactory.create(), baseUrl = "http://localhost:8080")
    }
}
