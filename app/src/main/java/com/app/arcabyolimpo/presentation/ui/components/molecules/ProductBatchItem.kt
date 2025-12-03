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
import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ViewButton
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ButtonBlue
import com.app.arcabyolimpo.ui.theme.DangerGray
import com.app.arcabyolimpo.ui.theme.White

/** ProductBatchItem: composable displaying a product batch item with image, name, creation date, and quantity.
 *
 * @param batch ProductBatchUiModel -> the product batch data to display
 * @param modifier Modifier = Modifier -> allows layout customization (padding, width, etc.)
 * @param onClick () -> Unit -> callback when the item is clicked
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun ProductBatchItem(
    batch: ProductBatchUiModel,
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
                AsyncImage(
                    model = "http://74.208.78.8:8080/" + batch.imagen,
                    contentDescription = batch.nombre,
                    modifier =
                        Modifier
                            .size(60.dp)
                            .background(color = ButtonBlue.copy(alpha = 0.1f), shape = CircleShape)
                            .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )

                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = batch.nombre,
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = "Creación: ${batch.fechaRealizacionFormatted.substring(0, 10)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "Cantidad: ${batch.cantidadProducida} piezas",
                        style = MaterialTheme.typography.bodySmall,
                        color = White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                    )
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
