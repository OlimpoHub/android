package com.app.arcabyolimpo.presentation.screens.product.productDetail

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val decisionDialogVisible: Boolean = false,
    val snackbarVisible: Boolean = false,
)
