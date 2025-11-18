package com.app.arcabyolimpo.presentation.screens.beneficiary

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.model.filter.FilterData

data class BeneficiaryFilterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    // Datos para poblar el panel de filtros (discapacidades, etc)
    val filterData: FilterData? = null,
    // Filtros seleccionados
    val selectedFilters: FilterDto =
        FilterDto(
            filters = emptyMap(),
            order = "ASC",
        ),
    // Lista ya filtrada desde el backend
    val result: List<Beneficiary> = emptyList(),
)
