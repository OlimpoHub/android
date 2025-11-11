package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

import com.app.arcabyolimpo.domain.model.supplies.Supply

data class SuppliesDetailUiState(
    val supplyBatchList: Supply? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
