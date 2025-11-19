package com.app.arcabyolimpo.domain.usecase.productbatches

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository
import com.app.arcabyolimpo.domain.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchProductBatchesUseCase @Inject constructor(
    private val repository: ProductBatchRepository
) {
    operator fun invoke(term: String): Flow<Result<List<ProductBatch>>> = flow {
        emit(Result.Loading)
        try {
            val result = repository.searchProductBatch(term)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
