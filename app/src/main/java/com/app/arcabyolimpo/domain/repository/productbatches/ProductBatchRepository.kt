package com.app.arcabyolimpo.domain.repository.productbatches

import com.app.arcabyolimpo.data.mapper.productbatches.toDto
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch

interface ProductBatchRepository {
    /**
     * Retrieves a list of all product batches.
     *
     * @return A list of [ProductBatch] domain models.
     */
    suspend fun getProductBatches(): List<ProductBatch>

    /**
     * Retrieves a single product batch by its unique identifier.
     *
     * @param id The unique ID of the product batch to fetch.
     * @return The corresponding [ProductBatch] domain model.
     */
    suspend fun getProductBatch(id: String): ProductBatch

    /**
     * Creates a new product batch entry.
     *
     * @param batch The [ProductBatch] object containing the data for the new batch.
     */
    suspend fun registerProductBatch(batch: ProductBatch)

    /**
     * Modifies an existing product batch.
     *
     * @param batch The [ProductBatch] object containing the updated information.
     * @param id The unique ID of the product batch to modify.
     */
    suspend fun modifyProductBatch(
        batch: ProductBatch,
        id: String,
    )

    /**
     * Searches for product batches that match a given search term.
     *
     * @param term The string to search for.
     * @return A list of matching [ProductBatch] domain models.
     */
    suspend fun searchProductBatch(term: String): List<ProductBatch>

    /**
     * Filters the list of product batches based on a set of criteria.
     *
     * @param filters A [FilterDto] object containing the selected filter options.
     * @return A filtered list of [ProductBatch] domain models.
     */
    suspend fun filterProductBatch(filters: FilterDto): List<ProductBatch>

    /**
     * Deletes a product batch by its unique identifier.
     *
     * @param id The unique ID of the product batch to delete.
     */
    suspend fun deleteProductBatch(id: String)
}
