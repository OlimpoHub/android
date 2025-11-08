package com.app.arcabyolimpo.presentation.ui.components.atoms.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
 * A circular "Add" button component that displays a "+" symbol.
 *
 * @param modifier Modifier to adjust the button's layout (size, padding, etc.).
 * @param onClick Lambda executed when the button is pressed.
 *
 * The button:
 * - Uses a circular shape.
 * - Has a white background.
 * - Displays a large "+" in the primary blue color.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier =
            modifier
                .size(80.dp),
        shape = CircleShape,
        contentPadding = ButtonDefaults.ContentPadding,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = White,
            ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
    ) {
        Text(
            text = "+",
            color = PrimaryBlue,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 50.sp,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun AddButtonPreview() {
    MaterialTheme {
        AddButton(onClick = {})
    }
}
