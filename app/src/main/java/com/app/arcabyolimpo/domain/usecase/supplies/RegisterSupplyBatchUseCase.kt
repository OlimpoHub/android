package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterSupplyBatchUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        operator fun invoke(batch: SupplyBatch): Flow<Result<SupplyBatch>> =
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
