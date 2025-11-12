package com.app.arcabyolimpo.presentation.screens.user

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.model.filter.FilterData

/**
 * UI state model representing the current state of the user list screen.
 *
 * This state holds all data necessary for rendering the user list UI,
 * including the complete user dataset, search results, and filter configuration.
 * It is updated by the [UserListViewModel] in response to repository or user actions.
 *
 * @property users The list of all [UserDto] objects currently available.
 * @property searchedUsers The subset of users filtered by the current search query.
 * @property allUsers A reference list of all users before filtering or searching.
 * @property isLoading Indicates whether user data is currently being fetched.
 * @property error Optional error message shown when data loading fails.
 * @property filterData The available filtering options displayed in the filter modal.
 * @property selectedFilters The active filters and ordering applied to the user list.
 */

data class UserListUiState(
    val users: List<UserDto> = emptyList(),
    val searchedUsers: List<UserDto> = emptyList(),
    val allUsers: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterData: FilterData =
        FilterData(
            sections =
                mapOf(
                    "Estatus"
                        to listOf("Activo", "Inactivo"),
                    "Documentos entregados"
                        to
                        listOf(
                            "Reglamento interno",
                            "Copia de INE",
                            "Aviso de confidencialidad",
                        ),
                ),
        ),
    val selectedFilters: FilterDto =
        FilterDto(
            filters = emptyMap(),
            order = "ASC",
        ),
)
