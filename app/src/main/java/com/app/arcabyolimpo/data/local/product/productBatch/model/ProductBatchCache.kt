package com.app.arcabyolimpo.data.local.product.productBatch.model

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch

/**
 * Cached representation of product batches.
 * Stores a list of batches and the timestamp of the last update.
 *
 * Used to keep local offline access to product batch information.
 */
data class ProductBatchCache(
    val productBatchList: List<ProductBatch>,
    val lastUpdate: Long,
)
