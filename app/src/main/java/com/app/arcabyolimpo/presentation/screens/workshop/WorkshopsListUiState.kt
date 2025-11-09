package com.app.arcabyolimpo.presentation.screens.workshop

import com.app.arcabyolimpo.domain.model.workshops.Workshop

data class WorkshopsListUiState (
    val workshopsList: List<Workshop> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)