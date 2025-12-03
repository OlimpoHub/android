package com.app.arcabyolimpo.presentation.ui.components.atoms.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.ui.theme.ButtonBlue
import com.app.arcabyolimpo.ui.theme.DangerGray
import com.app.arcabyolimpo.ui.theme.InputBackgroundBlue
import com.app.arcabyolimpo.ui.theme.PrimaryBlue
import com.app.arcabyolimpo.ui.theme.White

/**
 * DecisionDialog - A reusable confirmation dialog styled according to
 *
 * This component keeps the **original M3 AlertDialog structure**
 * (title, text, dismissButton, confirmButton) but customizes
 * typography, color palette, and layout alignment.
 *
 * Features:
 * - Centered title and message text.
 * - Buttons styled as "pills" using clip + background for rounded look.
 * - Colors and fonts taken from the current MaterialTheme / custom palette.
 *
 * @param onDismissRequest Called when the dialog should be closed (outside tap or Cancel button).
 * @param onConfirmation   Called when the Confirm button is pressed.
 * @param dialogTitle      Title text shown at the top of the dialog.
 * @param dialogText       Body text providing additional information.
 * @param confirmText      Text label for the confirm button.
 * @param dismissText      Text label for the dismiss button.
 */

@Composable
fun DecisionDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    confirmText: String,
    dismissText: String,
) {
    AlertDialog(

        // Visual properties for the dialog card
        shape = RoundedCornerShape(20.dp),
        containerColor = InputBackgroundBlue,                // Fondo oscuro del popup (tu paleta)
        tonalElevation = 0.dp,

        // Title
        title = {
            Text(text = dialogTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,            // sin .copy(...) → no dispara el warning
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },

        // Message  body
        text = {
            Text(text = dialogText,
                style = MaterialTheme.typography.bodySmall,
                color = DangerGray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        },

        onDismissRequest = {
            onDismissRequest()
        },

        // Confirm Button Right side
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                },
                colors = ButtonDefaults.textButtonColors(contentColor = DangerGray)
            ) {
                Text(text = confirmText,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(end = 50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ButtonBlue)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        },
        // Dismiss Button Left side
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.textButtonColors(contentColor = PrimaryBlue)
            ) {
                Text(text = dismissText,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(end = 50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DangerGray)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                )

            }
        }
    )
}


/**
 * Preview used for validating layout, spacing, and colors inside Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    MaterialTheme {
        DecisionDialog(
            onDismissRequest = {},
            onConfirmation = {},
            dialogTitle = "¿Estas seguro de eliminar este Insumo?",
            dialogText = "Esta accion no podra revertirce",
            confirmText = "Confirmar",
            dismissText = "Cancelar",
        )
    }
}
