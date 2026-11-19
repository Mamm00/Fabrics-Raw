package com.alazhartex.costing.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.alazhartex.costing.R

/**
 * Screen frame with a title row and an optional start-aligned back affordance.
 * Navigation icons are auto-mirrored so RTL layouts point the right way.
 */
@Composable
fun ScreenScaffold(title: String, onBack: (() -> Unit)? = null, content: @Composable () -> Unit) {
    val backLabel = stringResource(R.string.action_back)
    Column(Modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(title, style = MaterialTheme.typography.headlineSmall)
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.semantics { contentDescription = backLabel },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        content()
    }
}

@Composable
fun LabeledField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
    )
}

@Composable
fun InfoCard(title: String, lines: List<String>) {
    Card(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            lines.forEach { Text(it, style = MaterialTheme.typography.bodyMedium) }
        }
    }
}

@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium,
    )
}
