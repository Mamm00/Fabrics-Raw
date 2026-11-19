package com.alazhartex.costing.client.model

import kotlinx.serialization.Serializable

@Serializable
data class AnalyzeRequestDto(
    val batchId: String,
    val fabricNameEn: String,
    val fabricNameAr: String,
    val purchaseUnit: String,
    val quantity: String,
    val gsm: String? = null,
    val widthCm: String? = null,
    val unitCost: String,
    val pctFirst: String? = null,
    val pctSecond: String? = null,
    val pctReject: String? = null,
    val totalCost: String? = null,
    val costPerMeter: String? = null,
)

@Serializable
data class CitationDto(val title: String, val url: String)

@Serializable
data class AnalyzeResponseDto(
    val batchId: String,
    val summaryEn: String,
    val summaryAr: String,
    val citations: List<CitationDto>,
    val benchmarkNotes: List<String>,
    val stub: Boolean = false,
)
