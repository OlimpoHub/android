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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.ui.theme.PrimaryBlue
import com.app.arcabyolimpo.ui.theme.White


/**
 * A small square button component that displays a "-" symbol.
 *
 * @param modifier Modifier to adjust the button's layout (e.g., size or padding).
 * @param onClick Lambda function executed when the button is clicked.
 *
 * The button:
 * - Has a fixed square size (32x32 dp).
 * - Uses slightly rounded corners for a soft appearance.
 * - Has a white background with subtle elevation.
 * - Displays a bold "-" symbol in the primary blue color.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun SquareMinusButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(32.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = White,
            ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
    ) {
        Text(
            text = "-",
            color = PrimaryBlue,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = false)
@Composable
fun SquareMinusButtonPreview() {
    MaterialTheme {
        SquareMinusButton(onClick = {})
    }
}
