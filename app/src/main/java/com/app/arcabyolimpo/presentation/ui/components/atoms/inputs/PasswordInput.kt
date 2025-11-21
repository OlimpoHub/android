package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.KeyIcon
import com.app.arcabyolimpo.presentation.utils.validatePassword
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme

/**
 * PasswordInput: composable text field specialized for password entry, with customizable
 * validation and visual feedback.
 *
 * @param label String = "Contraseña" -> text label displayed above the input field.
 * @param placeholder String = "XXXXXXXXXX" -> helper text shown when the field is empty.
 * @param value String = "" -> current password text value.
 * @param onValueChange (String) -> Unit -> callback triggered when the password value changes.
 * @param modifier Modifier = Modifier -> allows layout customization (padding, width, etc.).
 *
 * Validation parameters (all optional, customizable by the developer):
 * @param minLength Int = 8 -> minimum number of characters required.
 * @param maxLength Int = 20 -> maximum allowed number of characters.
 * @param requireUppercase Boolean = true -> requires at least one uppercase letter.
 * @param requireDigit Boolean = true -> requires at least one numeric character.
 * @param requireSpecial Boolean = true -> requires at least one special character.
 *
 * Internally, this component:
 * - Validates the password using the `validatePassword` utility function.
 * - Enforces lowercase and no spaces as fixed rules.
 * - Displays a context-specific error message when validation fails.
 * - Includes a trailing key icon that changes color based on validation state.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun PasswordInput(
    label: String = "Contraseña",
    placeholder: String = "XXXXXXXXXX",
    value: String = "",
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    minLength: Int = 8,
    maxLength: Int = 20,
    requireUppercase: Boolean = true,
    requireDigit: Boolean = true,
    requireSpecial: Boolean = true,
) {
    var passwordText by rememberSaveable { mutableStateOf(value) }
    var isError by rememberSaveable { mutableStateOf(false) }
    var errorText: String? by rememberSaveable { mutableStateOf("") }

    StandardInput(
        label = label,
        placeholder = placeholder,
        value = passwordText,
        onValueChange = {
            passwordText = it

            val (isValid, message) =
                validatePassword(
                    password = it,
                    minLength = minLength,
                    maxLength = maxLength,
                    requireUppercase = requireUppercase,
                    requireDigit = requireDigit,
                    requireSpecial = requireSpecial,
                )

            isError = it.isNotEmpty() && !isValid
            errorText = if (!isValid) message else ""

            onValueChange(it)
        },
        isError = isError,
        errorMessage = if (isError) errorText else null,
        modifier = modifier.fillMaxWidth(),
        trailingIcon = {
            KeyIcon(
                tint =
                    if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
            )
        },
        visualTransformation = PasswordVisualTransformation(),
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPasswordInput() {
    var text by remember { mutableStateOf("") }
    ArcaByOlimpoTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            PasswordInput(
                value = text,
                onValueChange = { text = it },
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f),
            )
        }
    }
}
