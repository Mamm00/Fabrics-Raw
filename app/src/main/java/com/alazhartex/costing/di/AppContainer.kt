package com.alazhartex.costing.di

import android.content.Context
import android.os.Build
import android.os.LocaleList
import com.alazhartex.costing.BuildConfig
import com.alazhartex.costing.client.remote.BenchmarkClient
import com.alazhartex.costing.client.remote.HttpClientFactory
import com.alazhartex.costing.data.local.DatabaseFactory
import com.alazhartex.costing.data.repo.BatchRepository
import com.alazhartex.costing.data.repo.BenchmarkRepository
import com.alazhartex.costing.data.settings.SettingsRepository
import com.alazhartex.costing.domain.engine.CostBuildUpEngine
import com.alazhartex.costing.domain.engine.GradeYieldEngine
import com.alazhartex.costing.domain.engine.IntakeConverter
import com.alazhartex.costing.domain.engine.PricingEngine
import com.alazhartex.costing.domain.engine.ShrinkageEngine
import com.alazhartex.costing.domain.rules.TextileRules

/**
 * Manual DI container (no Hilt, per project convention). Built once at app start
 * and shared across ViewModels. The backend base URL comes from BuildConfig, so
 * no secrets or environment-specific hosts live in source.
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext
    private val database = DatabaseFactory.build(appContext)

    val batchRepository: BatchRepository = BatchRepository(database.fabricBatchDao())
    val benchmarkRepository: BenchmarkRepository = BenchmarkRepository(database.benchmarkDao())
    val settingsRepository: SettingsRepository = SettingsRepository(database.settingsDao())

    val defaultRules: TextileRules = TextileRules.DEFAULT

    val intakeConverter = IntakeConverter()
    val shrinkageEngine = ShrinkageEngine(defaultRules)
    val gradeYieldEngine = GradeYieldEngine(defaultRules)
    val costBuildUpEngine = CostBuildUpEngine(defaultRules)
    val pricingEngine = PricingEngine()

    val benchmarkClient: BenchmarkClient by lazy {
        BenchmarkClient(HttpClientFactory.create(), baseUrl = BuildConfig.BACKEND_BASE_URL)
    }

    /**
     * Applies the chosen locale. API 33+ uses the per-app language service;
     * older versions store the preference and apply it on next launch.
     */
    fun applyLocale(tag: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val manager = appContext.getSystemService(android.app.LocaleManager::class.java) ?: return
            manager.applicationLocales = LocaleList.forLanguageTags(tag)
        }
    }
}
