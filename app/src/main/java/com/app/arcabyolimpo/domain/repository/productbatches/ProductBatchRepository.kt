package com.app.arcabyolimpo.domain.repository.productbatches

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch

interface ProductBatchRepository {
    suspend fun getProductBatches(): List<ProductBatch>
}
