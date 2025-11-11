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
    val filterData: FilterData? = null,
    val selectedFilters: FilterDto =
        FilterDto(
            filters = emptyMap(),
            order = "ASC",
        ),
)
