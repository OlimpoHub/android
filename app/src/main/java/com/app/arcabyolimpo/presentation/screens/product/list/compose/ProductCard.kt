package com.app.arcabyolimpo.presentation.screens.product.list.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Importar clickable para detectar el evento de click en el card completo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale // Importación necesaria
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Importación clave para mostrar la imagen
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ViewButton
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ButtonBlue
import com.app.arcabyolimpo.ui.theme.DangerGray
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.White

/**
 * Displays a product card with image, details, and an action button in a Material Design layout.
 *
 * This composable renders a comprehensive product card that includes a circular product image (or
 * placeholder), product name, workshop association, pricing information, availability status, and
 * a view button for navigation. The card features a press animation that scales down slightly when
 * touched, providing visual feedback to users.
 *
 * The layout is organized horizontally with the product image on the left, product details in the
 * center taking up available space, and a view button aligned to the right. A divider line appears
 * at the bottom of the card to visually separate it from other items in a list.
 *
 * @param product The Product domain model containing all information to display, including name,
 *                imageUrl (optional), unitaryPrice, workshopName (optional), and available status.
 * @param onClick Callback function invoked when the view button is clicked. Typically used to
 *                navigate to the product detail screen or trigger a product-related action.
 * @param modifier Optional Modifier for customizing the card's layout, padding, or other visual
 *                 properties. Applied to the root Card composable. Defaults to Modifier if not provided.
 *
 * @see Product The domain model representing product data
 * @see ViewButton The action button component used for navigation
 *
 */
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "press_scale_animation",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 0.dp)
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = Background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 12.dp)
                    .padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (product.imageUrl.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .size(74.dp)
                            .background(color = ButtonBlue.copy(alpha = 0.1f), shape = CircleShape)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Img", color = White.copy(alpha = 0.6f), fontSize = 14.sp)
                    }
                } else {
                    AsyncImage(
                        model = "http://74.208.78.8:8080/" + product.imageUrl,
                        contentDescription = "Imagen de ${product.name}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(74.dp)
                            .clip(CircleShape),
                    )
                }

                // Product details column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    // Product name
                    Text(
                        text = product.name,
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = Poppins,
                        style = MaterialTheme.typography.bodySmall,
                    )

                    // Workshop name (if available)
                    if (product.workshopName != null) {
                        Text(
                            text = product.workshopName!!,
                            color = White.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            fontFamily = Poppins,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    // Price and availability row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Price
                        Text(
                            text = "$${product.unitaryPrice}",
                            color = White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            fontFamily = Poppins,
                        )

                        // Availability indicator
                        Text(
                            text = "•",
                            color = if (product.available) White else ErrorRed,
                            fontSize = 14.sp
                        )

                        Text(
                            text = if (product.available) "Disponible" else "No disponible",
                            color = if (product.available) White.copy(alpha = 0.8f) else ErrorRed,
                            fontSize = 12.sp,
                            fontFamily = Poppins,
                        )
                    }
                }

                // View button
                ViewButton(onClick = onClick)
            }

            // Divider
            Divider(
                color = DangerGray.copy(alpha = 0.3f),
                thickness = 0.7.dp,
                modifier = Modifier.padding(horizontal = 16.dp),
                )
        }
    }
}