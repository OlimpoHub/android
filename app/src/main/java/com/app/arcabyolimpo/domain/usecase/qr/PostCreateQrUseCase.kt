package com.app.arcabyolimpo.domain.usecase.qr

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.qr.QrRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostCreateQrUseCase
    @Inject
    constructor(
        private val repository: QrRepository,
    ) {
        operator fun invoke(
            userID: String,
            workshopID: String,
        ): Flow<Result<ByteArray>> =
            flow {
                try {
                    emit(Result.Loading)
                    val response = repository.postCreateQr(userID, workshopID)
                    emit(Result.Success(response))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
