package com.app.arcabyolimpo.presentation.ui.components.atoms.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Suppress("ktlint:standard:function-naming")
@Composable
fun InactivoStatus(
    modifier: Modifier = Modifier.padding(16.dp),
    width: Dp = 95.dp,
    height: Dp = 24.dp,
    backgroundColor: Color = Color(0xFFFFF7EB),
    text: String = "Inactivo",
    textColor: Color = Color(0xFF2844AE),
    cornerRadius: Dp = 8.dp,
    fontSize: Int = 12,
    fontFamily: FontFamily = Poppins,
) {
    Box(
        modifier =
            modifier
                .size(width = width, height = height)
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
        )
    }
}
