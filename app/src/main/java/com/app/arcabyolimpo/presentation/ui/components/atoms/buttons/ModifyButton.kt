package com.app.arcabyolimpo.presentation.ui.components.atoms.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.ui.theme.ButtonBlue
import com.app.arcabyolimpo.ui.theme.White

/**
 * ModifyButton: blue squared button with rounded corners used to modify in the app.
 *
 * @param onClick: () -> Unit -> function to execute when the button is clicked
 * @param cornerRadius: Dp = 8.dp -> how much rounded the corners are
 */

@Composable
fun ModifyButton(
    modifier: Modifier = Modifier, // Cambio 1 de varios: Agregar modifier
    onClick: () -> Unit,
    cornerRadius: Dp = 8.dp, // Cambio 2 de varios: Borrar width y height son parametros basura
) {
    Button(
        onClick = onClick,
        modifier = modifier, // Cambio 3 de varios: Usar el modifier que viene de afuera
        colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
        shape = RoundedCornerShape(cornerRadius),
        contentPadding =
            PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
    ) {
        Text(
            text = "Modificar",
            color = White,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun ModifyButtonPreview() {
    MaterialTheme {
        ModifyButton(
            onClick = { },
            modifier = Modifier.size(width = 112.dp, height = 40.dp)
        )
    }
}
