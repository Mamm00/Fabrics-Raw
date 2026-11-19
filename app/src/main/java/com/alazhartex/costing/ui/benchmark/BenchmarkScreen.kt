package com.alazhartex.costing.ui.benchmark

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.ui.common.ErrorText
import com.alazhartex.costing.ui.common.InfoCard
import com.alazhartex.costing.ui.common.ScreenScaffold

@Composable
fun BenchmarkScreen(container: AppContainer, batchId: String, onBack: () -> Unit) {
    val vm: BenchmarkViewModel = viewModel { BenchmarkViewModel(container, batchId) }
    val state by vm.state.collectAsStateWithLifecycle()

    ScreenScaffold(title = stringResource(R.string.nav_benchmark), onBack = onBack) {
        Column {
            Button(onClick = vm::analyze) { Text(stringResource(R.string.action_analyze)) }
            Spacer(Modifier.height(12.dp))
            val summary = state.summary
            when {
                summary != null -> InfoCard(
                    title = stringResource(R.string.benchmark_industry),
                    lines = listOf(summary) + state.citations,
                )
                state.offline -> ErrorText(stringResource(R.string.offline_message))
                else -> Text(stringResource(R.string.benchmark_empty))
            }
        }
    }
}
