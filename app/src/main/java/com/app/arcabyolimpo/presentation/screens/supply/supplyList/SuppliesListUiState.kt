package com.app.arcabyolimpo.presentation.screens.supply.supplyList

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Supply

/**
 * UI state model representing the current state of the supply list screen.
 *
 * This state is observed by the UI to reflect updates from the domain layer,
 * such as loading progress, data availability, or errors.
 *
 * @property suppliesList The list of [Supply] items currently displayed on screen.
 * @property isLoading Indicates whether the supply list is being loaded.
 * @property error An optional error message shown when data loading fails.
 */
data class SuppliesListUiState(
    val suppliesList: List<Supply> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    /**
     * Optional data used to populate the filter panel for supplies.
     */
    val filterData: FilterData? = null,
    /**
     * Holds the currently selected filters and sorting order.
     *
     * - `filters`: Map of filter keys and selected values.
     * - `order`: Sorting order of the list ("ASC" for ascending, "DESC" for descending).
     *
     * This is sent to the ViewModel when applying filters.
     */
    val selectedFilters: FilterDto =
        FilterDto(
            filters = emptyMap(),
            order = "ASC",
        ),
)
