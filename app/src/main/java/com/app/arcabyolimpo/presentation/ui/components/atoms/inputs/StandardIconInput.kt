package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.KeyIcon
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme

@Suppress("ktlint:standard:function-naming")
/**
 * StandardInput: composable input field with label, placeholder, and optional error handling.
 *
 * @param label String -> text label displayed above the input field
 * @param onValueChange (String) -> Unit -> callback function triggered when the input value changes
 * @param modifier Modifier = Modifier -> allows layout customization (padding, width, etc.)
 * @param value String = "" -> initial text value displayed in the input field
 * @param placeholder String = "" -> helper text shown when the field is empty
 * @param isError Boolean = false -> marks the input as invalid, changing border and color styles
 * @param errorMessage String? = null -> optional error message shown below the field if isError is true
 * @param visualTransformation VisualTransformation = VisualTransformation.None -> defines how text is visually transformed (e.g., password masking)
 * @param trailingIcon @Composable (() -> Unit)? = null -> optional icon displayed at the end of the input field
 */
@Composable
fun StandardIconInput(
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    value: String = "",
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null, // 🆕 nuevo parámetro
) {
    var textValue by rememberSaveable { mutableStateOf(value) }

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

        OutlinedTextField(
            value = textValue,
            onValueChange = {
                textValue = it
                onValueChange(it)
            },
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp),
            shape = RoundedCornerShape(12.dp),
            isError = isError,
            visualTransformation = visualTransformation,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon, // 🆕 icono inicial opcional
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor =
                        if (isError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                    unfocusedIndicatorColor =
                        if (isError) {
                            MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
        )

        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
fun StandardIconInputPreview() {
    ArcaByOlimpoTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        StandardIconInput(
            label = "Nombre",
            placeholder = "Escribe tu nombre",
            value = "",
            onValueChange = {},
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
fun StandardIconInputErrorPreview() {
    ArcaByOlimpoTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        StandardIconInput(
            label = "Correo electrónico",
            placeholder = "example@email.com",
            value = "Rob",
            isError = true,
            errorMessage = "Correo inválido",
            trailingIcon = { KeyIcon() },
            onValueChange = {},
        )
    }
}
