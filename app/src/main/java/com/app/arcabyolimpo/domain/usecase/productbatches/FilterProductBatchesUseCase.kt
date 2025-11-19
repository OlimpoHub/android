package com.app.arcabyolimpo.domain.usecase.productbatches

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.app.arcabyolimpo.domain.common.Result

class FilterProductBatchesUseCase @Inject constructor(
    private val repository: ProductBatchRepository
) {
    operator fun invoke(filters: FilterDto): Flow<Result<List<ProductBatch>>> = flow {
        emit(Result.Loading)
        try {
            val result = repository.filterProductBatch(filters)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
