    package com.app.arcabyolimpo.presentation.ui.components.atoms.buttons

    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
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
    import androidx.compose.ui.unit.sp
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
    fun SendEmailButton(
        onClick: () -> Unit,
        cornerRadius: Dp = 8.dp,
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
            shape = RoundedCornerShape(cornerRadius),
            contentPadding = PaddingValues(
                vertical = 15.dp
            ),
        ) {
            Text(
                text = "Enviar Correo",
                color = White,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }

    @Preview(
        showBackground = true,
    )
    @Composable
    fun SendEmailButtonPreview() {
        MaterialTheme {
            SendEmailButton(
                onClick = { },
            )
        }
    }
