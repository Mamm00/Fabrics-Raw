package com.alazhartex.costing.ui.benchmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alazhartex.costing.client.model.AnalyzeRequestDto
import com.alazhartex.costing.client.remote.BenchmarkClient
import com.alazhartex.costing.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BenchmarkUiState(
    val summary: String? = null,
    val citations: List<String> = emptyList(),
    val offline: Boolean = false,
    val loading: Boolean = false,
)

class BenchmarkViewModel(private val container: AppContainer, private val batchId: String) : ViewModel() {

    private val _state = MutableStateFlow(BenchmarkUiState())
    val state: StateFlow<BenchmarkUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val cached = container.benchmarkRepository.latest(batchId) ?: return@launch
            _state.update {
                it.copy(summary = cached.summaryEn, citations = cached.citations)
            }
        }
    }

    fun analyze() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, offline = false) }
            val batch = container.batchRepository.getById(batchId)
            val request = AnalyzeRequestDto(
                batchId = batchId,
                fabricNameEn = batch?.nameEn.orEmpty(),
                fabricNameAr = batch?.nameAr.orEmpty(),
                purchaseUnit = batch?.purchaseUnit?.name ?: "METER",
                quantity = batch?.quantity?.toPlainString() ?: "0",
                gsm = batch?.gsm?.toPlainString(),
                widthCm = batch?.widthCm?.toPlainString(),
                unitCost = batch?.unitCost?.toPlainString() ?: "0",
            )
            when (val result = container.benchmarkClient.analyze(request)) {
                is BenchmarkClient.Result.Success -> {
                    val response = result.response
                    val urls = response.citations.map { citation -> citation.url }
                    container.benchmarkRepository.cache(batchId, response.summaryEn, response.summaryAr, urls)
                    _state.update {
                        it.copy(
                            loading = false,
                            offline = false,
                            summary = response.summaryEn,
                            citations = urls,
                        )
                    }
                }
                is BenchmarkClient.Result.Failure ->
                    _state.update { it.copy(loading = false, offline = true) }
            }
        }
    }
}
