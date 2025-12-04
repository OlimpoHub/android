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
    /**
     * Creates (generates) a QR code associated with a specific user and workshop.
     *
     * @param userID ID of the user generating the QR.
     * @param workshopID ID of the workshop for which the QR is generated.
     * @return A ByteArray containing the generated QR image in PNG format.
     */
    suspend fun postCreateQr(
        userID: String,
        workshopID: String,
    ): ByteArray

    /**
     * Starts the QR scanning process using CameraX + ML Kit and returns
     * the first QR code detected.
     *
     * @param context The Android context used to obtain the executor for analysis.
     * @return A [QrResult] containing the decoded QR value.
     */
    suspend fun postScanQr(context: Context): QrResult

    /**
     * Initializes and starts the CameraX preview and analyzer required for scanning.
     *
     * @param previewView Where the camera preview will be displayed.
     * @param lifecycleOwner Owner to bind the camera lifecycle, ensuring proper cleanup.
     */
    suspend fun startCamera(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
    )

    /**
     * Stops the QR scanning process, releases CameraX resources,
     * and clears the active analyzer.
     */
    suspend fun stopScanning()

    /**
     * Validates a scanned QR code with the backend, ensuring it is valid,
     * not expired, and not previously used.
     *
     * @param qrValue The decoded content of the scanned QR.
     * @param readTime Timestamp of when the QR was scanned.
     * @param userID ID of the user performing the scan.
     * @return A [QrResult] containing the backend validation message.
     */
    suspend fun postValidateQr(
        qrValue: String,
        readTime: Long,
        userID: String,
    ): QrResult
}
