package com.app.arcabyolimpo.presentation.screens.attendance

import com.app.arcabyolimpo.data.remote.dto.attendance.AttendanceDto

data class AttendanceListUiState(
    val attendances: List<AttendanceDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
