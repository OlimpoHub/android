package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.HighlightInputBlue
import com.app.arcabyolimpo.ui.theme.HighlightRed
import com.app.arcabyolimpo.ui.theme.InputBackgroundBlue
import com.app.arcabyolimpo.ui.theme.InputBackgroundRed
import com.app.arcabyolimpo.ui.theme.SelectInputBlue
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun SelectInput(
    label: String = "Selecciona",
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = { },
                readOnly = true,
                label = null,
                isError = isError,
                placeholder = {
                    Text(
                        text = "Selecciona una opción",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = if (isError) InputBackgroundRed else InputBackgroundBlue,
                    unfocusedContainerColor = if (isError) InputBackgroundRed else InputBackgroundBlue,
                    errorContainerColor = InputBackgroundRed,
                    focusedIndicatorColor = if (isError) HighlightRed else SelectInputBlue,
                    unfocusedIndicatorColor = if (isError) HighlightRed else HighlightInputBlue,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedPlaceholderColor = White.copy(alpha = 0.6f),
                    unfocusedPlaceholderColor = White.copy(alpha = 0.6f),
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option, color = MaterialTheme.colorScheme.onSurface) },
                        onClick = {
                            onOptionSelected(option)
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = ErrorRed,
                style = Typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

// El Preview...
@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
fun PreviewSelectInputWithLabel() {
    val sampleOptions = listOf("Opción A", "Opción B", "Opción C")
    var selected by remember { mutableStateOf(sampleOptions.first()) }

    ArcaByOlimpoTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SelectInput(
                label = "Selecciona una opción",
                selectedOption = selected,
                options = sampleOptions,
                onOptionSelected = { selected = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            )
        }
    }
}