package com.alazhartex.costing.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alazhartex.costing.R
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.ui.common.ScreenScaffold
import kotlinx.coroutines.launch

private const val LOCALE_EN = "en"
private const val LOCALE_AR = "ar-EG"

@Composable
fun SettingsScreen(container: AppContainer, onBack: () -> Unit) {
    val settings by container.settingsRepository.observe()
        .collectAsStateWithLifecycle(initialValue = null)
    val scope = rememberCoroutineScope()

    ScreenScaffold(title = stringResource(R.string.nav_settings), onBack = onBack) {
        Column {
            Text(stringResource(R.string.settings_locale))
            Row {
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
            Text(stringResource(R.string.settings_currency) + ": " + (settings?.currency ?: "EGP"))
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
        }
    }
}
