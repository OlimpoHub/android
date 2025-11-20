package com.app.arcabyolimpo.presentation.screens.qr.workshopselection

import com.app.arcabyolimpo.domain.model.workshops.Workshop

/**
 * UI state model representing the current state of the workshop list screen.
 *
 * This state is observed by the UI to reflect updates from the domain layer,
 * such as loading progress, data availability, or errors.
 *
 * @property workshopList The list of [Workshop] items currently displayed on screen.
 * @property isLoading Indicates whether the workshop list is being loaded.
 * @property error An optional error message shown when data loading fails.
 */
data class QrWorkshopsListUiState(
    val workshopsList: List<Workshop> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
