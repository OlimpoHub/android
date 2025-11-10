package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSupplyBatchListUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        operator fun invoke(id: String): Flow<Result<Supply>> =
            flow {
                try {
                    emit(Result.Loading)
                    val supplyBatchList = repository.getSupplyBatchById(id)
                    emit(Result.Success(supplyBatchList))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
