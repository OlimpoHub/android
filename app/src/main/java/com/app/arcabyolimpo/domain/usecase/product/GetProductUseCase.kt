package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.domain.model.product.ProductAdd
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    /**
     * Executes the use case to fetch a detailed product by its ID.
     *
     * @param id The unique identifier of the product.
     * @return A [Result] containing the [Product] domain model.
     */
    suspend operator fun invoke(id: String): Result<Product> {
        return repository.getProduct(id)
    }
}