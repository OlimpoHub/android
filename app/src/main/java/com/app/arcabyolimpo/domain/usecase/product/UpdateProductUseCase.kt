package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.model.product.ProductUpdate
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    /**
     * Executes the use case to update an existing product.
     * (Requires a new updateProduct function in ProductRepository)
     *
     * @param id The ID of the product to update.
     * @param productData The data to be sent for modification.
     * @return A [Result] indicating success or failure.
     */
    suspend operator fun invoke(id: String, productData: ProductUpdate): Result<Unit> {
        return repository.updateProduct(id, productData)
    }
}