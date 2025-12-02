package com.app.arcabyolimpo.data.local.product.detail.model

import com.app.arcabyolimpo.domain.model.product.Product

/**
 * Represents a cached product detail entry.
 *
 * @property productDetail The cached product information.
 * @property lastUpdate The timestamp (in milliseconds) when the product detail was last stored.
 */
data class ProductDetailCache(
    val productDetail: Product,
    val lastUpdate: Long
)