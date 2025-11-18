package com.app.arcabyolimpo.presentation.ui.components.molecules

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.R

/**
 * A styled card component used on the Home screen.
 * Displays a centered text label and an image on the right side,
 * all inside a blue gradient background. The whole card is clickable.
 *
 * @param name The title text displayed in the center-left of the card.
 * @param image The image shown on the right side of the card.
 * @param onClick Callback triggered when the card is pressed.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun HomeScreenCard(
    name: String,
    image: Painter,
    onClick: () -> Unit,
) {
    val color1 = Color(0xFF3655C7)
    val color2 = Color(0xFF011560)
    val gradientBrush =
        Brush.linearGradient(
            colors = listOf(color1, color2),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clickable(onClick = onClick),
    ) {
        Row(
            modifier =
                Modifier
                    .background(gradientBrush)
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Image(
                painter = image,
                contentDescription = null,
                modifier =
                    Modifier
                        .height(130.dp)
                        .width(130.dp),
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HomeScreenCardPreview() {
    HomeScreenCard(name = "Usuarios", onClick = {}, image = painterResource(id = R.drawable.img_arca_logo))
}
