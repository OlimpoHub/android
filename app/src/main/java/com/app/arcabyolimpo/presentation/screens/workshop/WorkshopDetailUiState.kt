package com.app.arcabyolimpo.presentation.screens.workshop

/** ---------------------------------------------------------------------------------------------- *
 * SuppliesDetailUiState -> Data class with states that the view can have, such as the workshop was
 * not received, is loading or an error had happen.
 * ---------------------------------------------------------------------------------------------- */
data class WorkshopDetailUiState (
    val isLoading: Boolean = false,
    val error: String? = null,
    // Controls the visibility of the delete confirmation dialog
    val decisionDialogVisible: Boolean = false,
    // Controls whether the snackbar should be shown
    val snackbarVisible: Boolean = false,
)
