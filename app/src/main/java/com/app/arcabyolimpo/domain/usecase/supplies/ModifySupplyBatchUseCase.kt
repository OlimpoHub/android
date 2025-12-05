package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.RegisterSupplyBatch
import com.app.arcabyolimpo.domain.model.supplies.SuccessMessage
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for modifying an existing supply batch.
 *
 * @property repository The repository that communicates with the remote API.
 * @constructor Creates an instance of [ModifySupplyBatchUseCase].
 * @throws Exception If an error occurs during the process.
 * @return A flow emitting the result of the operation.
 * @see Result
 */
class ModifySupplyBatchUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        operator fun invoke(
            id: String,
            batch: RegisterSupplyBatch,
        ): Flow<Result<SuccessMessage>> =
            flow {
                try {
                    emit(Result.Loading)
                    val result = repository.modifySupplyBatch(id, batch)
                    emit(Result.Success(result))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
