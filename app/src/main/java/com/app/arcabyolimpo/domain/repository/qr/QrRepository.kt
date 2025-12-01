package com.app.arcabyolimpo.domain.repository.qr

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.app.arcabyolimpo.domain.model.qr.QrResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining the contract for QR-related operations.
 *
 * This layer abstracts the data sources involved in generating, scanning,
 * and validating QR codes. Implementations of this interface are responsible
 * for handling camera interactions, networking, and any necessary local logic.
 *
 * @see com.app.arcabyolimpo.domain.usecase.qr PostCreateQrUseCase, PostScanQrUseCase, PostValidateQrUseCase
 */

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
