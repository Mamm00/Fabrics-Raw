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

/** Validation outcomes map to localized strings in the UI layer. */
enum class IntakeError { NAME_REQUIRED, QUANTITY_INVALID, UNIT_COST_INVALID, WEIGHT_CONVERSION_INCOMPLETE }

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
    val error: IntakeError? = null,
    val saved: Boolean = false,
)

class IntakeViewModel(private val container: AppContainer, private val batchId: String?) : ViewModel() {

    private val _state = MutableStateFlow(IntakeUiState())
    val state: StateFlow<IntakeUiState> = _state.asStateFlow()

    init {
        if (batchId != null) {
            viewModelScope.launch {
                val existing = container.batchRepository.getById(batchId) ?: return@launch
                _state.update {
                    it.copy(
                        nameEn = existing.nameEn,
                        nameAr = existing.nameAr,
                        supplierEn = existing.supplierEn,
                        supplierAr = existing.supplierAr,
                        unit = existing.purchaseUnit,
                        quantity = existing.quantity.toPlainString(),
                        gsm = existing.gsm?.toPlainString().orEmpty(),
                        widthCm = existing.widthCm?.toPlainString().orEmpty(),
                        unitCost = existing.unitCost.toPlainString(),
                        metersPerBolt = existing.metersPerBolt?.toPlainString().orEmpty(),
                        notes = existing.notes,
                    )
                }
            }
        }
    }

    fun update(transform: IntakeUiState.() -> IntakeUiState) = _state.update(transform)

    fun setUnit(unit: PurchaseUnit) = _state.update { it.copy(unit = unit) }

    fun save() {
        val s = _state.value
        val qty = s.quantity.toBigDecimalOrNull()
        val cost = s.unitCost.toBigDecimalOrNull()

        if (s.nameEn.isBlank() && s.nameAr.isBlank()) {
            _state.update { it.copy(error = IntakeError.NAME_REQUIRED) }
            return
        }
        if (qty == null || qty.signum() <= 0) {
            _state.update { it.copy(error = IntakeError.QUANTITY_INVALID) }
            return
        }
        if (cost == null || cost.signum() < 0) {
            _state.update { it.copy(error = IntakeError.UNIT_COST_INVALID) }
            return
        }
        if (s.unit == PurchaseUnit.KG && (s.gsm.toBigDecimalOrNull() == null || s.widthCm.toBigDecimalOrNull() == null)) {
            _state.update { it.copy(error = IntakeError.WEIGHT_CONVERSION_INCOMPLETE) }
            return
        }
        if (s.unit == PurchaseUnit.BOLT && (s.metersPerBolt.toBigDecimalOrNull() ?: BigDecimal.ZERO).signum() <= 0) {
            _state.update { it.copy(error = IntakeError.WEIGHT_CONVERSION_INCOMPLETE) }
            return
        }

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val batch = FabricBatch(
                id = batchId ?: "b-" + now.toString(36),
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
