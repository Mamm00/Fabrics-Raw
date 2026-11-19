package com.alazhartex.costing.data.settings

import com.alazhartex.costing.data.local.dao.SettingsDao
import com.alazhartex.costing.data.local.entity.AppSettingsEntity
import com.alazhartex.costing.domain.rules.TextileRules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.math.BigDecimal

data class AppSettings(
    val locale: String,
    val arabicIndicDigits: Boolean,
    val currency: String,
    val rules: TextileRules,
)

/** Persists locale, digit preference and the configurable rules snapshot. */
class SettingsRepository(private val dao: SettingsDao) {

    private val json = Json { ignoreUnknownKeys = true }

    fun observe(): Flow<AppSettings> = dao.observe().map { it?.toDomain() ?: defaults() }

    suspend fun update(
        locale: String,
        arabicIndicDigits: Boolean,
        currency: String,
        rules: TextileRules,
    ) {
        dao.upsert(
            AppSettingsEntity(
                id = 1,
                locale = locale,
                arabicIndicDigits = arabicIndicDigits,
                currency = currency,
                rulesJson = json.encodeToString(TextileRules.serializer(), rules),
            ),
        )
    }

    private fun defaults(): AppSettings = AppSettings(
        locale = "ar-EG",
        arabicIndicDigits = false,
        currency = TextileRules.DEFAULT.currency,
        rules = TextileRules.DEFAULT,
    )

    private fun AppSettingsEntity.toDomain(): AppSettings {
        val rules = runCatching {
            json.decodeFromString(TextileRules.serializer(), rulesJson)
        }.getOrDefault(TextileRules.DEFAULT.copy(currency = currency))
        return AppSettings(locale, arabicIndicDigits, currency, rules)
    }
}
