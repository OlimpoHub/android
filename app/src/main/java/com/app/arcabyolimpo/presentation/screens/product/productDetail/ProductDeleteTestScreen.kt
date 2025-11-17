package com.app.arcabyolimpo.presentation.screens.product.productDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

private const val TEST_PRODUCT_ID = "ec5a2023-210f-4d85-80f8-d7a9e6579071"

@Composable
fun ProductDeleteTestScreen(
    onDeleted: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { _ ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            ProductDeleteAction(
                productId = TEST_PRODUCT_ID,
                snackbarHostState = snackbarHostState,
                onDeleted = {
                    println("✅ Producto eliminado desde ProductDeleteTestScreen")
                    onDeleted()
                },
            )
        }
    }
}
