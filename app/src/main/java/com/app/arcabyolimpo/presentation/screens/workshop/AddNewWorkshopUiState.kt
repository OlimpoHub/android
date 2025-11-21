package com.app.arcabyolimpo.presentation.screens.workshop

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto

/**
 * UI state model representing the current state of the register workshop screen.
 *
 * This state is observed by the UI to reflect updates from the domain layer,
 * such as loading progress, data availability, or errors.
 *
 * @property addNewWorkshop The new [Workshop] item forms displayed on screen.
 * @property isLoading Indicates whether the workshop forms is being loaded.
 * @property error An optional error message shown when data loading fails.
 * @property isSuccess Indicates if the action of register the workshop is a success.
 */
data class AddNewWorkshopUiState(
    val addNewWorkshop: WorkshopDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)