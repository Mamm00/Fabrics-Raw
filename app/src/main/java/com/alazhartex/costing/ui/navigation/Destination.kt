package com.alazhartex.costing.ui.navigation

import kotlinx.serialization.Serializable

/** Top-level tabs plus the batch workspace. Serializable routes for Navigation 3. */
@Serializable
sealed interface Destination {
    data object Dashboard : Destination
    data object Batches : Destination
    data class BatchDetail(val batchId: String) : Destination
    data object Settings : Destination
}

/** Sub-sections of the batch workspace, rendered as tabs inside one screen. */
enum class BatchTab { OVERVIEW, INTAKE, SHRINKAGE, GRADES, COST, PRICING, BENCHMARK }
