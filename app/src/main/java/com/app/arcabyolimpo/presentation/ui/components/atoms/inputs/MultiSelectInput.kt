package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.app.arcabyolimpo.ui.theme.PlaceholderGray
import com.app.arcabyolimpo.ui.theme.SelectInputBlue
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun MultiSelectInput(
    label: String = "Selecciona",
    placeholder: String = "Selecciona opciones",
    selectedOptions: List<String>,
    options: List<String>,
    onOptionsSelected: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
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
            style = Typography.bodyMedium,
            color = White,
        )

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded },
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = when {
                    selectedOptions.isEmpty() -> ""
                    selectedOptions.size == 1 -> selectedOptions.first()
                    else -> "${selectedOptions.size} seleccionados"
                },
                onValueChange = { },
                readOnly = true,
                label = null,
                isError = isError,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = PlaceholderGray,
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                shape = RoundedCornerShape(12.dp),
                colors =
                    TextFieldDefaults.colors(
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
                modifier =
                    Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(top = 4.dp),
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option, color = MaterialTheme.colorScheme.onSurface) },
                        onClick = {
                            val newSelection = if (selectedOptions.contains(option)) {
                                selectedOptions - option
                            } else {
                                selectedOptions + option
                            }
                            onOptionsSelected(newSelection)
                        },
                        leadingIcon = {
                            Checkbox(
                                checked = selectedOptions.contains(option),
                                onCheckedChange = null,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = SelectInputBlue,
                                )
                            )
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        // Chips para mostrar las opciones seleccionadas
        if (selectedOptions.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                selectedOptions.forEach { option ->
                    AssistChip(
                        onClick = {
                            onOptionsSelected(selectedOptions - option)
                        },
                        label = { Text(option) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar $option",
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = SelectInputBlue.copy(alpha = 0.2f),
                            labelColor = White,
                            trailingIconContentColor = White,
                        ),
                        modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
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

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
fun PreviewMultiSelectInput() {
    val sampleOptions = listOf("Opción A", "Opción B", "Opción C", "Opción D", "Opción E")
    var selected by remember { mutableStateOf(listOf("Opción A", "Opción C")) }

    ArcaByOlimpoTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
        ) {
            MultiSelectInput(
                label = "Selecciona múltiples opciones",
                selectedOptions = selected,
                options = sampleOptions,
                onOptionsSelected = { selected = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            )
        }
    }
}