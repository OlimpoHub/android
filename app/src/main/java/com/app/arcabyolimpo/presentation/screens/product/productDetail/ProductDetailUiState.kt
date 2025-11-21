package com.app.arcabyolimpo.presentation.screens.product.productDetail

import com.app.arcabyolimpo.domain.model.product.Product

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val error: String? = null,
    val decisionDialogVisible: Boolean = false,
    val snackbarVisible: Boolean = false,
    val shouldNavigateBack: Boolean = false,
)
