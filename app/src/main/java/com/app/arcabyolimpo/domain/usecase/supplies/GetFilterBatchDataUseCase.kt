package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for retrieving filter data for supply batches.
 *
 * @property repository The repository that communicates with the remote API.
 * @constructor Creates an instance of [GetFilterBatchDataUseCase].
 * @throws Exception If an error occurs during the process.
 * @return A flow emitting the result of the operation.
 * @see Result
 */
class GetFilterBatchDataUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        operator fun invoke(): Flow<Result<FilterData>> =
            flow {
                emit(Result.Loading)
                try {
                    val filterData = repository.getFilterBatchData()
                    emit(Result.Success(filterData))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
