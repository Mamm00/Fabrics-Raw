package com.alazhartex.costing.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.domain.model.FabricBatch
import com.alazhartex.costing.domain.model.PurchaseUnit
import com.alazhartex.costing.ui.components.AlertBanner
import com.alazhartex.costing.ui.components.EmptyState
import com.alazhartex.costing.ui.components.KpiCard
import com.alazhartex.costing.ui.components.SectionHeader
import com.alazhartex.costing.ui.components.StatusChip
import com.alazhartex.costing.ui.components.Tone
import com.alazhartex.costing.ui.theme.tabular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    container: AppContainer,
    onOpenBatches: () -> Unit,
    onOpenBatch: (String) -> Unit,
    onAddBatch: () -> Unit,
) {
    val batches by container.batchRepository.observeAll()
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val missingConversion = batches.filter { it.purchaseUnit == PurchaseUnit.KG && (it.gsm == null || it.widthCm == null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddBatch,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(stringResource(R.string.action_add_batch)) },
            )
        },
    ) { padding ->
        LazyColumn(
            Modifier.padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    KpiCard(
                        label = stringResource(R.string.dashboard_kpi_batches),
                        value = batches.size.toString(),
                        caption = stringResource(R.string.dashboard_kpi_batches_caption),
                        modifier = Modifier.weight(1f),
                    )
                    KpiCard(
                        label = stringResource(R.string.dashboard_kpi_weight_warning),
                        value = missingConversion.size.toString(),
                        caption = stringResource(R.string.dashboard_kpi_weight_caption),
                        tone = if (missingConversion.isEmpty()) Tone.SUCCESS else Tone.WARNING,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            if (missingConversion.isNotEmpty()) {
                item {
                    AlertBanner(
                        tone = Tone.WARNING,
                        text = stringResource(R.string.dashboard_alert_conversion, missingConversion.size),
                    )
                }
            }

            item { SectionHeader(stringResource(R.string.dashboard_recent)) }

            if (batches.isEmpty()) {
                item {
                    EmptyState(
                        title = stringResource(R.string.batch_list_empty),
                        body = stringResource(R.string.dashboard_empty_body),
                    )
                }
            } else {
                items(items = batches.take(5), key = FabricBatch::id, contentType = { "recent" }) { batch ->
                    BatchSummaryCard(batch = batch, onClick = { onOpenBatch(batch.id) })
                }
                item {
                    Text(
                        stringResource(R.string.dashboard_view_all),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 12.dp).clickable(onClick = onOpenBatches),
                    )
                }
            }
            item { Spacer(Modifier.height(72.dp)) }
        }
    }
}

@Composable
private fun BatchSummaryCard(batch: FabricBatch, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    batch.nameEn.ifBlank { batch.nameAr },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                StatusChip(batch.currency, Tone.NEUTRAL)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                batch.supplierEn.ifBlank { batch.supplierAr },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                batch.quantity.toPlainString() + " " + batch.purchaseUnit.name.lowercase() + " · " +
                    batch.unitCost.toPlainString(),
                style = MaterialTheme.typography.bodyMedium.tabular,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
