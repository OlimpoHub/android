package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.RegisterSupplyBatch
import com.app.arcabyolimpo.domain.model.supplies.SuccessMessage
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for registering a new supply batch.
 *
 * Contract:
 * - Input: a [RegisterSupplyBatch] domain object containing the data required
 *   to create a new batch (supplyId, quantity, dates, acquisition type).
 * - Output: a Flow emitting [Result] wrappers to represent the asynchronous
 *   execution states: Loading -> Success -> Error.
 *
 * Behaviour:
 * - Emits [Result.Loading] immediately to indicate work has started.
 * - Calls [SupplyRepository.registerSupplyBatch] to perform the network/database
 *   operation. On success emits [Result.Success] with the returned domain model.
 * - Catches exceptions and emits [Result.Error] containing the thrown exception.
 *
 */
class RegisterSupplyBatchUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        /**
         * Invoke the use case.
         *
         * Emits a [Result] Flow with the following lifecycle:
         * - Loading: operation started
         * - Success: repository returned the created batch
         * - Error: an exception was thrown while registering the batch
         *
         * @param batch The domain model describing the batch to register.
         * @return Flow<Result<RegisterSupplyBatch>> reactive stream of operation status.
         */
        operator fun invoke(batch: RegisterSupplyBatch): Flow<Result<SuccessMessage>> =
            flow {
                try {
                    emit(Result.Loading)
                    val result = repository.registerSupplyBatch(batch)
                    emit(Result.Success(result))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
