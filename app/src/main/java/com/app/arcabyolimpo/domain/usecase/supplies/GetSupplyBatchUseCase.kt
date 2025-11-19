package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchList
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSupplyBatchUseCase @Inject constructor(
    private val repository: SupplyRepository,
) {
        operator fun invoke(
            expirationDate: String,
            idSupply: String,
        ): Flow<Result<SupplyBatchList>> =
            flow {
                try {
                    emit(Result.Loading)
                    val supplyBatchList = repository.supplyBatchList(expirationDate, idSupply)
                    emit(Result.Success(supplyBatchList))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
