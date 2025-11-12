package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList

import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel

data class ProductBatchesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val batches: List<ProductBatchUiModel> = emptyList(),
)
