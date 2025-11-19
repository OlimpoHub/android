package com.app.arcabyolimpo.presentation.screens.product.productDetail

import com.app.arcabyolimpo.domain.model.product.Product

data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val decisionDialogVisible: Boolean = false,
    val snackbarVisible: Boolean = false,
)