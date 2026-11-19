package com.alazhartex.costing.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.NavDisplay
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.ui.benchmark.BenchmarkScreen
import com.alazhartex.costing.ui.batches.BatchListScreen
import com.alazhartex.costing.ui.cost.CostScreen
import com.alazhartex.costing.ui.dashboard.DashboardScreen
import com.alazhartex.costing.ui.grade.GradeScreen
import com.alazhartex.costing.ui.intake.IntakeScreen
import com.alazhartex.costing.ui.pricing.PricingScreen
import com.alazhartex.costing.ui.settings.SettingsScreen
import com.alazhartex.costing.ui.shrinkage.ShrinkageScreen

/**
 * Navigation 3 host. The back stack holds [Destination]s; the entry provider maps
 * each destination to its screen.
 */
@Composable
fun AppNavHost(container: AppContainer, modifier: Modifier = Modifier) {
    val backStack = remember { mutableStateListOf<Destination>(Destination.Dashboard) }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {
            entry<Destination.Dashboard> {
                DashboardScreen(
                    container = container,
                    onOpenBatches = { backStack.add( Destination.Batches) },
                    onOpenSettings = { backStack.add( Destination.Settings) },
                )
            }
            entry<Destination.Batches> {
                BatchListScreen(
                    container = container,
                    onAddBatch = { backStack.add( Destination.Intake(null)) },
                    onOpenBatch = { id -> backStack.add( Destination.Shrinkage(id)) },
                )
            }
            entry<Destination.Intake> { entry ->
                IntakeScreen(
                    container = container,
                    batchId = entry.batchId,
                    onSaved = { backStack.removeLastOrNull() },
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<Destination.Shrinkage> { entry ->
                ShrinkageScreen(
                    container = container,
                    batchId = entry.batchId,
                    onNext = { backStack.add( Destination.Grade(entry.batchId)) },
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<Destination.Grade> { entry ->
                GradeScreen(
                    container = container,
                    batchId = entry.batchId,
                    onNext = { backStack.add( Destination.Cost(entry.batchId)) },
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<Destination.Cost> { entry ->
                CostScreen(
                    container = container,
                    batchId = entry.batchId,
                    onNext = { backStack.add( Destination.Pricing(entry.batchId)) },
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<Destination.Pricing> { entry ->
                PricingScreen(
                    container = container,
                    batchId = entry.batchId,
                    onBenchmark = { backStack.add( Destination.Benchmark(entry.batchId)) },
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<Destination.Benchmark> { entry ->
                BenchmarkScreen(
                    container = container,
                    batchId = entry.batchId,
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<Destination.Settings> {
                SettingsScreen(
                    container = container,
                    onBack = { backStack.removeLastOrNull() },
                )
            }
        },
    )
}
