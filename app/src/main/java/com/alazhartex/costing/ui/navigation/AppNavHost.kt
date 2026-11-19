package com.alazhartex.costing.ui.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.ui.batchdetail.BatchDetailScreen
import com.alazhartex.costing.ui.batches.BatchListScreen
import com.alazhartex.costing.ui.dashboard.DashboardScreen
import com.alazhartex.costing.ui.settings.SettingsScreen

/**
 * Adaptive Navigation 3 host: bottom bar on compact widths, navigation rail on
 * expanded widths, with the batch workspace pushed on top of the back stack.
 */
@Composable
fun AppNavHost(container: AppContainer, modifier: Modifier = Modifier) {
    val backStack = remember { mutableStateListOf<Destination>(Destination.Dashboard) }
    val current = backStack.lastOrNull()

    fun open(destination: Destination) = backStack.add(destination)
    fun back() { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }

    BoxWithConstraints(modifier) {
        val expanded = maxWidth >= 720.dp
        val showTopBar = current is Destination.Dashboard ||
            current is Destination.Batches ||
            current is Destination.Settings

        Scaffold(
            bottomBar = {
                if (!expanded && showTopBar) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = current is Destination.Dashboard,
                            onClick = { backStack.clear(); backStack.add(Destination.Dashboard) },
                            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                            label = { Text(stringResource(R.string.nav_dashboard)) },
                        )
                        NavigationBarItem(
                            selected = current is Destination.Batches,
                            onClick = { backStack.clear(); backStack.add(Destination.Batches) },
                            icon = { Icon(Icons.Filled.List, contentDescription = null) },
                            label = { Text(stringResource(R.string.nav_batches)) },
                        )
                        NavigationBarItem(
                            selected = current is Destination.Settings,
                            onClick = { backStack.clear(); backStack.add(Destination.Settings) },
                            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                            label = { Text(stringResource(R.string.nav_settings)) },
                        )
                    }
                }
            },
        ) { innerPadding ->
            androidx.compose.foundation.layout.Row(Modifier.padding(innerPadding)) {
                if (expanded && showTopBar) {
                    NavigationRail(Modifier.width(88.dp)) {
                        NavigationRailItem(
                            selected = current is Destination.Dashboard,
                            onClick = { backStack.clear(); backStack.add(Destination.Dashboard) },
                            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                            label = { Text(stringResource(R.string.nav_dashboard)) },
                        )
                        NavigationRailItem(
                            selected = current is Destination.Batches,
                            onClick = { backStack.clear(); backStack.add(Destination.Batches) },
                            icon = { Icon(Icons.Filled.List, contentDescription = null) },
                            label = { Text(stringResource(R.string.nav_batches)) },
                        )
                        NavigationRailItem(
                            selected = current is Destination.Settings,
                            onClick = { backStack.clear(); backStack.add(Destination.Settings) },
                            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                            label = { Text(stringResource(R.string.nav_settings)) },
                        )
                    }
                }
                NavDisplay(
                    backStack = backStack,
                    modifier = Modifier.weight(1f),
                    onBack = { back() },
                    entryProvider = entryProvider {
                        entry<Destination.Dashboard> {
                            DashboardScreen(
                                container = container,
                                onOpenBatches = { open(Destination.Batches) },
                                onOpenBatch = { id -> open(Destination.BatchDetail(id)) },
                                onAddBatch = { open(Destination.BatchDetail("")) },
                            )
                        }
                        entry<Destination.Batches> {
                            BatchListScreen(
                                container = container,
                                onAddBatch = { open(Destination.BatchDetail("")) },
                                onOpenBatch = { id -> open(Destination.BatchDetail(id)) },
                                onBack = { back() },
                            )
                        }
                        entry<Destination.BatchDetail> { entry ->
                            BatchDetailScreen(
                                container = container,
                                batchId = entry.batchId,
                                onBack = { back() },
                            )
                        }
                        entry<Destination.Settings> {
                            SettingsScreen(container = container, onBack = { back() })
                        }
                    },
                )
            }
        }
    }
}
