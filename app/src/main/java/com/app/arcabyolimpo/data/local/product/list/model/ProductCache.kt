package com.app.arcabyolimpo.data.local.product.list.model

import com.app.arcabyolimpo.domain.model.product.Product

/**
 * Represents a cached snapshot of the product list stored locally.
 *
 * This model is used by [ProductPreferences] to encapsulate both the cached
 * list of products and the timestamp indicating when the cache was last updated.
 *
 * @property productList The list of cached [Product] items retrieved from the API.
 * @property lastUpdate The timestamp (in milliseconds) indicating when the cache
 *                      was last refreshed.
 */
data class ProductCache(
    val productList: List<Product>,
    val lastUpdate: Long,
)