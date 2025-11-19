package com.app.arcabyolimpo.presentation.screens.supply.supplyBatchList

import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchList

data class SupplyBatchListUiState(
    val supplyBatchList: SupplyBatchList? = null,
    val supplyName: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val decisionDialogVisible: Boolean = false,
    val snackbarVisible: Boolean = false,
    val snackbarMessage: String? = null,
)
