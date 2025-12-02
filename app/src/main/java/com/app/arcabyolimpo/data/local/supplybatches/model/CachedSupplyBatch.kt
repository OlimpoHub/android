package com.app.arcabyolimpo.data.local.supplybatches.model

import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchItem

data class CachedSupplyBatch(
    val supplyBatchesList: List<SupplyBatchItem>,
    val lastUpdate: Long,
)
