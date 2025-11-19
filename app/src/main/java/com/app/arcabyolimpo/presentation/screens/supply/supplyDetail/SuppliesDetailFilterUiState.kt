package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Batch

/**
 * Represents the UI state for the supply detail screen filters.
 *
 * @property isLoading Indicates whether a filter or data load operation is in progress.
 * @property error Error message if an operation fails.
 * @property filterData Available filter categories and values.
 * @property selectedFilters Filters currently applied by the user.
 * @property result List of batches filtered by the selected criteria.
 */
data class SuppliesDetailFilterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterData: FilterData? = null,
    val selectedFilters: FilterDto = FilterDto(
        filters = emptyMap(),
        order = "ASC",
    ),
    val result: List<Batch> = emptyList(),
)

