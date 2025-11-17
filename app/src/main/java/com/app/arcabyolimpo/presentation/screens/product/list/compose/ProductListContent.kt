package com.app.arcabyolimpo.presentation.screens.product.list.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.White

/**
 * Composable that displays the list of products with loading and error states.
 *
 * This component handles three main states:
 * 1. Loading: Shows a circular progress indicator
 * 2. Error: Displays error message with retry button
 * 3. Success: Shows the list of products
 *
 * @param productsList List of products to display
 * @param isLoading Whether the data is currently loading
 * @param error Optional error message to display
 * @param onProductClick Callback when a product is clicked, receives product ID
 * @param onRetry Callback when retry button is clicked
 * @param modifier Modifier for this composable
 */
@Composable
fun ProductListContent(
    productsList: List<Product>,
    isLoading: Boolean,
    error: String?,
    onProductClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = White
                )
            }
            error != null -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = error,
                        color = ErrorRed,
                        style = Typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White
                        )
                    ) {
                        Text("Reintentar", color = Background)
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = productsList,
                        key = { it.id }
                    ) { product ->
                        ProductCard(
                            product = product,
                            onClick = {
                                println("🔵 ProductCard clicked! Product ID: '${product.id}'")
                                if (product.id.isNotEmpty()) {
                                    println("🟢 Calling onProductClick with ID: '${product.id}'")
                                    onProductClick(product.id)
                                } else {
                                    println("🔴 Product ID is EMPTY!")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}