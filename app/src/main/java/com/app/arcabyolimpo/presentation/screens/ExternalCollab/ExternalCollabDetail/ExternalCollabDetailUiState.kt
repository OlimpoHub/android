package com.app.arcabyolimpo.presentation.screens.ExternalCollab.ExternalCollabDetail

import com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab

data class ExternalCollabDetailUiState(
    val collab: ExternalCollab? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val deleteLoading: Boolean = false,
    val deleteError: String? = null,
    val deleted: Boolean = false,
)
