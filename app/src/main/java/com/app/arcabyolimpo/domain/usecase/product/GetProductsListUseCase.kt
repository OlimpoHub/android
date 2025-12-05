package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for retrieving the complete list of products from the system.
 *
 * This use case encapsulates the business logic for fetching all available products,
 * orchestrating the repository call and wrapping the operation in a reactive [Flow]
 * that emits [Result] states. This pattern enables UI layers to observe and react
 * to loading, success, and error states throughout the product list retrieval,
 * providing responsive feedback during potentially long-running operations.
 *
 * The use case is particularly useful for product listing screens, catalog views,
 * or any interface requiring a complete overview of available products. It follows
 * the single responsibility principle by focusing solely on the product list
 * retrieval operation.
 *
 * @property repository The [ProductRepository] instance used to fetch product data.
 * @constructor Creates a use case instance with the provided repository via dependency injection.
 */
class GetProductsListUseCase @Inject constructor(
    private val repository: ProductRepository,
) {
    /**
     * Executes the use case to retrieve all products, emitting result states as a [Flow].
     *
     * This operator function enables the use case to be invoked like a regular function.
     * It creates a cold Flow that emits three possible states during the fetch operation:
     * 1. [Result.Loading] - Emitted immediately to indicate the fetch has started
     * 2. [Result.Success] - Emitted with a [List] of [Product] models if the operation succeeds
     * 3. [Result.Error] - Emitted with the exception if the fetch fails due to network issues,
     *    database errors, or other data source problems
     *
     * The Flow approach allows observers to react to each state change, enabling
     * responsive UI updates such as showing loading indicators during the fetch,
     * displaying the product list upon success with smooth transitions, or presenting
     * error messages with retry options when failures occur.
     *
     * @return A [Flow] that emits [Result] states wrapping a [List] of [Product] or error information.
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