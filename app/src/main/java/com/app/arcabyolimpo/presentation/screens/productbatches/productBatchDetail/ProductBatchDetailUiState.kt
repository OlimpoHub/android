package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchDetail

import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel

data class ProductBatchDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val batch: ProductBatchUiModel? = null,
)
