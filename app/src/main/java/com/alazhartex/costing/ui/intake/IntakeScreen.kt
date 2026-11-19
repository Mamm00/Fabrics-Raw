package com.alazhartex.costing.ui.intake

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.domain.model.PurchaseUnit
import com.alazhartex.costing.ui.common.LabeledField
import com.alazhartex.costing.ui.common.ScreenScaffold

@Composable
fun IntakeScreen(container: AppContainer, batchId: String?, onSaved: () -> Unit, onBack: () -> Unit) {
    val vm: IntakeViewModel = viewModel { IntakeViewModel(container, batchId) }
    val state by vm.state.collectAsStateWithLifecycle()

    ScreenScaffold(title = "Batch Intake", onBack = onBack) {
        Column {
            LabeledField("Name (EN)", state.nameEn) { v -> vm.update { copy(nameEn = v) } }
            Spacer(Modifier.height(8.dp))
            LabeledField("Name (AR)", state.nameAr) { v -> vm.update { copy(nameAr = v) } }
            Spacer(Modifier.height(8.dp))
            LabeledField("Supplier (EN)", state.supplierEn) { v -> vm.update { copy(supplierEn = v) } }
            Spacer(Modifier.height(8.dp))
            LabeledField("Supplier (AR)", state.supplierAr) { v -> vm.update { copy(supplierAr = v) } }
            Spacer(Modifier.height(8.dp))
            LabeledField("Quantity", state.quantity) { v -> vm.update { copy(quantity = v) } }
            Spacer(Modifier.height(8.dp))
            LabeledField("GSM", state.gsm) { v -> vm.update { copy(gsm = v) } }
            Spacer(Modifier.height(8.dp))
            LabeledField("Width (cm)", state.widthCm) { v -> vm.update { copy(widthCm = v) } }
            Spacer(Modifier.height(8.dp))
            LabeledField("Unit cost", state.unitCost) { v -> vm.update { copy(unitCost = v) } }
            Spacer(Modifier.height(8.dp))
            LabeledField("Meters / bolt", state.metersPerBolt) { v -> vm.update { copy(metersPerBolt = v) } }
            Spacer(Modifier.height(8.dp))
            LabeledField("Notes", state.notes) { v -> vm.update { copy(notes = v) } }

            state.error?.let { Text(it, color = androidx.compose.material3.MaterialTheme.colorScheme.error) }
            Spacer(Modifier.height(12.dp))
            Button(onClick = { vm.save() }) { Text("Save batch") }
            if (state.saved) onSaved()
        }
    }
}
