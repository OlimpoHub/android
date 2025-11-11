package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/** ---------------------------------------------------------------------------------------------- *
 * GetSupplyBatchListUseCase -> Use case that receives the ID of the supply, then gathers the data
 * from the supply and its batches, updating different status
 *
 * @param repository: SupplyRepository -> repository where the api calls is found
 * ---------------------------------------------------------------------------------------------- */
class GetSupplyBatchListUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        operator fun invoke(id: String): Flow<Result<SupplyBatchExt>> =
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
