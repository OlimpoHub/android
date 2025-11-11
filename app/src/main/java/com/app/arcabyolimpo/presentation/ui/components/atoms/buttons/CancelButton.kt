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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.theme.Poppins

/**
 * CancelButton: white squared button with rounded corners and blue text.
 *
 * @param modifier Modifier -> customize the button
 * @param onClick () -> Unit -> function executed when clicked
 * @param cornerRadius Dp = 8.dp -> corner roundness
 */
@Composable
fun CancelButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    cornerRadius: Dp = 8.dp,
) {
    val blueColor = Color(0xFF3655C7)
    val whiteColor = Color(0xFFDBD5CC)

    Button(
        onClick = onClick,
        modifier = modifier.size(width = 112.dp, height = 40.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = whiteColor,
                contentColor = blueColor,
            ),
        shape = RoundedCornerShape(cornerRadius),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
    ) {
        Text(
            text = "Cancelar",
            color = blueColor,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CancelButtonPreview() {
    MaterialTheme {
        CancelButton(
            modifier = Modifier.size(width = 112.dp, height = 40.dp),
            onClick = { },
        )
    }
}
