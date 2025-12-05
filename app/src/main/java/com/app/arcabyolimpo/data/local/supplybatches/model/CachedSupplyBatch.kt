package com.app.arcabyolimpo.data.local.supplybatches.model

import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchItem

/**
 * Represents the cached data structure for a list of supply batches.
 *
 * This data class is used to bundle the retrieved list of [SupplyBatchItem]
 * with the timestamp of when it was last updated in the cache.
 *
 * @property supplyBatchesList The list of supply batch items retrieved from the cache.
 * @property lastUpdate The timestamp (in milliseconds) of when the data was last saved.
 */
data class CachedSupplyBatch(
    val supplyBatchesList: List<SupplyBatchItem>,
    val lastUpdate: Long,
)
