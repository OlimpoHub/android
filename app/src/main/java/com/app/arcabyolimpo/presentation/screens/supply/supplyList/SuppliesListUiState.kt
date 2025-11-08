package com.app.arcabyolimpo.presentation.screens.supply.supplyList

import com.app.arcabyolimpo.domain.model.supplies.Supply

data class SuppliesListUiState(
    val suppliesList: List<Supply> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
