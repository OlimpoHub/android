package com.app.arcabyolimpo.presentation.screens.workshop

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
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
 * @property filterData The available filtering options displayed in the filter modal.
 * @property selectedFilters The active filters and ordering applied to the workshops list.
 */
data class WorkshopsListUiState (
    val workshopsList: List<Workshop> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterData: FilterData =
        FilterData(
            sections =
                mapOf(
                    "Fecha"
                            to emptyList(),
                    "Hora de Entrada"
                            to listOf(
                                "06:00", "07:00", "08:00", "09:00",
                                "10:00", "11:00", "12:00", "13:00",
                                "14:00", "15:00", "16:00", "17:00",
                                "18:00", "19:00", "20:00", "21:00"),
                    "Estado"
                            to listOf("Activo", "Inactivo")

                ),
        ),
    val selectedFilters: FilterDto =
        FilterDto(
            filters = emptyMap(),
            order = "ASC",
        ),
)