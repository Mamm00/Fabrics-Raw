package com.alazhartex.costing.ui.batchdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.ui.components.KpiCard
import com.alazhartex.costing.ui.components.SectionHeader
import com.alazhartex.costing.ui.navigation.BatchTab

@Composable
fun OverviewPane(container: AppContainer, batchId: String, onSelectTab: (BatchTab) -> Unit) {
    val batchFlow = remember(batchId) { container.batchRepository.observeById(batchId) }
    val batch by batchFlow.collectAsStateWithLifecycle(initialValue = null)

    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KpiCard(
                label = stringResource(R.string.overview_quantity),
                value = batch?.quantity?.toPlainString() ?: "—",
                caption = batch?.purchaseUnit?.name?.lowercase(),
                modifier = Modifier.weight(1f),
            )
            KpiCard(
                label = stringResource(R.string.overview_unit_cost),
                value = (batch?.currency ?: "EGP") + " " + (batch?.unitCost?.toPlainString() ?: "—"),
                caption = stringResource(R.string.overview_per_unit),
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KpiCard(
                label = stringResource(R.string.overview_gsm),
                value = batch?.gsm?.toPlainString() ?: stringResource(R.string.value_not_set),
                caption = "g/m²",
                modifier = Modifier.weight(1f),
            )
            KpiCard(
                label = stringResource(R.string.overview_width),
                value = batch?.widthCm?.toPlainString() ?: stringResource(R.string.value_not_set),
                caption = "cm",
                modifier = Modifier.weight(1f),
            )
        }

        SectionHeader(stringResource(R.string.overview_next_steps))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalButton(onClick = { onSelectTab(BatchTab.SHRINKAGE) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.nav_shrinkage))
            }
            FilledTonalButton(onClick = { onSelectTab(BatchTab.GRADES) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.tab_grades))
            }
            FilledTonalButton(onClick = { onSelectTab(BatchTab.COST) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.nav_cost))
            }
            FilledTonalButton(onClick = { onSelectTab(BatchTab.PRICING) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.nav_pricing))
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            stringResource(R.string.overview_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
