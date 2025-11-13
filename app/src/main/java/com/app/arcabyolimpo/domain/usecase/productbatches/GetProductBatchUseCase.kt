package com.app.arcabyolimpo.domain.usecase.productbatches

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository
import javax.inject.Inject

class GetProductBatchUseCase
    @Inject
    constructor(
        private val repository: ProductBatchRepository,
    ) {
        suspend operator fun invoke(id: String): ProductBatch = repository.getProductBatch(id)
    }
