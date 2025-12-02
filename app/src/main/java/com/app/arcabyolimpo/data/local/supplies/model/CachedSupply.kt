package com.app.arcabyolimpo.data.local.supplies.model

/**
 * Cached version of Supply model for offline storage.
 * Simplified version containing only the data needed for the list view.
 */
data class CachedSupply(
    val id: String,
    val name: String,
    val imageUrl: String?
)

/**
 * Detailed cached supply with all information including batches.
 */
data class CachedSupplyDetail(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val unitMeasure: String,
    val totalQuantity: Int,
    val workshop: String,
    val category: String,
    val status: Int,
    val batches: List<CachedBatch>
)

/**
 * Cached batch information - matches your Batch model.
 */
data class CachedBatch(
    val id: String,
    val quantity: Int,
    val expirationDate: String,
    val adquisitionType: String
)

/**
 * Wrapper for the cached supplies list with metadata.
 */
data class CachedSuppliesData(
    val supplies: List<CachedSupply>,
    val cacheTimestamp: Long
)