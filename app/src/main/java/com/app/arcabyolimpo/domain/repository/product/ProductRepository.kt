package com.app.arcabyolimpo.domain.repository.product

import com.app.arcabyolimpo.domain.model.product.ProductAdd
import com.app.arcabyolimpo.domain.model.product.Product

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

    suspend fun getProducts(): List<Product>

    suspend fun searchProducts(query: String): List<Product>


}