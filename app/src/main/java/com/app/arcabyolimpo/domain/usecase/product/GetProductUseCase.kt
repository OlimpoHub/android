package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.product.ProductDetail
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    /**
     * Executes the use case to fetch a detailed product by its ID.
     *
     * @param id The unique identifier of the product.
     * @return A [Result] containing the [Product] domain model.
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