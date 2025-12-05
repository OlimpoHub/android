package com.app.arcabyolimpo.presentation.screens.workshop

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto

/**
 * UI state model representing the current state of the modify workshop screen.
 *
 * This state is observed by the UI to reflect updates from the domain layer,
 * such as loading progress, updated workshop data, or error states.
 *
 * @property modifiedWorkshop The [WorkshopDto] item being edited and displayed on screen.
 * @property isLoading Indicates whether the workshop information is currently being processed.
 * @property error An optional error message shown when the update fails.
 * @property isSuccess Indicates whether the workshop modification was completed successfully.
 */
data class ModifyWorkshopUiState(
    val modifiedWorkshop: WorkshopDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
