package com.alazhartex.costing.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.ui.common.ScreenScaffold

@Composable
fun SettingsScreen(container: AppContainer, onBack: () -> Unit) {
    val settings by container.settingsRepository.observe()
        .collectAsStateWithLifecycle(initialValue = null)

    ScreenScaffold(title = "Settings", onBack = onBack) {
        Column {
            Text("Locale: ${settings?.locale ?: "ar-EG"}")
            Spacer(Modifier.height(8.dp))
            Text("Currency: ${settings?.currency ?: "EGP"}")
            Spacer(Modifier.height(8.dp))
            Text("Arabic-Indic digits: ${settings?.arabicIndicDigits ?: false}")
        }
    }
}
