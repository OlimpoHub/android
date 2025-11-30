package com.app.arcabyolimpo.presentation.screens.qr.scanresult

import com.app.arcabyolimpo.domain.model.qr.QrResult

data class ScanResultUiState(
    val response: QrResult? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
