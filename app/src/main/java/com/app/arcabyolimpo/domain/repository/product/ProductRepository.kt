package com.app.arcabyolimpo.domain.repository.product

import com.app.arcabyolimpo.domain.model.product.ProductAdd
import com.app.arcabyolimpo.domain.model.product.Product
import kotlinx.coroutines.flow.Flow
import com.app.arcabyolimpo.domain.model.product.ProductDetail
import com.app.arcabyolimpo.domain.model.product.ProductUpdate

/**
 * Interface defining the contract for managing product-related data operations.
 *
 * This repository acts as a clean boundary between the domain layer and the API.
 */
interface ProductRepository {
    /**
     * Adds a new product to the underlying API
     *
     * @param product The [ProductAdd] object containing the necessary details of the new product.
     * @return A [Result] indicating success or failure.
     */
    suspend fun addProduct(product: ProductAdd): Result<Unit>

    /**
     * Retrieves all products from the data source.
     *
     * This method fetches the complete list of products available in the system,
     * returning them as domain models ready for business logic processing and
     * UI presentation. The products are returned in their basic form without
     * enriched details, making this method suitable for product listings and
     * overview screens.
     *
     * @return A [List] of [Product] domain models representing all available products.
     * @throws Exception if the data retrieval fails due to network, database, or other errors.
     */
    suspend fun getProducts(): List<Product>

    /**
     * Deletes an existing product by its unique identifier.
     *
     * @param id The unique identifier of the product to delete.
     * @return [Unit] implicitly if the operation is successful.
     */
    suspend fun deleteProduct(id: String)

    /**
     * Retrieves a specific product by its unique identifier, wrapped in a [Flow] of [Result].
     *
     * This method queries the data source for a product matching the provided ID,
     * returning it as a reactive Flow that emits Result states. The Flow pattern
     * allows observers to react to loading, success, and error states, enabling
     * responsive UI updates throughout the fetch operation.
     *
     * Unlike the synchronous variant, this method is particularly useful when the
     * UI needs to observe the operation's progress and handle intermediate states
     * gracefully without blocking.
     *
     * @param productId The unique identifier of the product to retrieve.
     * @return A [Flow] that emits [Result] states wrapping the [Product] or error information.
     */
    fun getProductById(productId: String): Flow<com.app.arcabyolimpo.domain.common.Result<Product>>

    /**
     * Searches for products whose details match a given query string.
     *
     * @param query The text string used for the search.
     * @return A [List] of [Product] objects that match the query.
     */
    suspend fun searchProducts(query: String): List<Product>

    /**
     * Retrieves detailed information for a specific product by its ID.
     *
     * @param id The unique identifier of the product to retrieve.
     * @return A [Result] containing the [Product] domain model if successful.
     */
    suspend fun getProduct(id: String): Result<ProductDetail>

    /**
     * Updates an existing product's details and optionally its image.
     *
     * @param idProduct The unique identifier of the product to update (passed in the URL path).
     * @param product The [ProductUpdate] object containing the new details.
     * @return A [Result] indicating whether the operation was successful or if an error occurred.
     */
    suspend fun updateProduct(
        idProduct: String,
        product: ProductUpdate
    ): Result<Unit>
}