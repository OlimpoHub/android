package com.app.arcabyolimpo.domain.usecase.qr

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.qr.QrResult
import com.app.arcabyolimpo.domain.repository.qr.QrRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case responsible for handling the QR scanning process and communicating
 * with the underlying repository layer.
 *
 * This class orchestrates three main responsibilities:
 * 1. Executing the QR scan request and emitting loading, success, or error states.
 * 2. Starting the camera and preparing it for QR code detection.
 * 3. Stopping QR scanning and releasing camera resources.
 *
 * It acts as an intermediary between the UI and the repository, keeping
 * the higher-level layers unaware of the data handling implementation.
 *
 * @property repository Repository that manages camera operations and QR scanning logic.
 */
class PostScanQrUseCase
    @Inject
    constructor(
        private val repository: QrRepository,
    ) {
        /**
         * Triggers a request to scan a QR code using the currently captured camera frame.
         *
         * This function emits:
         * - [Result.Loading] while the scan request is being processed.
         * - [Result.Success] containing the parsed QR result.
         * - [Result.Error] if scanning or backend validation fails.
         *
         * @param context Android context required for some scanning-related operations.
         * @return A [Flow] emitting the different scan states.
         */
        operator fun invoke(context: Context): Flow<Result<QrResult>> =
            flow {
                try {
                    emit(Result.Loading)
                    val response = repository.postScanQr(context)
                    emit(Result.Success(response))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }

        /**
         * Initializes and starts the camera to prepare for QR scanning.
         *
         * Delegates camera configuration and processing pipeline setup to the repository.
         *
         * @param previewView UI component where the camera stream will be displayed.
         * @param lifecycleOwner Lifecycle owner required to bind camera use cases.
         */
        suspend fun startCamera(
            previewView: PreviewView,
            lifecycleOwner: LifecycleOwner,
        ) {
            repository.startCamera(previewView, lifecycleOwner)
        }

        /**
         * Stops QR scanning and releases any underlying camera resources.
         *
         * Ensures that no further detections occur until the camera is restarted.
         */
        suspend fun stopScanning() {
            repository.stopScanning()
        }
    }
