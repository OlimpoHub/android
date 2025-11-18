package com.app.arcabyolimpo.presentation.screens.qr.qr

import android.graphics.Bitmap

data class QrUiState(
    val qrBitmap: Bitmap? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
