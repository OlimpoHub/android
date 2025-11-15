package com.app.arcabyolimpo.presentation.ui.components.molecules

import android.content.res.Configuration
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.HighlightInputBlue
import com.app.arcabyolimpo.ui.theme.HighlightRed
import com.app.arcabyolimpo.ui.theme.InputBackgroundBlue
import com.app.arcabyolimpo.ui.theme.InputBackgroundRed
import com.app.arcabyolimpo.ui.theme.PlaceholderGray
import com.app.arcabyolimpo.ui.theme.PrimaryBlue
import com.app.arcabyolimpo.ui.theme.SelectInputBlue
import com.app.arcabyolimpo.ui.theme.White

/** ---------------------------------------------------------------------------------------------- *
 * Molecule Select Object Input: Like the SelectInput, however, this is done to receive a list with
 * many attributes i.e. a workshopList with id and name, then shows the name to the user but saves
 * the id value for creating a new object i.e. a new Supply
 *
 * @param modifier: Modifier = Modifier -> modifier to apply to the composable
 * @param label: String = "Selecciona..." -> label to show above the input field
 * @param options: List<T> -> list of options to choose from
 * @param selectedId: String? -> id of the selected option
 * @param onOptionSelected: (String) -> Unit -> function to execute when an option is selected
 * @param getItemName: (T) -> String -> function to get the name of an option
 * @param getItemId: (T) -> String -> function to get the id of an option
 * @param isError: Boolean = false -> whether the input is in an error state
 * @param errorMessage: String? = null -> error message to show if the input is in an error state
 * ---------------------------------------------------------------------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectObjectInput(
    modifier: Modifier = Modifier,
    label: String = "Selecciona...",
    options: List<T>,
    selectedId: String?,
    onOptionSelected: (String) -> Unit,
    getItemName: (T) -> String,
    getItemId: (T) -> String,
    isError: Boolean = false,
    errorMessage: String? = null,
    ) {
    var isOpen by remember { mutableStateOf(false)}

    val selectedOption = options.find { getItemId(it) == selectedId }
        ?.let { getItemName(it) } ?: ""

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = label,
            style = Typography.bodyMedium,
            fontFamily = Poppins,
            color = White
        )

        ExposedDropdownMenuBox(
            expanded = isOpen,
            onExpandedChange = { isOpen = !isOpen },
            modifier = modifier.fillMaxWidth(),
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
                        color = PlaceholderGray,
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isOpen) },
                shape = RoundedCornerShape(12.dp),
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = if (isError) InputBackgroundRed else InputBackgroundBlue,
                        unfocusedContainerColor = if (isError) InputBackgroundRed else InputBackgroundBlue,
                        errorContainerColor = InputBackgroundRed,
                        focusedIndicatorColor = if (isError) HighlightRed else SelectInputBlue,
                        unfocusedIndicatorColor = if (isError) HighlightRed else HighlightInputBlue,
                        cursorColor = PrimaryBlue, // Usando tu color
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedPlaceholderColor = White.copy(alpha = 0.6f),
                        unfocusedPlaceholderColor = White.copy(alpha = 0.6f),
                    ),
                modifier =
                    Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(top = 4.dp)

            )

            ExposedDropdownMenu(
                expanded = isOpen,
                onDismissRequest = { isOpen = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = getItemName(option),
                                color = White,
                                fontFamily = Poppins
                            )
                        },
                        onClick = {
                            onOptionSelected(getItemId(option))
                            false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = ErrorRed,
                style = Typography.bodySmall,
                fontFamily = Poppins,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF040610,
)
@Composable
fun SelectObjectInputPreview() {
    MaterialTheme {
        SelectObjectInput(
            label = "Selecciona...",
            options = listOf("Opción 1", "Opción 2", "Opción 3"),
            selectedId = "1",
            onOptionSelected = { },
            getItemName = { it },
            getItemId = { it },
            isError = false,
            errorMessage = null
        )
    }
}

