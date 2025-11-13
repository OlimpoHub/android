package com.app.arcabyolimpo.data.repository.productbatches

import com.app.arcabyolimpo.data.mapper.productbatches.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository

class ProductBatchRepositoryImpl(
    private val api: ArcaApi,
) : ProductBatchRepository {
    override suspend fun getProductBatches(): List<ProductBatch> = api.getProductBatches().map { it.toDomain() }
}
