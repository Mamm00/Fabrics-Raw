package com.alazhartex.costing.ui.batches

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.domain.model.FabricBatch
import com.alazhartex.costing.ui.common.ScreenScaffold

@Composable
fun BatchListScreen(container: AppContainer, onAddBatch: () -> Unit, onOpenBatch: (String) -> Unit) {
    val batches by container.batchRepository.observeAll().collectAsStateWithLifecycle(initialValue = emptyList())

    ScreenScaffold(title = "Batches") {
        Button(onClick = onAddBatch) { Text("Add batch") }
        LazyColumn {
            items(items = batches, key = FabricBatch::id, contentType = { "batch" }) { batch ->
                Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(batch.nameEn.ifBlank { batch.nameAr })
                        Text(batch.supplierEn.ifBlank { batch.supplierAr })
                        Button(onClick = { onOpenBatch(batch.id) }) { Text("Open") }
                    }
                }
            }
        }
    }
}
