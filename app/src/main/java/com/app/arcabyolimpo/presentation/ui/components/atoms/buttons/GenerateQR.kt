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
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.presentation.theme.Poppins

@Composable
fun GenerateQrButton(
    onClick: () -> Unit,
    cornerRadius: Dp = 8.dp,
    width: Dp = 88.dp,
    height: Dp = 32.dp,
){
    Button(
        onClick = onClick,
        modifier = Modifier.size(width = width, height = height),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFF7EB)),
        shape = RoundedCornerShape(cornerRadius),
        contentPadding = PaddingValues(
            horizontal = 7.dp,
            vertical = 6.dp
        ),

    ){
        Text(
            text = "Generar QR",
            color = Color(0xFF2844AE),
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun GenerateQrButtonPreview() {
    MaterialTheme {
        GenerateQrButton(
            onClick = { },
        )
    }
}