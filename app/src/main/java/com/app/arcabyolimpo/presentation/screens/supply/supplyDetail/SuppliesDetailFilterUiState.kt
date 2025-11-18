package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Batch

data class SuppliesDetailFilterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterData: FilterData? = null,
    val selectedFilters: FilterDto = FilterDto(
        filters = emptyMap(),
        order = "ASC",
    ),
    // Debe ser lista de BATCHES, no beneficiaries
    val result: List<Batch> = emptyList(),
)
