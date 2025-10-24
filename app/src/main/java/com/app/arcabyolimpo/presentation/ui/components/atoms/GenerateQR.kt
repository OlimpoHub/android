package com.app.arcabyolimpo.presentation.ui.components.atoms

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GenerateQrButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Button(
        onClick = onClick,
        modifier = modifier.size(width = 88.dp, height = 32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFF7EB),
            contentColor = Color(0xFF2844AE)
        ),
    ){
        Text(
            text = text,
            fontSize = 12.sp
        )
    }
}
