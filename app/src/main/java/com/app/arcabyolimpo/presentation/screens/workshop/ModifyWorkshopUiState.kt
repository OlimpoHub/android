package com.app.arcabyolimpo.presentation.screens.workshop

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto

data class ModifyWorkshopUiState(
    val modifiedWorkshop: WorkshopDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)