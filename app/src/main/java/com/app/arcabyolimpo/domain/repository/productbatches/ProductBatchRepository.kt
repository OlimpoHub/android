package com.app.arcabyolimpo.domain.repository.productbatches

import com.app.arcabyolimpo.data.mapper.productbatches.toDto
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch

interface ProductBatchRepository {
    suspend fun getProductBatches(): List<ProductBatch>

    suspend fun getProductBatch(id: String): ProductBatch

    suspend fun registerProductBatch(batch: ProductBatch): Result<Unit>
}
