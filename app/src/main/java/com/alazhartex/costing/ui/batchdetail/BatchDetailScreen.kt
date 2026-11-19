package com.alazhartex.costing.ui.batchdetail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.alazhartex.costing.ui.benchmark.BenchmarkPane
import com.alazhartex.costing.ui.components.StatusChip
import com.alazhartex.costing.ui.components.Tone
import com.alazhartex.costing.ui.cost.CostPane
import com.alazhartex.costing.ui.grade.GradePane
import com.alazhartex.costing.ui.intake.IntakePane
import com.alazhartex.costing.ui.navigation.BatchTab
import com.alazhartex.costing.ui.pricing.PricingPane
import com.alazhartex.costing.ui.shrinkage.ShrinkagePane
import com.alazhartex.costing.ui.theme.tabular

@Composable
fun BatchDetailScreen(container: AppContainer, batchId: String, onBack: () -> Unit) {
    val isNew = batchId.isBlank()
    val batchFlow = remember(batchId) {
        if (isNew) kotlinx.coroutines.flow.flowOf(null)
        else container.batchRepository.observeById(batchId)
    }
    val batch by batchFlow.collectAsStateWithLifecycle(initialValue = null)
    var tab by remember(batchId) { mutableStateOf(if (isNew) BatchTab.INTAKE else BatchTab.OVERVIEW) }

    Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
            }
            Text(
                text = stringResource(if (isNew) R.string.action_add_batch else R.string.batch_detail_title),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Spacer(Modifier.height(8.dp))

        batch?.let { HeaderCard(it) }

        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BatchTab.entries.forEach { candidate ->
                FilterChip(
                    selected = tab == candidate,
                    enabled = !(isNew && candidate != BatchTab.INTAKE),
                    onClick = { tab = candidate },
                    label = { Text(stringResource(candidate.labelRes())) },
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        when (tab) {
            BatchTab.OVERVIEW -> OverviewPane(container, batchId, onSelectTab = { tab = it })
            BatchTab.INTAKE -> IntakePane(container, batchId.ifBlank { null }, onSaved = { tab = BatchTab.SHRINKAGE })
            BatchTab.SHRINKAGE -> ShrinkagePane(container, batchId)
            BatchTab.GRADES -> GradePane(container, batchId, onOpenPricing = { tab = BatchTab.PRICING })
            BatchTab.COST -> CostPane(container, batchId)
            BatchTab.PRICING -> PricingPane(container, batchId, onBenchmark = { tab = BatchTab.BENCHMARK })
            BatchTab.BENCHMARK -> BenchmarkPane(container, batchId)
        }
    }
}

private fun BatchTab.labelRes(): Int = when (this) {
    BatchTab.OVERVIEW -> R.string.tab_overview
    BatchTab.INTAKE -> R.string.tab_intake
    BatchTab.SHRINKAGE -> R.string.tab_shrinkage
    BatchTab.GRADES -> R.string.tab_grades
    BatchTab.COST -> R.string.tab_cost
    BatchTab.PRICING -> R.string.tab_pricing
    BatchTab.BENCHMARK -> R.string.tab_benchmark
}

@Composable
private fun HeaderCard(batch: FabricBatch) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                batch.nameEn.ifBlank { batch.nameAr },
                style = MaterialTheme.typography.titleLarge,
            )
            if (batch.nameAr.isNotBlank() && batch.nameEn.isNotBlank()) {
                Text(batch.nameAr, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                batch.supplierEn.ifBlank { batch.supplierAr },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    batch.quantity.toPlainString() + " " + batch.purchaseUnit.name.lowercase(),
                    style = MaterialTheme.typography.bodyMedium.tabular,
                )
                StatusChip(batch.currency, Tone.NEUTRAL)
                if (batch.purchaseUnit.name == "KG" && (batch.gsm == null || batch.widthCm == null)) {
                    StatusChip(stringResource(R.string.alert_missing_conversion), Tone.WARNING)
                }
            }
        }
    }
}
