package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt

/** ---------------------------------------------------------------------------------------------- *
 * SuppliesDetailUiState -> Data class with states that the view can have, such as the supply was
 * not received, is loading or an error had happen.
 * ---------------------------------------------------------------------------------------------- */
data class SuppliesDetailUiState(
    val supplyBatchList: SupplyBatchExt? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val decisionDialogVisible: Boolean = false,
    val snackbarVisible: Boolean = false,
)
