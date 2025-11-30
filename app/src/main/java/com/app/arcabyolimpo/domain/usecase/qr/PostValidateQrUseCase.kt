package com.app.arcabyolimpo.domain.usecase.qr

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.qr.QrResult
import com.app.arcabyolimpo.domain.repository.qr.QrRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostValidateQrUseCase
    @Inject
    constructor(
        private val repository: QrRepository,
    ) {
        operator fun invoke(
            qrValue: String?,
            readTime: Long,
            userID: String,
        ): Flow<Result<QrResult>> =
            flow {
                try {
                    emit(Result.Loading)
                    val response = repository.postValidateQr(qrValue.orEmpty(), readTime, userID)
                    emit(Result.Success(response))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
