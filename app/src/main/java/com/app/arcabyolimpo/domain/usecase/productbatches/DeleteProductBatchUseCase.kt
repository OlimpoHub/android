package com.app.arcabyolimpo.domain.usecase.productbatches

import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository
import javax.inject.Inject

/** Use case for deleting a product batch
 * @param repository ProductBatchRepository -> repository of module product batches
 */
class DeleteProductBatchUseCase
    @Inject
    constructor(
        private val repository: ProductBatchRepository,
    ) {
        suspend operator fun invoke(id: String) = repository.deleteProductBatch(id)
    }
