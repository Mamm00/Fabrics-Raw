package com.alazhartex.costing.backend.model

import kotlinx.serialization.Serializable

/** Batch context the Android client sends for benchmark analysis. */
@Serializable
data class AnalyzeRequest(
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
data class Citation(val title: String, val url: String)

@Serializable
data class AnalyzeResponse(
    val batchId: String,
    val summaryEn: String,
    val summaryAr: String,
    val citations: List<Citation>,
    val benchmarkNotes: List<String>,
    val stub: Boolean = false,
)
