package com.alazhartex.costing.ui.benchmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alazhartex.costing.client.model.AnalyzeRequestDto
import com.alazhartex.costing.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BenchmarkUiState(
    val summary: String? = null,
    val offlineMessage: String? = null,
    val loading: Boolean = false,
)

class BenchmarkViewModel(private val container: AppContainer, private val batchId: String) : ViewModel() {

    private val _state = MutableStateFlow(BenchmarkUiState())
    val state: StateFlow<BenchmarkUiState> = _state.asStateFlow()

    fun analyze() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, offlineMessage = null) }
            val request = AnalyzeRequestDto(
                batchId = batchId,
                fabricNameEn = "",
                fabricNameAr = "",
                purchaseUnit = "METER",
                quantity = "0",
                unitCost = "0",
            )
            when (val result = container.benchmarkClient.analyze(request)) {
                is com.alazhartex.costing.client.remote.BenchmarkClient.Result.Success ->
                    _state.update { it.copy(loading = false, summary = result.response.summaryEn) }
                is com.alazhartex.costing.client.remote.BenchmarkClient.Result.Failure ->
                    _state.update { it.copy(loading = false, offlineMessage = "Internet required for benchmark insights") }
            }
        }
    }
}
