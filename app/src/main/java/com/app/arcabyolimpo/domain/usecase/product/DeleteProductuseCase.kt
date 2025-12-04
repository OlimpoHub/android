package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for handling the deletion of a product.
 *
 * This use case encapsulates the logic to request the deletion of a product
 * from the [ProductRepository] and exposes the result as a [Flow] of [Result],
 * allowing the UI layer to react to loading, success or error states.
 *
 * @property repository Repository that performs the actual delete operation
 *                      against the data source (remote/local).
 */
class DeleteProductUseCase @Inject constructor(
    private val repository: ProductRepository,
) {

    /**
     * Invokes the delete product process for the given [idProduct].
     *
     * The returned [Flow] emits:
     * - [Result.Loading] while the operation is in progress.
     * - [Result.Success] when the product is successfully deleted.
     * - [Result.Error] if an exception occurs during the operation.
     *
     * @param idProduct Unique identifier of the product to be deleted.
     */
    operator fun invoke(idProduct: String): Flow<Result<Unit>> =
        flow {
            try {
                emit(Result.Loading)
                repository.deleteProduct(idProduct)
                emit(Result.Success(Unit))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
