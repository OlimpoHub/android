package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.product.ProductDetail
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for retrieving comprehensive details of a specific product by its unique identifier.
 *
 * This use case encapsulates the business logic for fetching detailed product information,
 * orchestrating the repository call and wrapping the operation in a reactive [Flow] that
 * emits [Result] states. Unlike the simpler product retrieval use case, this fetches
 * enriched product data including workshop names, category descriptions, and complete
 * product specifications suitable for detailed product views or edit forms.
 *
 * The use case handles the conversion from Kotlin's standard Result type (returned by
 * the repository) to the application's custom Result type, ensuring consistent error
 * handling patterns throughout the domain layer and providing UI layers with observable
 * state changes.
 *
 * @property repository The [ProductRepository] instance used to fetch detailed product data.
 * @constructor Creates a use case instance with the provided repository via dependency injection.
 */
class GetProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    /**
     * Executes the use case to fetch detailed product information by ID, emitting result states as a [Flow].
     *
     * This operator function enables the use case to be invoked like a regular function.
     * It creates a cold Flow that emits states throughout the detailed product fetch operation:
     * 1. [Result.Loading] - Emitted immediately to indicate the fetch has started
     * 2. [Result.Success] - Emitted with a [ProductDetail] containing comprehensive product
     *    information including workshop name, category description, and full specifications
     * 3. [Result.Error] - Emitted with the exception if the fetch fails due to the product
     *    not being found, network issues, or other data source problems
     *
     * The method uses Kotlin's fold operation to convert the repository's standard Result
     * into the application's custom Result type, maintaining consistent error handling
     * patterns while preserving all error context.
     *
     * The Flow approach allows observers to react to each state change, enabling responsive
     * UI updates such as showing detailed loading screens during the fetch, displaying the
     * complete product information with rich formatting upon success, or presenting
     * appropriate error messages when the product cannot be retrieved.
     *
     * @param id The unique identifier of the product to retrieve detailed information for.
     * @return A [Flow] that emits [Result] states wrapping the [ProductDetail] or error information.
     */
    operator fun invoke(id: String): Flow<Result<ProductDetail>> =
        flow {
            emit(Result.Loading)
            val productDetail = repository.getProduct(id)
            productDetail.fold(
                onSuccess = { productDetail ->
                    emit(Result.Success(productDetail))
                },
                onFailure = { throwable ->
                    emit(Result.Error(throwable))
                }
            )
        }

}