package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.model.product.ProductAdd
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import javax.inject.Inject

/**
 * Use case responsible for managing the logic of adding a new product.
 * It validates the input data and delegates the storage operation to the [ProductRepository].
 *
 * @property repository The repository that handles the actual data operation.
 */
class AddProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        product: ProductAdd,
    ): Result<Unit> {
        if (product.name.isBlank() || product.idWorkshop.isBlank() || product.idCategory.isBlank()) {
            return Result.failure<Unit>(Exception("Completa todos los campos"))
        }
        return repository.addProduct(product)
    }
}