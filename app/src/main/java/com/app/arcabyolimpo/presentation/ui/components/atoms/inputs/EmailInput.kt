package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.MailIcon

/**
 * EmailInput: composable text field specialized for email entry, with validation and visual feedback.
 *
 * @param label String = "Correo electrónico" -> text label displayed above the input field
 * @param placeholder String = "ejemplo@correo.com" -> helper text shown when the field is empty
 * @param value String = "" -> current email text value
 * @param onValueChange (String) -> Unit -> callback triggered when the email value changes
 * @param modifier Modifier = Modifier -> allows layout customization (padding, width, etc.)
 *
 * Internally, this component:
 * - Validates the email format using a regular expression.
 * - Displays an error message if the input is not a valid email.
 * - Includes a trailing mail icon for visual clarity.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun EmailInput(
    label: String = "Correo electrónico",
    placeholder: String = "ejemplo@correo.com",
    value: String = "",
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var emailText by rememberSaveable { mutableStateOf(value) }
    var isError by rememberSaveable { mutableStateOf(false) }

    fun validateEmail(input: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
        return input.matches(emailRegex)
    }

    StandardInput(
        label = label,
        placeholder = placeholder,
        value = emailText,
        onValueChange = {
            emailText = it
            isError = it.isNotEmpty() && !validateEmail(it)
            onValueChange(it)
        },
        isError = isError,
        errorMessage = if (isError) "Correo electrónico no válido" else null,
        modifier = modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = {}) {
                MailIcon(
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun PreviewEmailInput() {
    var text by remember { mutableStateOf("") }

    MaterialTheme {
        EmailInput(
            label = "Correo electrónico",
            placeholder = "ejemplo@correo.com",
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
