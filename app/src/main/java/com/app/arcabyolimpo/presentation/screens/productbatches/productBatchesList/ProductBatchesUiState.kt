package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel

/** UI state for ProductBatchesListScreen.
 * @param batches The list of product batches to be displayed on the screen.
 * @param filters The data transfer object containing the currently applied filters.
 * @param filterData The data used to populate the filter options, such as available product names.
 * @param isLoading A boolean flag that indicates if the list of batches is currently being loaded.
 * @param error A nullable string containing an error message if a problem occurred during data fetching.
*/
data class ProductBatchesUiState(
    val batches: List<ProductBatchUiModel> = emptyList(),
    val filters: FilterDto = FilterDto(),
    val filterData: FilterData? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
