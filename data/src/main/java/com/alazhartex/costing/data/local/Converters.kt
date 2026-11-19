package com.alazhartex.costing.data.local

import androidx.room3.TypeConverter
import com.alazhartex.costing.data.local.entity.SensitivityRowDto
import kotlinx.serialization.json.Json

/** Room type converters for lists/maps/embedded DTOs. Stored enums are strings. */
class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun stringListToJson(value: List<String>): String = json.encodeToString(value)

    @TypeConverter
    fun jsonToStringList(value: String): List<String> =
        runCatching { json.decodeFromString<List<String>>(value) }.getOrDefault(emptyList())

    @TypeConverter
    fun priceMapToJson(value: Map<String, String>): String = json.encodeToString(value)

    @TypeConverter
    fun jsonToPriceMap(value: String): Map<String, String> =
        runCatching { json.decodeFromString<Map<String, String>>(value) }.getOrDefault(emptyMap())

    @TypeConverter
    fun sensitivityToJson(value: List<SensitivityRowDto>): String = json.encodeToString(value)

    @TypeConverter
    fun jsonToSensitivity(value: String): List<SensitivityRowDto> =
        runCatching { json.decodeFromString<List<SensitivityRowDto>>(value) }.getOrDefault(emptyList())
}
