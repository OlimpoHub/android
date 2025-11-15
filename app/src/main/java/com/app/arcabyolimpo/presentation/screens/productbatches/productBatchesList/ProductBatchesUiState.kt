package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList

import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel

/** UI state for ProductBatchesListScreen.
 * @param isLoading Boolean -> indicates if data is loading
 * @param error String? -> error message if any
 * @param batches List<ProductBatchUiModel> -> list of product batches
*/
data class ProductBatchesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val batches: List<ProductBatchUiModel> = emptyList(),
)
