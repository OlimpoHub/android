package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a specific product by its unique identifier.
 *
 * This use case encapsulates the business logic for fetching a single product,
 * delegating the repository call and exposing the operation as a reactive [Flow]
 * that emits [Result] states. This pattern enables UI layers to observe loading,
 * success, and error states throughout the product retrieval operation.
 *
 * Unlike more complex use cases, this acts as a simple pass-through to the repository,
 * following the repository pattern where the repository already handles the Result
 * wrapping. This design maintains consistency in the architecture and provides a
 * clear boundary between domain and data layers.
 *
 * @property repository The [ProductRepository] instance used to fetch product data.
 * @constructor Creates a use case instance with the provided repository via dependency injection.
 */
class GetProductByIdUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    /**
     * Executes the use case to retrieve a product by its ID as a [Flow] of [Result] states.
     *
     * This operator function enables the use case to be invoked like a regular function,
     * delegating directly to the repository which handles the emission of loading, success,
     * and error states. The repository's Flow emits:
     * - [Result.Loading] when the fetch operation begins
     * - [Result.Success] with the [Product] if found successfully
     * - [Result.Error] with the exception if the operation fails
     *
     * The delegation pattern used here allows the repository to manage the Flow
     * lifecycle while keeping the use case layer focused on business logic coordination.
     *
     * @param productId The unique identifier of the product to retrieve.
     * @return A [Flow] that emits [Result] states wrapping the [Product] or error information.
     */
    operator fun invoke(productId: String): Flow<Result<Product>> {
        return repository.getProductById(productId)
    }
}