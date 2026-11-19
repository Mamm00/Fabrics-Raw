package com.alazhartex.costing.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.ui.common.ScreenScaffold

@Composable
fun DashboardScreen(container: AppContainer, onOpenBatches: () -> Unit, onOpenSettings: () -> Unit) {
    val batches by container.batchRepository.observeAll()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    ScreenScaffold(title = stringResource(R.string.app_name)) {
        Text(stringResource(R.string.dashboard_batches_count, batches.size))
        Spacer(Modifier.height(16.dp))
        Column {
            Button(onClick = onOpenBatches) { Text(stringResource(R.string.nav_batches)) }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onOpenSettings) { Text(stringResource(R.string.nav_settings)) }
        }
    }
}
