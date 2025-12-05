package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for managing the logic of deleting an existing product.
 * It validates the input product ID and delegates the deletion operation to the [ProductRepository].
 *
 * @property repository The repository that handles the actual data operation (deletion).
 */
class DeleteProductUseCase @Inject constructor(
    private val repository: ProductRepository,
) {

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
