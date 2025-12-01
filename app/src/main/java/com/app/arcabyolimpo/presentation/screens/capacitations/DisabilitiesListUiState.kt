package com.app.arcabyolimpo.presentation.screens.capacitations

import com.app.arcabyolimpo.domain.model.disabilities.Disability

data class DisabilitiesListUiState(
    val isLoading: Boolean = false,
    val disabilities: List<Disability> = emptyList(),
    val error: String? = null,
    val searchText: String = ""
)