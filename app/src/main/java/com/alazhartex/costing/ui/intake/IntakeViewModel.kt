package com.alazhartex.costing.ui.intake

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.domain.model.FabricBatch
import com.alazhartex.costing.domain.model.PurchaseUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

data class IntakeUiState(
    val nameEn: String = "",
    val nameAr: String = "",
    val supplierEn: String = "",
    val supplierAr: String = "",
    val unit: PurchaseUnit = PurchaseUnit.METER,
    val quantity: String = "",
    val gsm: String = "",
    val widthCm: String = "",
    val unitCost: String = "",
    val metersPerBolt: String = "",
    val notes: String = "",
    val error: String? = null,
    val saved: Boolean = false,
)

class IntakeViewModel(private val container: AppContainer, private val batchId: String?) : ViewModel() {

    private val _state = MutableStateFlow(IntakeUiState())
    val state: StateFlow<IntakeUiState> = _state.asStateFlow()

    fun update(transform: IntakeUiState.() -> IntakeUiState) = _state.update(transform)

    fun save() {
        val s = _state.value
        val qty = s.quantity.toBigDecimalOrNull()
        val cost = s.unitCost.toBigDecimalOrNull()
        if (s.nameEn.isBlank() && s.nameAr.isBlank()) {
            _state.update { it.copy(error = "Name required (EN or AR)") }
            return
        }
        if (qty == null || qty.signum() <= 0) {
            _state.update { it.copy(error = "Valid quantity required") }
            return
        }
        if (cost == null || cost.signum() < 0) {
            _state.update { it.copy(error = "Valid unit cost required") }
            return
        }
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val batch = FabricBatch(
                id = batchId ?: "b-${now.toString(36)}",
                nameEn = s.nameEn.trim(),
                nameAr = s.nameAr.trim(),
                supplierEn = s.supplierEn.trim(),
                supplierAr = s.supplierAr.trim(),
                purchaseUnit = s.unit,
                quantity = qty,
                gsm = s.gsm.toBigDecimalOrNull(),
                widthCm = s.widthCm.toBigDecimalOrNull(),
                unitCost = cost,
                currency = "EGP",
                metersPerBolt = s.metersPerBolt.toBigDecimalOrNull(),
                notes = s.notes.trim(),
                createdAt = now,
                updatedAt = now,
            )
            container.batchRepository.upsert(batch)
            _state.update { it.copy(saved = true, error = null) }
        }
    }
}
