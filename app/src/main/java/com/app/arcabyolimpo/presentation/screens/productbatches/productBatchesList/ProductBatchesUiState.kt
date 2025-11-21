package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel

/** UI state for ProductBatchesListScreen.
 * @param isLoading Boolean -> indicates if data is loading
 * @param error String? -> error message if any
 * @param batches List<ProductBatchUiModel> -> list of product batches
*/
data class ProductBatchesUiState(
    val batches: List<ProductBatchUiModel> = emptyList(),
    val filters: FilterDto = FilterDto(),
    val filterData: FilterData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
