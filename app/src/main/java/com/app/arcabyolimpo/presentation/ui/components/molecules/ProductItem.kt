package com.app.arcabyolimpo.presentation.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ViewButton
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.DangerGray
import com.app.arcabyolimpo.ui.theme.White

/**
 * ProductItem: Item from the product list.
 *
 * @param name Product name.
 * @param unitaryPrice Unit price (already formatted as text).
 * @param workshopName Workshop name (can be null).
 * @param imageUrl URL of the product image (can be null).
 * @param onClick Action taken when the "View" button is pressed.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun ProductItem(
    name: String,
    unitaryPrice: String,
    workshopName: String?,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp, horizontal = 0.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Background,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(0.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (imageUrl.isNullOrBlank()) {
                    androidx.compose.foundation.layout.Box(
                        modifier =
                            Modifier
                                .size(60.dp)
                                .background(DangerGray, CircleShape)
                                .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Img", color = White.copy(alpha = 0.6f), fontSize = 12.sp)
                    }
                } else {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Imagen de $name",
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                    )
                }

                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = name,
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = Poppins,
                    )
                    Text(
                        text = "Precio unitario: $unitaryPrice",
                        style = MaterialTheme.typography.bodySmall,
                        color = White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                    )
                    workshopName?.let {
                        Text(
                            text = "Taller: $it",
                            style = MaterialTheme.typography.bodySmall,
                            color = White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                        )
                    }
                }

                ViewButton(onClick = onClick)
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.7.dp,
                color = DangerGray.copy(alpha = 0.3f),
            )
        }
    }
}