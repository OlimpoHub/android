package com.app.arcabyolimpo.presentation.screens.beneficiary

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.model.filter.FilterData

/*
 * UI state model that represents the state of the beneficiary detail screen.
 */
data class BeneficiaryDetailUiState(
    val isScreenLoading: Boolean = true,
    val beneficiary: Beneficiary? = null,
    val screenError: String? = null,
    val isDeleting: Boolean = false,
    val deleteError: String? = null,
    val deleteSuccess: Boolean = false,
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
