package com.alazhartex.costing.ui.benchmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.ui.common.InfoCard
import com.alazhartex.costing.ui.common.ScreenScaffold

@Composable
fun BenchmarkScreen(container: AppContainer, batchId: String, onBack: () -> Unit) {
    val vm: BenchmarkViewModel = viewModel { BenchmarkViewModel(container, batchId) }
    val state by vm.state.collectAsStateWithLifecycle()

    ScreenScaffold(title = "Benchmark Insight", onBack = onBack) {
        Column {
            Button(onClick = { vm.analyze() }) { Text("Analyze this operation") }
            Spacer(Modifier.height(12.dp))
            state.summary?.let { InfoCard("Industry benchmark", listOf(it)) }
            state.offlineMessage?.let { Text(it, color = androidx.compose.material3.MaterialTheme.colorScheme.error) }
        }
    }
}
