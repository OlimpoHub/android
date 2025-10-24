package com.app.arcabyolimpo.presentation.ui.components.atoms

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
/**
 * SaveButton: blue squared button with rounded corners used to save changes in the app.
 *
 * @param modifier: Modifier -> modifier to customize the button
 * @param onClick: () -> Unit -> function to execute when the button is clicked
 * @param cornerRadius: Dp = 8.dp -> how much rounded the corners are
 */
@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    cornerRadius: Dp = 8.dp,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3655C7)),
        shape = RoundedCornerShape(cornerRadius),
    ) {
        Text(
            text = "Guardar",
            color = Color.White,+
            fontFamily = 
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun SaveButtonPreview() {
    MaterialTheme {
        SaveButton(
            modifier =
                Modifier
                    .size(width = 112.dp, height = 40.dp),
            onClick = { },
        )
    }
}
