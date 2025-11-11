package com.app.arcabyolimpo.presentation.screens.user

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.model.filter.FilterData

data class UserListUiState (
    val users: List<UserDto> = emptyList(),
    val allUsers: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterData: FilterData = FilterData(
        sections = mapOf(
            "Estatus"
                    to listOf("Activo", "Inactivo"),
            "Documentos entregados"
                    to listOf("Reglamento interno",
                                "Copia de INE",
                                "Aviso de confidencialidad")
        )
    ),
    val selectedFilters: FilterDto =
        FilterDto(
            filters = emptyMap(),
            order = "ASC",
        ),
)
