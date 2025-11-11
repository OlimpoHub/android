package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt

data class SuppliesDetailUiState(
    val supplyBatchList: SupplyBatchExt? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
