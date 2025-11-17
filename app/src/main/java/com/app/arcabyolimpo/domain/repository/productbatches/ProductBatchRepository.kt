package com.app.arcabyolimpo.domain.repository.productbatches

import com.app.arcabyolimpo.data.mapper.productbatches.toDto
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch

interface ProductBatchRepository {
    // GetAll
    suspend fun getProductBatches(): List<ProductBatch>

    // GetOne
    suspend fun getProductBatch(id: String): ProductBatch

    // Register
    suspend fun registerProductBatch(batch: ProductBatch)

    suspend fun modifyProductBatch(
        batch: ProductBatch,
        id: String,
    )
}
