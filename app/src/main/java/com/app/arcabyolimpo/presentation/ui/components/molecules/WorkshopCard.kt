package com.app.arcabyolimpo.presentation.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.ui.theme.Background

@Composable

fun WorkshopCard(
    name: String,
    onClick: () -> Unit,
){
    val color1 = Color(0xFF3655C7)
    val color2 = Color(0xFF011560)
    val gradientBrush = Brush.linearGradient(
        colors = listOf(color1, color2),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Card(
        modifier =
            Modifier
                .width(340.dp)
                .height(110.dp)
                .clickable(onClick = onClick),

    ){
        Row(
            modifier =
                Modifier
                    .background(gradientBrush)
                    .fillMaxSize()
                    .padding(horizontal = 50.dp),
            verticalAlignment = Alignment.CenterVertically

        ){
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.width(0.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun WorkshopCardPreview() {
    WorkshopCard(name = "John Smith", onClick = {})
}