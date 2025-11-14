package com.app.arcabyolimpo.domain.repository.product

import com.app.arcabyolimpo.domain.model.product.ProductAdd

interface ProductRepository {
    suspend fun addProduct(
        product: ProductAdd
    ): Result<Unit>
}