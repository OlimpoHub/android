package com.app.arcabyolimpo.domain.usecase.productbatches

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository
import javax.inject.Inject

/** Use case for registering a new product batch
 * @param repository ProductBatchRepository -> repository to fetch product batches
 * @return Result<Unit>
*/
class RegisterProductBatchUseCase
    @Inject
    constructor(
        private val repository: ProductBatchRepository,
    ) {
        suspend operator fun invoke(batch: ProductBatch) = repository.registerProductBatch(batch)
    }
