package com.app.arcabyolimpo.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.R

/**
 * A composable view that displays an error message with a retry option.
 *
 * This view is typically shown when a network request, data load, or operation fails,
 * allowing the user to understand the issue and attempt the action again.
 *
 * The layout consists of:
 * - An error icon (currently a placeholder icon).
 * - An optional message describing the error.
 * - A retry button that triggers the provided [onRetry] action.
 *
 * @param message The error message to be displayed. If null or blank, only the icon
 * and retry button are shown.
 * @param onRetry Lambda function that is executed when the user clicks the retry button.
 * Typically used to re-trigger data loading or an API call.
 * @param modifier [Modifier] used to adjust layout or styling behavior (e.g., padding or size).
 *
 *
 * ### Example Usage:
 * ```
 * ErrorView(
 *     message = "Failed to load supplies. Please try again.",
 *     onRetry = { viewModel.loadSuppliesList() }
 * )
 * ```
 *
 * ### UI Behavior:
 * - Centers all content vertically and horizontally within its parent.
 * - Uses Material 3 theming for text and color consistency.
 * - Displays a 64dp error icon tinted with the theme’s error color.
 * - Shows a retry button labeled **“Reintentar”**.
 */
@Composable
fun ErrorView(
    message: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_return_icon), // Necesitamos un icono de error!!!! <--Rich
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error,
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (!message.isNullOrBlank()) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}
