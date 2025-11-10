package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme

/** Multiline input for description text. Enforces a max length and shows a right-aligned counter. */
const val DESCRIPTION_MAX_CHARS: Int = 400
const val PLACEHOLDER_ALPHA: Float = 0.7f

@Suppress("ktlint:standard:function-naming")
@Composable
fun DescriptionInput(
    label: String = "Descripción",
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Escribe una descripción...",
    isError: Boolean = false,
    // kept for compatibility; not shown if you removed the hint line
    errorMessage: String? = null,
    minLines: Int = 4,
    maxLines: Int = 8,
    maxChars: Int = DESCRIPTION_MAX_CHARS,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    widthFraction: Float = 1f,
    maxWidthDp: Dp? = null,
) {
    val colorScheme: ColorScheme = MaterialTheme.colorScheme
    val limited: String = if (value.length <= maxChars) value else value.take(maxChars)

    Column(
        modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurface,
        )

        OutlinedTextField(
            value = limited,
            onValueChange = { onValueChange(if (it.length <= maxChars) it else it.take(maxChars)) },
            placeholder = {
                Text(
                    text = placeholder,
                    color = colorScheme.onSurface.copy(alpha = PLACEHOLDER_ALPHA)
                )
            },
            modifier = Modifier
                .fillMaxWidth(widthFraction)
                .then(if (maxWidthDp != null) Modifier.widthIn(max = maxWidthDp) else Modifier)
                .padding(top = 4.dp),
            shape = RoundedCornerShape(16.dp),
            isError = isError,
            visualTransformation = visualTransformation,
            textStyle = TextStyle(color = colorScheme.onSurface),
            minLines = minLines,
            maxLines = maxLines,
            colors = TextFieldDefaults.colors(
                focusedContainerColor   = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface,
                disabledContainerColor  = colorScheme.surface,

                focusedIndicatorColor   = if (isError) colorScheme.error else colorScheme.primary,
                unfocusedIndicatorColor = if (isError) colorScheme.error.copy(alpha = 0.7f) else colorScheme.primary,
                errorIndicatorColor     = colorScheme.error,

                focusedTextColor        = colorScheme.onSurface,
                unfocusedTextColor      = colorScheme.onSurface,
                cursorColor             = if (isError) colorScheme.error else colorScheme.primary,
            ),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(widthFraction)
                .then(if (maxWidthDp != null) Modifier.widthIn(max = maxWidthDp) else Modifier)
                .padding(top = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                text = "${limited.length}/$maxChars",
                color = if (isError) colorScheme.error else colorScheme.onSurface.copy(alpha = PLACEHOLDER_ALPHA),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/* ---------- Preview ---------- */
@Preview
@Composable
private fun DescriptionInputPreview() {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        var v by remember { mutableStateOf("") }
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            // Example: 92% width, capped at 560.dp
            DescriptionInput(
                value = v,
                onValueChange = { v = it },
                widthFraction = 0.92f,
                maxWidthDp = 560.dp
            )
        }
    }
}
