package com.alazhartex.costing.data.local

import androidx.room.TypeConverter
import com.alazhartex.costing.data.local.entity.SensitivityRowDto
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

/** Room type converters for lists/maps/embedded DTOs. Stored enums are strings. */
class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    private val stringListSerializer = ListSerializer(String.serializer())
    private val stringMapSerializer =
        MapSerializer(String.serializer(), String.serializer())

    @TypeConverter
    fun stringListToJson(value: List<String>): String =
        json.encodeToString(stringListSerializer, value)

    @TypeConverter
    fun jsonToStringList(value: String): List<String> =
        runCatching { json.decodeFromString(stringListSerializer, value) }.getOrDefault(emptyList())

    @TypeConverter
    fun priceMapToJson(value: Map<String, String>): String =
        json.encodeToString(stringMapSerializer, value)

    @TypeConverter
    fun jsonToPriceMap(value: String): Map<String, String> =
        runCatching { json.decodeFromString(stringMapSerializer, value) }.getOrDefault(emptyMap())

    @TypeConverter
    fun sensitivityToJson(value: List<SensitivityRowDto>): String =
        json.encodeToString(ListSerializer(SensitivityRowDto.serializer()), value)

    @TypeConverter
    fun jsonToSensitivity(value: String): List<SensitivityRowDto> =
        runCatching {
            json.decodeFromString(ListSerializer(SensitivityRowDto.serializer()), value)
        }.getOrDefault(emptyList())
}
