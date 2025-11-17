package com.app.arcabyolimpo.data.repository.productbatches

import com.app.arcabyolimpo.data.mapper.productbatches.toDomain
import com.app.arcabyolimpo.data.mapper.productbatches.toDto
import com.app.arcabyolimpo.data.mapper.productbatches.toModifyDto
import com.app.arcabyolimpo.data.mapper.productbatches.toRegisterDto
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository

/**
 * Implementation of ProductBatchRepository that interacts with the remote API.
 *
 * @param api ArcaApi -> Retrofit API interface for network calls
 */
class ProductBatchRepositoryImpl(
    private val api: ArcaApi,
) : ProductBatchRepository {
    override suspend fun getProductBatches(): List<ProductBatch> = api.getProductBatches().map { it.toDomain() }

    override suspend fun getProductBatch(id: String): ProductBatch = api.getProductBatch(id).toDomain()

    override suspend fun registerProductBatch(batch: ProductBatch) {
        val dto = batch.toRegisterDto()
        api.addProductBatch(dto)
    }

    override suspend fun modifyProductBatch(
        batch: ProductBatch,
        id: String,
    ) {
        val dto = batch.toModifyDto()
        api.modifyProductBatch(
            id = id,
            batch = dto,
        )
    }
}
