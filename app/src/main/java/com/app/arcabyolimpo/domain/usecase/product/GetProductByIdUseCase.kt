package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case responsible for obtaining a single product by its identifier.
 *
 * This class delegates the fetch operation to [ProductRepository] and exposes
 * the result as a [Flow] of [Result], so the presentation layer can observe
 * loading, success and error states when requesting a specific [Product].
 *
 * @property repository Repository that provides access to product information.
 */
class GetProductByIdUseCase @Inject constructor(
    private val repository: ProductRepository,
) {

    /**
     * Executes the use case to retrieve a product with the given [productId].
     *
     * @param productId Unique identifier of the product to be retrieved.
     * @return A [Flow] that emits a [Result] wrapping the requested [Product].
     */
    operator fun invoke(productId: String): Flow<Result<Product>> {
        return repository.getProductById(productId)
    }
}
