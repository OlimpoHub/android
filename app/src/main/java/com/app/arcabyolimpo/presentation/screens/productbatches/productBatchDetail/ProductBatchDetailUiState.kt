package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchDetail

import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel

/** UI state for ProductBatchDetailScreen.
 * @param isLoading Boolean -> indicates if data is loading
 * @param error String? -> error message if any
 * @param batch ProductBatchUiModel? -> the product batch details
*/
data class ProductBatchDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val batch: ProductBatchUiModel? = null,
)
