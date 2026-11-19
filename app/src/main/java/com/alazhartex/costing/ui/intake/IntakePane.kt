package com.alazhartex.costing.ui.intake

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.domain.model.PurchaseUnit
import com.alazhartex.costing.ui.common.ErrorText
import com.alazhartex.costing.ui.common.LabeledField

@Composable
fun IntakePane(container: AppContainer, batchId: String?, onSaved: () -> Unit) {
    val vm: IntakeViewModel = viewModel { IntakeViewModel(container, batchId) }
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) {
        if (state.saved) onSaved()
    }

    Column(Modifier.verticalScroll(rememberScrollState())) {
        PurchaseUnit.entries.forEach { unit ->
            FilterChip(
                selected = state.unit == unit,
                onClick = { vm.setUnit(unit) },
                label = { Text(unit.name) },
            )
        }
        Spacer(Modifier.height(8.dp))
        LabeledField(stringResource(R.string.intake_name_en), state.nameEn) { v ->
            vm.update { copy(nameEn = v) }
        }
        Spacer(Modifier.height(8.dp))
        LabeledField(stringResource(R.string.intake_name_ar), state.nameAr) { v ->
            vm.update { copy(nameAr = v) }
        }
        Spacer(Modifier.height(8.dp))
        LabeledField(stringResource(R.string.intake_supplier_en), state.supplierEn) { v ->
            vm.update { copy(supplierEn = v) }
        }
        Spacer(Modifier.height(8.dp))
        LabeledField(stringResource(R.string.intake_supplier_ar), state.supplierAr) { v ->
            vm.update { copy(supplierAr = v) }
        }
        Spacer(Modifier.height(8.dp))
        LabeledField(stringResource(R.string.intake_quantity), state.quantity) { v ->
            vm.update { copy(quantity = v) }
        }
        Spacer(Modifier.height(8.dp))
        LabeledField(stringResource(R.string.intake_gsm), state.gsm) { v ->
            vm.update { copy(gsm = v) }
        }
        Spacer(Modifier.height(8.dp))
        LabeledField(stringResource(R.string.intake_width_cm), state.widthCm) { v ->
            vm.update { copy(widthCm = v) }
        }
        Spacer(Modifier.height(8.dp))
        LabeledField(stringResource(R.string.intake_unit_cost), state.unitCost) { v ->
            vm.update { copy(unitCost = v) }
        }
        Spacer(Modifier.height(8.dp))
        LabeledField(stringResource(R.string.intake_meters_per_bolt), state.metersPerBolt) { v ->
            vm.update { copy(metersPerBolt = v) }
        }
        Spacer(Modifier.height(8.dp))
        LabeledField(stringResource(R.string.intake_notes), state.notes) { v ->
            vm.update { copy(notes = v) }
        }
        Spacer(Modifier.height(12.dp))

        Button(onClick = vm::save) { Text(stringResource(R.string.action_save_batch)) }

        if (state.saved) {
            Spacer(Modifier.height(12.dp))
            Text(stringResource(R.string.grade_empty_hint))
        }
    }
}
