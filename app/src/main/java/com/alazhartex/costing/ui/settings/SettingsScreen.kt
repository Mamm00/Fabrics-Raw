package com.alazhartex.costing.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.ui.components.AlertBanner
import com.alazhartex.costing.ui.components.SectionHeader
import com.alazhartex.costing.ui.components.Tone
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal

private const val LOCALE_EN = "en"
private const val LOCALE_AR = "ar-EG"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(container: AppContainer, onBack: () -> Unit) {
    val settings by container.settingsRepository.observe()
        .collectAsStateWithLifecycle(initialValue = null)
    val scope = rememberCoroutineScope()

    Scaffold(topBar = { TopAppBar(title = { Text(stringResource(R.string.nav_settings)) }) }) { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 16.dp).verticalScroll(rememberScrollState())) {
            SectionHeader(stringResource(R.string.settings_locale))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = settings?.locale == LOCALE_AR,
                    onClick = {
                        scope.launch {
                            container.settingsRepository.setLocale(LOCALE_AR)
                            container.applyLocale(LOCALE_AR)
                        }
                    },
                    label = { Text(stringResource(R.string.settings_language_ar)) },
                )
                FilterChip(
                    selected = settings?.locale == LOCALE_EN,
                    onClick = {
                        scope.launch {
                            container.settingsRepository.setLocale(LOCALE_EN)
                            container.applyLocale(LOCALE_EN)
                        }
                    },
                    label = { Text(stringResource(R.string.settings_language_en)) },
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = settings?.arabicIndicDigits ?: false,
                    onCheckedChange = { enabled ->
                        scope.launch { container.settingsRepository.setArabicIndicDigits(enabled) }
                    },
                )
                Text(stringResource(R.string.settings_arabic_digits))
            }
            Text(
                stringResource(R.string.settings_currency) + ": " + (settings?.currency ?: "EGP"),
                style = MaterialTheme.typography.bodyMedium,
            )

            SectionHeader(stringResource(R.string.settings_rules))
            Text(
                stringResource(R.string.settings_rules_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            RulesEditor(
                container,
                settings?.rules?.let {
                    listOf(
                        it.shrinkageWarpWarnPct, it.secondGradeCompensationThresholdPct,
                        it.defaultWastagePct, it.defaultVatPct, it.dyeingFinishingMinPct, it.dyeingFinishingMaxPct,
                    )
                } ?: emptyList(),
                scope,
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RulesEditor(container: AppContainer, values: List<BigDecimal>, scope: kotlinx.coroutines.CoroutineScope) {
    var shrinkage by remember(values) { mutableStateOf(values.getOrNull(0)?.toPlainString().orEmpty()) }
    var secondGrade by remember(values) { mutableStateOf(values.getOrNull(1)?.toPlainString().orEmpty()) }
    var wastage by remember(values) { mutableStateOf(values.getOrNull(2)?.toPlainString().orEmpty()) }
    var vat by remember(values) { mutableStateOf(values.getOrNull(3)?.toPlainString().orEmpty()) }
    var dyeMin by remember(values) { mutableStateOf(values.getOrNull(4)?.toPlainString().orEmpty()) }
    var dyeMax by remember(values) { mutableStateOf(values.getOrNull(5)?.toPlainString().orEmpty()) }
    var saved by remember { mutableStateOf(false) }

    val fields = listOf(
        stringResource(R.string.settings_shrinkage_warn) to shrinkage,
        stringResource(R.string.settings_second_grade) to secondGrade,
        stringResource(R.string.settings_wastage) to wastage,
        stringResource(R.string.settings_vat) to vat,
        stringResource(R.string.settings_dyeing_min) to dyeMin,
        stringResource(R.string.settings_dyeing_max) to dyeMax,
    )
    fields.forEachIndexed { index, (label, value) ->
        OutlinedTextField(
            value = value,
            onValueChange = {
                when (index) {
                    0 -> shrinkage = it
                    1 -> secondGrade = it
                    2 -> wastage = it
                    3 -> vat = it
                    4 -> dyeMin = it
                    else -> dyeMax = it
                }
                saved = false
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        )
    }
    Button(onClick = {
        val current = kotlinx.coroutines.runBlocking { container.settingsRepository.observe().first() }
        val base = current.rules
        val parsed = listOf(shrinkage, secondGrade, wastage, vat, dyeMin, dyeMax).mapNotNull { it.toBigDecimalOrNull() }
        if (parsed.size == 6) {
            scope.launch {
                container.settingsRepository.update(
                    locale = current.locale,
                    arabicIndicDigits = current.arabicIndicDigits,
                    currency = current.currency,
                    rules = base.copy(
                        shrinkageWarpWarnPct = parsed[0],
                        secondGradeCompensationThresholdPct = parsed[1],
                        defaultWastagePct = parsed[2],
                        defaultVatPct = parsed[3],
                        dyeingFinishingMinPct = parsed[4],
                        dyeingFinishingMaxPct = parsed[5],
                    ),
                )
                saved = true
            }
        }
    }, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.settings_save_rules))
    }
    if (saved) {
        Spacer(Modifier.height(8.dp))
        AlertBanner(Tone.SUCCESS, stringResource(R.string.settings_rules_hint))
    }
}
