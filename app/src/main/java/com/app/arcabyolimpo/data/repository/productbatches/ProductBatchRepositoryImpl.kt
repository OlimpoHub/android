package com.app.arcabyolimpo.data.repository.productbatches

import com.app.arcabyolimpo.data.local.product.ProductBatchPreferences
import com.app.arcabyolimpo.data.mapper.productbatches.toDomain
import com.app.arcabyolimpo.data.mapper.productbatches.toDto
import com.app.arcabyolimpo.data.mapper.productbatches.toModifyDto
import com.app.arcabyolimpo.data.mapper.productbatches.toRegisterDto
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository
import java.io.IOException

/**
 * Implementation of ProductBatchRepository that interacts with the remote API.
 *
 * @param api ArcaApi -> Retrofit API interface for network calls
 */
class ProductBatchRepositoryImpl(
    private val api: ArcaApi,
    private val preferences: ProductBatchPreferences,
) : ProductBatchRepository {
    override suspend fun getProductBatches(): List<ProductBatch> {
        if (preferences.isCacheValid()) {
            val cachedData = preferences.getProductBatchCache()
            if (cachedData != null) {
                return cachedData.productBatchList
            }
        }

        return try {
            val remoteList = api.getProductBatches().map { it.toDomain() }
            preferences.saveProductBatchList(remoteList)
            remoteList
        } catch (e: IOException) {
            val cachedData = preferences.getProductBatchCache()
            if (cachedData != null) {
                cachedData.productBatchList
            } else {
                throw e
            }
        }
    }
    override suspend fun getProductBatch(id: String): ProductBatch = api.getProductBatch(id).toDomain()

    override suspend fun registerProductBatch(batch: ProductBatch) {
        val dto = batch.toRegisterDto()
        api.addProductBatch(dto)
        preferences.clearCache()
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
        preferences.clearCache()
    }


    override suspend fun searchProductBatch(term: String): List<ProductBatch> {
        return api.searchProductBatch(term).map { it.toDomain() }
    }

    override suspend fun filterProductBatch(filters: FilterDto): List<ProductBatch> {
        return api.filterProductBatch(filters).map { it.toDomain() }
    }

    override suspend fun deleteProductBatch(id: String) {
        api.deleteProductBatch(id)
        preferences.clearCache()
    }
}

