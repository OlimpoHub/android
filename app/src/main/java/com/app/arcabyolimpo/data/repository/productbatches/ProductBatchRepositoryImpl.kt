package com.app.arcabyolimpo.data.repository.productbatches

import com.app.arcabyolimpo.data.mapper.productbatches.toDomain
import com.app.arcabyolimpo.data.mapper.productbatches.toDto
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository

class ProductBatchRepositoryImpl(
    private val api: ArcaApi,
) : ProductBatchRepository {
    override suspend fun getProductBatches(): List<ProductBatch> = api.getProductBatches().map { it.toDomain() }

    override suspend fun getProductBatch(id: String): ProductBatch {
        println("DEBUG -> llamando a api.getProductBatch($id)")
        val dto = api.getProductBatch(id)
        println("DEBUG -> dto recibido: $dto")
        return dto.toDomain()
    }

//    override suspend fun getProductBatch(id: String): ProductBatch = api.getProductBatch(id).toDomain()

    override suspend fun registerProductBatch(batch: ProductBatch): Result<Unit> =
        try {
            val dto = batch.toDto()
            api.addProductBatch(dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
