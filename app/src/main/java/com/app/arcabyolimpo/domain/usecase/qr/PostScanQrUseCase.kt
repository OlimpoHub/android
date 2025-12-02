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

class PostScanQrUseCase
    @Inject
    constructor(
        private val repository: QrRepository,
    ) {
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

        suspend fun startCamera(
            previewView: PreviewView,
            lifecycleOwner: LifecycleOwner,
        ) {
            repository.startCamera(previewView, lifecycleOwner)
        }

        suspend fun stopScanning() {
            repository.stopScanning()
        }
    }
