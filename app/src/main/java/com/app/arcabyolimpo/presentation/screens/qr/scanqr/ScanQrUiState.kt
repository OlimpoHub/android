package com.app.arcabyolimpo.presentation.screens.qr.scanqr

import com.app.arcabyolimpo.domain.model.qr.QrResult

data class ScanQrUiState(
    val response: QrResult? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
