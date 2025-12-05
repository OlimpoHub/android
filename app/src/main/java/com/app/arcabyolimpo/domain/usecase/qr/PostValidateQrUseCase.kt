package com.app.arcabyolimpo.domain.usecase.qr

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.qr.QrResult
import com.app.arcabyolimpo.domain.repository.qr.QrRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case responsible for validating a scanned QR code.
 *
 * This use case interacts with the QR repository to validate the received
 * QR value along with the timestamp and user identifier. It emits a flow of
 * [Result] representing loading, success, or error states.
 *
 * @property repository The QR repository that handles validation requests.
 */
class PostValidateQrUseCase
    @Inject
    constructor(
        private val repository: QrRepository,
    ) {
        /**
         * Invokes the QR validation process.
         *
         * @param qrValue The raw value read from the QR code. If null, an empty string is used.
         * @param readTime Timestamp indicating when the QR code was scanned.
         * @param userID ID of the user performing the scan.
         *
         * @return A [Flow] emitting the validation result wrapped in [Result].
         */
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
