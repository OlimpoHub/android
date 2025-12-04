package com.app.arcabyolimpo.domain.usecase.productbatches

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository
import javax.inject.Inject

/**
 * A use case for modifying an existing product batch. *
 * This class encapsulates the business logic for updating a product batch, acting as a bridge
 * between the ViewModel and the repository.
 *
 * @param repository The repository for handling product batch data operations.
 */
class ModifyProductBatchUseCase
    @Inject
    constructor(
        private val repository: ProductBatchRepository,
    ) {
        suspend operator fun invoke(
            batch: ProductBatch,
            id: String,
        ) {
            repository.modifyProductBatch(batch = batch, id = id)
        }
    }
