package com.app.arcabyolimpo.data.local.supplybatches.model

import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchItem

/**
 * Cached version of Supply batch for offline storage.
 *
 * @param supplyBatchesList The list of cached supply batches.
 * @param lastUpdate The timestamp when the cache was last updated.
 */
data class CachedSupplyBatch(
    val supplyBatchesList: List<SupplyBatchItem>,
    val lastUpdate: Long,
)
