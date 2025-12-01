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
import com.app.arcabyolimpo.ui.theme.HighlightInputBlue
import com.app.arcabyolimpo.ui.theme.HighlightRed
import com.app.arcabyolimpo.ui.theme.InputBackgroundBlue
import com.app.arcabyolimpo.ui.theme.InputBackgroundRed
import com.app.arcabyolimpo.ui.theme.PlaceholderGray
import com.app.arcabyolimpo.ui.theme.SelectInputBlue
import com.app.arcabyolimpo.ui.theme.White

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
            color = White,
        )

        OutlinedTextField(
            value = limited,
            onValueChange = { onValueChange(if (it.length <= maxChars) it else it.take(maxChars)) },
            placeholder = {
                Text(
                    text = placeholder,
                    color = PlaceholderGray
                )
            },
            modifier = Modifier
                .fillMaxWidth(widthFraction)
                .then(if (maxWidthDp != null) Modifier.widthIn(max = maxWidthDp) else Modifier)
                .padding(top = 4.dp),
            shape = RoundedCornerShape(16.dp),
            isError = isError,
            visualTransformation = visualTransformation,
            textStyle = TextStyle(color = White),            minLines = minLines,
            maxLines = maxLines,
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
                color = if (isError) colorScheme.error else White,
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
        Column(Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
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
