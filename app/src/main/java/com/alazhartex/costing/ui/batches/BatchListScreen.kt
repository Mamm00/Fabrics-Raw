package com.alazhartex.costing.ui.batches

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.domain.model.FabricBatch
import com.alazhartex.costing.ui.common.ScreenScaffold

@Composable
fun BatchListScreen(
    container: AppContainer,
    onAddBatch: () -> Unit,
    onOpenBatch: (String) -> Unit,
    onBack: () -> Unit,
) {
    val batches by container.batchRepository.observeAll()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    ScreenScaffold(title = stringResource(R.string.nav_batches), onBack = onBack) {
        Button(onClick = onAddBatch) { Text(stringResource(R.string.action_add_batch)) }
        Spacer(Modifier.height(8.dp))
        if (batches.isEmpty()) {
            Text(stringResource(R.string.batch_list_empty))
        }
        LazyColumn {
            items(items = batches, key = FabricBatch::id, contentType = { "batch" }) { batch ->
                Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(batch.nameEn.ifBlank { batch.nameAr })
                        Text(batch.supplierEn.ifBlank { batch.supplierAr })
                        Button(onClick = { onOpenBatch(batch.id) }) {
                            Text(stringResource(R.string.action_open))
                        }
                    }
                }
            }
        }
    }
}
