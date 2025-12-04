package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for retrieving the list of products.
 *
 * This use case encapsulates the logic to request all products from the
 * [ProductRepository] and exposes the result as a [Flow] of [Result],
 * so the UI layer can easily react to loading, success, or error states.
 *
 * @property repository Repository that provides access to product data from
 *                      the corresponding data sources (remote/local).
 */
class GetProductsListUseCase @Inject constructor(
    private val repository: ProductRepository,
) {

    /**
     * Invokes the process to fetch the complete list of products.
     *
     * The returned [Flow] emits:
     * - [Result.Loading] while the data is being fetched.
     * - [Result.Success] with the list of [Product] on a successful response.
     * - [Result.Error] if an exception occurs during the retrieval.
     */
    operator fun invoke(): Flow<Result<List<Product>>> =
        flow {
            try {
                emit(Result.Loading)
                val products = repository.getProducts()
                emit(Result.Success(products))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
