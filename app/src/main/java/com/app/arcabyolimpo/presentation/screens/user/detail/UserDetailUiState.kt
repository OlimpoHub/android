package com.app.arcabyolimpo.presentation.screens.user.detail

import com.app.arcabyolimpo.data.remote.dto.user.UserDto

data class UserDetailUiState(
    val collab: UserDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val deleteLoading: Boolean = false,
    val deleteError: String? = null,
    val deleted: Boolean = false,
)