package com.alazhartex.costing.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
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
        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
        entryProvider = entryProvider {
            entry<Destination.Dashboard> {
                DashboardScreen(
                    container = container,
                    onOpenBatches = { backStack.add(Destination.Batches) },
                    onOpenSettings = { backStack.add(Destination.Settings) },
                )
            }
            entry<Destination.Batches> {
                BatchListScreen(
                    container = container,
                    onAddBatch = { backStack.add(Destination.Intake(null)) },
                    onOpenBatch = { id -> backStack.add(Destination.Shrinkage(id)) },
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                )
            }
            entry<Destination.Intake> { entry ->
                IntakeScreen(
                    container = container,
                    batchId = entry.batchId,
                    onSaved = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                )
            }
            entry<Destination.Shrinkage> { entry ->
                ShrinkageScreen(
                    container = container,
                    batchId = entry.batchId,
                    onNext = { backStack.add(Destination.Grade(entry.batchId)) },
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                )
            }
            entry<Destination.Grade> { entry ->
                GradeScreen(
                    container = container,
                    batchId = entry.batchId,
                    onNext = { backStack.add(Destination.Cost(entry.batchId)) },
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                )
            }
            entry<Destination.Cost> { entry ->
                CostScreen(
                    container = container,
                    batchId = entry.batchId,
                    onNext = { backStack.add(Destination.Pricing(entry.batchId)) },
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                )
            }
            entry<Destination.Pricing> { entry ->
                PricingScreen(
                    container = container,
                    batchId = entry.batchId,
                    onBenchmark = { backStack.add(Destination.Benchmark(entry.batchId)) },
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                )
            }
            entry<Destination.Benchmark> { entry ->
                BenchmarkScreen(
                    container = container,
                    batchId = entry.batchId,
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                )
            }
            entry<Destination.Settings> {
                SettingsScreen(
                    container = container,
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                )
            }
        },
    )
}
