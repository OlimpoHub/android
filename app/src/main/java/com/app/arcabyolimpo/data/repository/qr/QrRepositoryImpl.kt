@file:Suppress("ktlint:standard:filename")

package com.app.arcabyolimpo.data.repository.qr

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.qr.CreateQrDto
import com.app.arcabyolimpo.data.remote.dto.qr.ValidateQrDto
import com.app.arcabyolimpo.domain.model.qr.QrResult
import com.app.arcabyolimpo.domain.repository.qr.QrRepository
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.concurrent.Executor
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

/**
 * Implementation of [QrRepository] responsible for handling QR code generation,
 * scanning, and validation by integrating CameraX, ML Kit, and the remote API.
 *
 * This class converts callback-based camera and ML Kit operations into coroutine-friendly
 * suspend functions, enabling seamless use from ViewModels and use cases.
 *
 * @property api Remote API service used for generating and validating QR codes.
 */
class QrRepositoryImpl
    @Inject
    constructor(
        private val api: ArcaApi,
    ) : QrRepository {
        private var scanner = BarcodeScanning.getClient()
        private var analysis: ImageAnalysis? = null
        private var executor: Executor? = null
        private var appContext: Context? = null

        /**
         * Sends a request to generate a QR code for a specific user and workshop.
         *
         * @param userID ID of the user requesting the QR code.
         * @param workshopID ID of the workshop for which the QR is generated.
         * @return The QR code image as a ByteArray (PNG format).
         * @throws Exception if the API request fails.
         */
        override suspend fun postCreateQr(
            userID: String,
            workshopID: String,
        ): ByteArray {
            try {
                val response = api.postCreateQr(CreateQrDto(userID, workshopID))
                val pngBytes = response.bytes()
                return pngBytes
            } catch (e: Exception) {
                throw Exception("Error al crear qr: ${e.message}", e)
            }
        }

        /**
         * Starts scanning a QR code using CameraX + ML Kit.
         * Converts callback-based camera/ML Kit logic into a suspend function using suspendCancellableCoroutine.
         *
         * The function returns as soon as a QR code is detected OR throws an exception on failure.
         *
         * @param context Context used for setting the analyzer executor.
         * @return QrResult containing the QR code string.
         * @throws Exception if ML Kit fails to read the QR or the analyzer errors.
         */
        @OptIn(ExperimentalGetImage::class, ExperimentalCoroutinesApi::class)
        override suspend fun postScanQr(context: Context): QrResult =
            // It lets you take callback-based code (camera + MLKit are callbacks)
            // and convert it into a suspend function that returns something.
            // Inside continuation you choose when the suspend function returns:
            // 1. continue.resume(result) → success
            //  or
            // 2. continue.resumeWithException(e) → error
            suspendCancellableCoroutine { continuation ->
                val analyzer =
                    ImageAnalysis.Analyzer { imageProxy ->
                        val mediaImage = imageProxy.image

                        if (mediaImage == null) {
                            imageProxy.close()
                            return@Analyzer
                        }
                        // This creates an ML Kit–compatible image.
                        val image =
                            InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.imageInfo.rotationDegrees,
                            )
                        // This starts the ML Kit barcode detection.
                        scanner
                            .process(image)
                            .addOnSuccessListener { barcodes ->

                                val qr = barcodes.firstOrNull()?.rawValue

                                if (qr != null && continuation.isActive) {
                                    analysis?.clearAnalyzer()
                                    // Return the qr code result
                                    continuation.resume(
                                        QrResult(qr),
                                        onCancellation = null,
                                    )
                                }
                                // If ML Kit fails
                            }.addOnFailureListener { error ->
                                if (continuation.isActive) {
                                    analysis?.clearAnalyzer()
                                    continuation.resumeWithException(
                                        Exception("Error al leer QR: ${error.message}", error),
                                    )
                                }
                                // release the frame to avoid memory leaks
                            }.addOnCompleteListener {
                                imageProxy.close()
                            }
                    }
                // attach analyzer to existing analysis
                val exec = executor ?: ContextCompat.getMainExecutor(context)
                analysis?.setAnalyzer(exec, analyzer)
            }

        /**
         * Initializes and starts the CameraX preview + image analyzer needed for QR scanning.
         *
         * @param previewView View where the camera preview will be displayed.
         * @param lifecycleOwner Owner that controls the camera lifecycle.
         */
        override suspend fun startCamera(
            previewView: PreviewView,
            lifecycleOwner: LifecycleOwner,
        ) {
            appContext = previewView.context

            val cameraProvider =
                ProcessCameraProvider.getInstance(previewView.context).get()

            val preview =
                Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }

            analysis =
                ImageAnalysis
                    .Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                analysis, // attach analyzer pipeline
            )
        }

        /**
         * Stops the active camera session and analyzer used for QR scanning.
         * Safely releases CameraX resources to prevent memory leaks.
         */
        override suspend fun stopScanning() {
            val context = appContext ?: return

            try {
                val cameraProvider = ProcessCameraProvider.getInstance(context).get()

                cameraProvider.unbindAll()

                analysis?.clearAnalyzer()
                analysis = null
                executor = null

                Log.d("QrRepositoryImpl", "Camera scanning stopped")
            } catch (e: Exception) {
                Log.e("QrRepositoryImpl", "Error stopping camera: ${e.message}", e)
            }
        }

        /**
         * Validates a scanned QR code by sending it to the backend.
         *
         * The backend returns:
         * - 201 → success (QR validated)
         * - 406 → failure (invalid QR, used QR, expired QR, etc.)
         *
         * @param qrValue The text content of the scanned QR code.
         * @param readTime Timestamp when the QR was scanned.
         * @param userID ID of the user scanning the QR.
         * @return QrResult containing the backend message.
         * @throws Exception with the backend error message if validation fails.
         */
        override suspend fun postValidateQr(
            qrValue: String,
            readTime: Long,
            userID: String,
        ): QrResult {
            try {
                val response = api.postValidateQr(ValidateQrDto(qrValue, readTime, userID))

                if (!response.isSuccessful) {
                    // 406 code
                    val errorBody = response.errorBody()?.string()

                    val message =
                        try {
                            JSONObject(errorBody).getString("message")
                        } catch (e: Exception) {
                            "Unknown error"
                        }
                    Log.d("result failed", message)
                    throw Exception(message)
                }

                val body =
                    response.body()
                        ?: throw Exception("Empty response body")
                Log.d("result success", body.message)
                // 201 code
                return QrResult(body.message)
            } catch (e: Exception) {
                throw Exception(e.message)
            }
        }
    }
