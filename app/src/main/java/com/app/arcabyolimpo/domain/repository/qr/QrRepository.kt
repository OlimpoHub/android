package com.app.arcabyolimpo.domain.repository.qr

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.app.arcabyolimpo.domain.model.qr.QrResult
import kotlinx.coroutines.flow.Flow

interface QrRepository {
    suspend fun postCreateQr(
        userID: String,
        workshopID: String,
    ): ByteArray

    suspend fun postScanQr(context: Context): QrResult

    suspend fun startCamera(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
    )

    suspend fun stopScanning()

    suspend fun postValidateQr(
        qrValue: String,
        readTime: Long,
        userID: String,
    ): QrResult
}
