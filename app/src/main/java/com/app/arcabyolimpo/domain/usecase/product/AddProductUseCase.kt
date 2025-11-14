package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.model.product.ProductAdd
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import javax.inject.Inject
/** ---------------------------------------------------------------------------------------------- *
 * AddSupplyUseCase -> Use case that receives the Supply and its image to send it to the db
 *
 * @param repository: SupplyRepository -> repository where the api calls is found
 * ---------------------------------------------------------------------------------------------- */
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