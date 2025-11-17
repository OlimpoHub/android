package com.app.arcabyolimpo.domain.repository.product

import com.app.arcabyolimpo.domain.model.product.ProductAdd
import com.app.arcabyolimpo.domain.model.product.Product
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
    suspend fun addProduct(
        product: ProductAdd
    ): Result<Unit>

    suspend fun deleteProduct(id: String)

    /**
     * Retrieves detailed information for a specific product by its ID.
     *
     * @param id The unique identifier of the product to retrieve.
     * @return A [Result] containing the [Product] domain model if successful.
     */
    suspend fun getProduct(id: String): Result<Product>

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