package com.alazhartex.costing.ui.batches

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.domain.model.FabricBatch
import com.alazhartex.costing.domain.model.PurchaseUnit
import com.alazhartex.costing.ui.components.EmptyState
import com.alazhartex.costing.ui.components.StatusChip
import com.alazhartex.costing.ui.components.Tone
import com.alazhartex.costing.ui.theme.tabular

private enum class BatchFilter { ALL, NEEDS_CONVERSION, KG, BOLT }
private enum class BatchSort { NEWEST, NAME }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchListScreen(
    container: AppContainer,
    onAddBatch: () -> Unit,
    onOpenBatch: (String) -> Unit,
    onBack: () -> Unit,
) {
    val batches by container.batchRepository.observeAll()
        .collectAsStateWithLifecycle(initialValue = emptyList())
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(BatchFilter.ALL) }
    var sort by remember { mutableStateOf(BatchSort.NEWEST) }

    val visible = batches
        .filter { batch ->
            val matchesQuery = query.isBlank() ||
                batch.nameEn.contains(query, true) ||
                batch.nameAr.contains(query) ||
                batch.supplierEn.contains(query, true) ||
                batch.supplierAr.contains(query)
            matchesQuery && when (filter) {
                BatchFilter.ALL -> true
                BatchFilter.NEEDS_CONVERSION ->
                    batch.purchaseUnit == PurchaseUnit.KG && (batch.gsm == null || batch.widthCm == null)
                BatchFilter.KG -> batch.purchaseUnit == PurchaseUnit.KG
                BatchFilter.BOLT -> batch.purchaseUnit == PurchaseUnit.BOLT
            }
        }
        .let { list ->
            when (sort) {
                BatchSort.NEWEST -> list.sortedByDescending { it.updatedAt }
                BatchSort.NAME -> list.sortedBy { it.nameEn.ifBlank { it.nameAr }.lowercase() }
            }
        }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.nav_batches)) }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddBatch,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(stringResource(R.string.action_add_batch)) },
            )
        },
    ) { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                label = { Text(stringResource(R.string.batch_search_hint)) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(filter == BatchFilter.ALL, { filter = BatchFilter.ALL }, { Text(stringResource(R.string.filter_all)) })
                FilterChip(filter == BatchFilter.NEEDS_CONVERSION, { filter = BatchFilter.NEEDS_CONVERSION }, { Text(stringResource(R.string.filter_needs_conversion)) })
                FilterChip(filter == BatchFilter.KG, { filter = BatchFilter.KG }, { Text(stringResource(R.string.filter_kg)) })
                FilterChip(filter == BatchFilter.BOLT, { filter = BatchFilter.BOLT }, { Text(stringResource(R.string.filter_bolt)) })
            }
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(sort == BatchSort.NEWEST, { sort = BatchSort.NEWEST }, { Text(stringResource(R.string.sort_newest)) })
                FilterChip(sort == BatchSort.NAME, { sort = BatchSort.NAME }, { Text(stringResource(R.string.sort_name)) })
            }
            Spacer(Modifier.height(8.dp))

            if (visible.isEmpty()) {
                EmptyState(
                    title = stringResource(R.string.batch_list_empty),
                    body = stringResource(R.string.batch_empty_body),
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items = visible, key = FabricBatch::id, contentType = { "batch" }) { batch ->
                        BatchRow(batch, onClick = { onOpenBatch(batch.id) })
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun BatchRow(batch: FabricBatch, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(batch.nameEn.ifBlank { batch.nameAr }, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                StatusChip(batch.currency, Tone.NEUTRAL)
            }
            Text(
                batch.nameAr,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    batch.supplierEn.ifBlank { batch.supplierAr },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    batch.quantity.toPlainString() + " " + batch.purchaseUnit.name.lowercase(),
                    style = MaterialTheme.typography.labelMedium.tabular,
                )
            }
            if (batch.purchaseUnit == PurchaseUnit.KG && (batch.gsm == null || batch.widthCm == null)) {
                Spacer(Modifier.height(6.dp))
                StatusChip(stringResource(R.string.alert_missing_conversion), Tone.WARNING)
            }
        }
    }
}
