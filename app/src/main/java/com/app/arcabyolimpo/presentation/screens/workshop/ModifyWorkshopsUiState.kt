package com.app.arcabyolimpo.presentation.screens.workshop

import com.app.arcabyolimpo.domain.model.workshops.Workshop

data class ModifyWorkshopsUiState (
    val modifyWorkshop: Workshop? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)