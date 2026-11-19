package com.alazhartex.costing.ui.navigation

/** Navigation destinations. Serializable routes for Navigation 3. */
sealed interface Destination {
    data object Dashboard : Destination
    data object Batches : Destination
    data class Intake(val batchId: String?) : Destination
    data class Shrinkage(val batchId: String) : Destination
    data class Grade(val batchId: String) : Destination
    data class Cost(val batchId: String) : Destination
    data class Pricing(val batchId: String) : Destination
    data class Benchmark(val batchId: String) : Destination
    data object Settings : Destination
}
