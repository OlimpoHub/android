package com.app.arcabyolimpo.domain.usecase.product

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductsListUseCase @Inject constructor(
    private val repository: ProductRepository,
) {
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