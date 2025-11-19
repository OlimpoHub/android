package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFilterBatchDataUseCase @Inject constructor(
    private val repository: SupplyRepository
) {
    operator fun invoke(): Flow<Result<FilterData>> = flow {
        emit(Result.Loading)
        try {
            val filterData = repository.getFilterBatchData()
            emit(Result.Success(filterData))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}