package com.app.arcabyolimpo.domain.usecase.productbatches

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository
import javax.inject.Inject

class RegisterProductBatchUseCase
    @Inject
    constructor(
        private val repository: ProductBatchRepository,
    ) {
        suspend operator fun invoke(batch: ProductBatch) = repository.registerProductBatch(batch)
    }
