package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    modifier: Modifier = Modifier,
    label: String,
    options: List<Pair<String, String>>,
    selectedValue: String = "",
    onOptionSelected: (String) -> Unit,
    placeholder: String = "Seleccionar",
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var currentSelection by rememberSaveable { mutableStateOf(selectedValue) }

    val displayValue = options.find { it.second == currentSelection }?.first ?: ""

    Column(
        modifier = modifier
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
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .padding(top = 4.dp),
                readOnly = true,
                value = displayValue,
                onValueChange = {},
                placeholder = {
                    Text(
                        text = placeholder,
                        color = PlaceholderGray,
                    )
                },
                shape = RoundedCornerShape(12.dp),
                isError = isError,
                textStyle = TextStyle(color = White),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    disabledTextColor = White,
                    errorTextColor = White,
                    focusedContainerColor = InputBackgroundBlue,
                    unfocusedContainerColor = InputBackgroundBlue,
                    disabledContainerColor = InputBackgroundBlue,
                    errorContainerColor = InputBackgroundRed,
                    cursorColor = SelectInputBlue,
                    errorCursorColor = HighlightRed,
                    focusedIndicatorColor = if (isError) HighlightRed else SelectInputBlue,
                    unfocusedIndicatorColor = if (isError) HighlightRed else HighlightInputBlue,
                    focusedTrailingIconColor = White,
                    unfocusedTrailingIconColor = White,
                    disabledTrailingIconColor = White,
                    errorTrailingIconColor = White,
                    focusedLabelColor = White,
                    unfocusedLabelColor = White,
                    disabledLabelColor = White,
                    errorLabelColor = HighlightRed,
                    focusedPlaceholderColor = PlaceholderGray,
                    unfocusedPlaceholderColor = PlaceholderGray,
                    disabledPlaceholderColor = PlaceholderGray,
                    errorPlaceholderColor = PlaceholderGray
                ),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(InputBackgroundBlue)
            ) {
                options.forEach { (displayName, id) ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = displayName,
                                style = Typography.bodyMedium,
                                color = White
                            )
                        },
                        onClick = {
                            currentSelection = id
                            onOptionSelected(id)
                            expanded = false
                        }
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
