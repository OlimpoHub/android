package com.app.arcabyolimpo.domain.usecase.productbatches

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository
import javax.inject.Inject

/** Use case for retrieving one product batch.
 * @param repository ProductBatchRepository -> repository to fetch product batches
 * @return ProductBatch
*/
class GetProductBatchUseCase
    @Inject
    constructor(
        private val repository: ProductBatchRepository,
    ) {
        suspend operator fun invoke(id: String): ProductBatch = repository.getProductBatch(id)
    }
