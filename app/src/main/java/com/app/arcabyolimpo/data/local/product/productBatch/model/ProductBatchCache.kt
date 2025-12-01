package com.app.arcabyolimpo.data.local.product.productBatch.model

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch

data class ProductBatchCache(
    val productBatchList: List<ProductBatch>,
    val lastUpdate: Long,
)