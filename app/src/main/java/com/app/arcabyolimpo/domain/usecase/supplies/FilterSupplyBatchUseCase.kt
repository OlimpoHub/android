package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for filtering supply batches by delegating to the repository.
 *
 * @property repository The repository that communicates with the remote API.
 */
class FilterSupplyBatchUseCase @Inject constructor(
    private val repository: SupplyRepository
) {
    /**
     * Executes the filter action and emits loading, success, or error states.
     *
     * @param idSupply The ID of the supply whose batches will be filtered.
     * @param filters The selected filters.
     */
    operator fun invoke(idSupply: String, filters: FilterDto): Flow<Result<List<Batch>>> = flow {
        emit(Result.Loading)
        try {
            val filteredBatches = repository.filterSupplyBatch(idSupply, filters)
            emit(Result.Success(filteredBatches))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
