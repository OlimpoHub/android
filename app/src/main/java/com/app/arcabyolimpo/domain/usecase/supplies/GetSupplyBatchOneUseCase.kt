package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.RegisterSupplyBatch
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

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
