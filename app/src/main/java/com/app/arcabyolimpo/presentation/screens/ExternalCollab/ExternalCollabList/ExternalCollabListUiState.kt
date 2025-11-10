package com.app.arcabyolimpo.presentation.screens.ExternalCollab.ExternalCollabList

import com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab

data class ExternalCollabListUiState(
    val isLoading: Boolean = false,
    val collabs: List<ExternalCollab> = emptyList(),
    val error: String? = null
)