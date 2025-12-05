package com.app.arcabyolimpo.presentation.screens.workshop

/**
 * UI state model representing the current state of the workshop detail screen.
 *
 * This state is observed by the UI to reflect changes in the detail view, such as
 * loading status, errors, and UI element visibility related to user actions.
 *
 * @property isLoading Indicates whether the workshop details are being loaded.
 * @property error An optional error message shown when loading the details fails.
 * @property decisionDialogVisible Controls the visibility of the delete confirmation dialog.
 * @property snackbarVisible Controls whether the snackbar should be shown after an action.
 */
data class WorkshopDetailUiState (
    val isLoading: Boolean = false,
    val error: String? = null,
    // Controls the visibility of the delete confirmation dialog
    val decisionDialogVisible: Boolean = false,
    // Controls whether the snackbar should be shown
    val snackbarVisible: Boolean = false,
)
