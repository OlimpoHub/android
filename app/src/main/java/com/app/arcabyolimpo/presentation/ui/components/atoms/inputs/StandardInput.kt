package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.DangerGray
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.HighlightInputBlue
import com.app.arcabyolimpo.ui.theme.HighlightRed
import com.app.arcabyolimpo.ui.theme.InputBackgroundBlue
import com.app.arcabyolimpo.ui.theme.InputBackgroundRed
import com.app.arcabyolimpo.ui.theme.PlaceholderGray
import com.app.arcabyolimpo.ui.theme.SelectInputBlue
import com.app.arcabyolimpo.ui.theme.White
import com.app.arcabyolimpo.ui.theme.PrimaryBlue

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
@Suppress("ktlint:standard:function-naming")
@Composable
fun StandardInput(
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    value: String = "",
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    compact: Boolean = false,
) {

    var textValue by rememberSaveable { mutableStateOf(value) }

    Column(
        modifier
            .fillMaxWidth()
            .padding(vertical = if (compact) 2.dp else 4.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = label,
            style = if (compact) Typography.bodySmall else Typography.bodyMedium,
            color = White,
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
                    color = PlaceholderGray,
                )
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = if (compact) 2.dp else 4.dp),
            shape = RoundedCornerShape(if (compact) 8.dp else 12.dp),
            isError = isError,
            visualTransformation = visualTransformation,
            // Merge the current LocalTextStyle so callers can override typography (e.g. bodyMedium)
            // while we still enforce the input text color to White.
            textStyle = LocalTextStyle.current.merge(TextStyle(color = White)),
            trailingIcon = trailingIcon,
            singleLine = true,
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = InputBackgroundBlue,
                    unfocusedContainerColor = InputBackgroundBlue,
                    errorContainerColor = InputBackgroundRed,
                    focusedIndicatorColor =
                        if (isError) {
                            HighlightRed
                        } else {
                            SelectInputBlue
                        },
                    unfocusedIndicatorColor =
                        if (isError) {
                            HighlightRed
                        } else {
                            HighlightInputBlue
                        },
                    cursorColor = PrimaryBlue,
                ),
        )

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
@Preview(showBackground = true)
@Composable
fun StandardInputPreview() {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        StandardInput(
            label = "Nombre",
            placeholder = "Escribe tu nombre",
            value = "",
            onValueChange = {},
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun StandardInputErrorPreview() {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        StandardInput(
            label = "Correo electrónico",
            placeholder = "example@email.com",
            value = "Rob",
            isError = true,
            errorMessage = "Correo inválido",
            onValueChange = {},
        )
    }
}