package com.app.arcabyolimpo.presentation.screens.supply.supplyBatchList

import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchList

/** ---------------------------------------------------------------------------------------------- *
 * SupplyBatchListUiState -> Data class with states that the view can have, such as the loding
 * state or an error had happen.
 * ---------------------------------------------------------------------------------------------- */
data class SupplyBatchListUiState(
    val supplyBatchList: SupplyBatchList? = null,
    val supplyName: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val decisionDialogVisible: Boolean = false,
    val snackbarVisible: Boolean = false,
    val snackbarMessage: String? = null,
)
