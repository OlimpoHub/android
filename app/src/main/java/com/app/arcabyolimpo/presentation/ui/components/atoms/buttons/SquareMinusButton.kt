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

@Suppress("ktlint:standard:function-naming")
@Composable
fun SquareMinusButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(32.dp), // 🔹 cuadrado 80x80
        shape = RoundedCornerShape(8.dp), // 🔹 esquinas redondeadas (ajusta el valor si quieres más o menos curva)
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
