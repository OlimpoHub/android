package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.RegisterSupplyBatch
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for retrieving a single supply batch by its ID.
 *
 * @property repository The repository that communicates with the remote API.
 * @constructor Creates an instance of [GetSupplyBatchOneUseCase].
 * @throws Exception If an error occurs during the process.
 * @return A flow emitting the result of the operation.
 * @see Result
 */
class GetSupplyBatchOneUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        operator fun invoke(id: String): Flow<Result<RegisterSupplyBatch>> =
            flow {
                try {
                    emit(Result.Loading)
                    val batch = repository.getSupplyBatchOne(id)
                    emit(Result.Success(batch))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
