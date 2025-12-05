package com.app.arcabyolimpo.data.repository.productbatches

import com.app.arcabyolimpo.data.local.product.productBatch.preferences.ProductBatchPreferences
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
 * Concrete implementation of the [ProductBatchRepository] interface.
 *
 * This class handles the logic for fetching and managing product batch data. It uses [ArcaApi]
 * for network operations and [ProductBatchPreferences] for caching data locally to support
 * offline functionality. In case of a network failure, it attempts to return cached data.
 * It also clears the cache after any write operations (create, modify, delete) to ensure
 * data consistency.
 *
 * @param api The Retrofit API interface for making network calls to the product batch endpoints.
 * @param preferences The class responsible for saving and retrieving the product batch list from
 *                    local preferences, acting as a cache.
 */
class ProductBatchRepositoryImpl(
    private val api: ArcaApi,
    private val preferences: ProductBatchPreferences,
) : ProductBatchRepository {
    /**
     * Retrieves a list of all product batches, implementing a cache-aside strategy.
     *
     * It first attempts to fetch the latest list from the remote API. If successful, it updates
     * the local cache with the new data and returns the list. If the network call fails (e.g., due to
     * no internet connection), it retrieves and returns the last known list from the cache.
     * If the cache is also empty, it re-throws the original exception.
     *
     * @return A list of [ProductBatch] domain models.
     * @throws IOException if the network call fails and the cache is empty.
     */
    override suspend fun getProductBatches(): List<ProductBatch> =
        try {
            val remoteList = api.getProductBatches().map { it.toDomain() }
            preferences.saveProductBatchList(remoteList)
            remoteList
        } catch (e: IOException) {
            val cachedData = preferences.getProductBatchCache()
            cachedData?.productBatchList ?: throw e
        }

    /**
     * Retrieves a single product batch by its ID, with a cache fallback.
     *
     * It tries to fetch the batch from the API first. If that fails, it searches for the
     * corresponding batch within the locally cached list.
     *
     * @param id The unique ID of the product batch to fetch.
     * @return The corresponding [ProductBatch] domain model.
     * @throws IOException if the network call fails and the batch is not found in the cache.
     */
    override suspend fun getProductBatch(id: String): ProductBatch =
        try {
            api.getProductBatch(id).toDomain()
        } catch (e: IOException) {
            val cachedData = preferences.getProductBatchCache()
            val cachedBatch = cachedData?.productBatchList?.find { it.idInventario == id }
            if (cachedBatch != null) {
                cachedBatch
            } else {
                throw e
            }
        }

    /**
     * Creates a new product batch entry via the API and clears the local cache.
     *
     * @param batch The [ProductBatch] object containing the data for the new batch.
     */
    override suspend fun registerProductBatch(batch: ProductBatch) {
        val dto = batch.toRegisterDto()
        api.addProductBatch(dto)
        preferences.clearCache()
    }

    /**
     * Modifies an existing product batch via the API and clears the local cache.
     *
     * @param batch The [ProductBatch] object containing the updated information.
     * @param id The unique ID of the product batch to modify.
     */
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

    /**
     * Searches for product batches on the remote server that match a given search term.
     * This operation does not use the cache.
     *
     * @param term The string to search for.
     * @return A list of matching [ProductBatch] domain models.
     */
    override suspend fun searchProductBatch(term: String): List<ProductBatch> = api.searchProductBatch(term).map { it.toDomain() }

    /**
     * Filters the list of product batches on the remote server based on a set of criteria.
     * This operation does not use the cache.
     *
     * @param filters A [FilterDto] object containing the selected filter options.
     * @return A filtered list of [ProductBatch] domain models.
     */
    override suspend fun filterProductBatch(filters: FilterDto): List<ProductBatch> = api.filterProductBatch(filters).map { it.toDomain() }

    /**
     * Deletes a product batch via the API and clears the local cache.
     *
     * @param id The unique ID of the product batch to delete.
     */
    override suspend fun deleteProductBatch(id: String) {
        api.deleteProductBatch(id)
        preferences.clearCache()
    }
}
