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
 * SaveButton: blue squared button with rounded corners used to save changes in the app.
 *
 * @param onClick: () -> Unit -> function to execute when the button is clicked
 * @param cornerRadius: Dp = 8.dp -> how much rounded the corners are
 * @param width Dp -> button width (default: 112.dp)
 * @param height Dp -> button height (default: 40.dp)
 */

@Suppress("ktlint:standard:function-naming")
@Composable
fun SaveButton(
    onClick: () -> Unit,
    cornerRadius: Dp = 8.dp,
    width: Dp = 112.dp,
    height: Dp = 40.dp,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(width = width, height = height),
        colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
        shape = RoundedCornerShape(cornerRadius),
        contentPadding =
            PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
    ) {
        Text(
            text = "Guardar",
            color = White,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(
    showBackground = true,
)
@Composable
fun SaveButtonPreview() {
    MaterialTheme {
        SaveButton(
            onClick = { },
        )
    }
}
