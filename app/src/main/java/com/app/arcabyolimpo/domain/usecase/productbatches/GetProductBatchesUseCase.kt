package com.app.arcabyolimpo.domain.usecase.productbatches

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository
import javax.inject.Inject

/** Use case for retrieving all product batches.
 * @param repository ProductBatchRepository -> repository to fetch product batches
 * @return List<ProductBatch>
*/
class GetProductBatchesUseCase
    @Inject
    constructor(
        private val repository: ProductBatchRepository,
    ) {
        suspend operator fun invoke() = repository.getProductBatches()
    }
