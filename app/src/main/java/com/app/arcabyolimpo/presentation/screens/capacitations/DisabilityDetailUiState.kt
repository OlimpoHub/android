package com.app.arcabyolimpo.presentation.screens.capacitations

import com.app.arcabyolimpo.domain.model.disabilities.Disability

data class DisabilityDetailUiState(
    val isScreenLoading: Boolean = true,
    val disability: Disability? = null,
    val screenError: String? = null,
    val isDeleting: Boolean = false,
    val deleteError: String? = null,
    val deleteSuccess: Boolean = false,
)