package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FilterSupplyBatchUseCase @Inject constructor(
    private val repository: SupplyRepository
) {
    operator fun invoke(filters: FilterDto): Flow<Result<List<Batch>>> = flow {
        emit(Result.Loading)
        try {
            val filteredBatches = repository.filterSupplyBatch(filters)
            emit(Result.Success(filteredBatches))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}