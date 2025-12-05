package com.app.arcabyolimpo.domain.usecase.qr

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.qr.QrRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case responsible for requesting the creation of a QR code associated
 * with a specific user and workshop.
 *
 * This class acts as an intermediary between the presentation layer and
 * the repository, encapsulating the logic required to trigger the QR
 * generation process and emit loading, success, or error states.
 *
 * The result of this use case is a [Flow] that emits:
 * - [Result.Loading] while the request is being processed.
 * - [Result.Success] containing the generated QR as a [ByteArray].
 * - [Result.Error] if any exception occurs during the request.
 *
 * @property repository Repository that handles remote QR generation requests.
 */
class PostCreateQrUseCase
    @Inject
    constructor(
        private val repository: QrRepository,
    ) {
        /**
         * Executes the QR creation request for a given user and workshop.
         *
         * @param userID Identifier of the authenticated user creating the QR.
         * @param workshopID Identifier of the workshop for which the QR is generated.
         *
         * @return A [Flow] emitting the different states of the QR creation process.
         */
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
