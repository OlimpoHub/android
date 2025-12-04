package com.app.arcabyolimpo.data.local.supplies.model

/**
 * Cached version of Supply model for offline storage.
 * Simplified version containing only the data needed for the list view.
 *
 * @param id The unique identifier of the supply.
 * @param name The name of the supply.
 * @param imageUrl The URL or path to the supply's image.
 */
data class CachedSupply(
    val id: String,
    val name: String,
    val imageUrl: String?
)

/**
 * Detailed cached supply with all information including batches.
 *
 * @param id The unique identifier of the supply.
 * @param name The name of the supply.
 * @param imageUrl The URL or path to the supply's image.
 * @param unitMeasure The unit of measurement used for the supply (e.g., "kg", "pcs").
 * @param totalQuantity The total quantity of the supply across all batches.
 * @param workshop The workshop associated with the supply.
 * @param category The category of the supply.
 * @param status The status of the supply (e.g., "Disponible", "Caducado").
 * @param batches A list of [CachedBatch] objects representing different batches of the supply.
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
 *
 * @param id The unique identifier of the batch.
 * @param quantity The quantity of the batch.
 * @param expirationDate The expiration date of the batch.
 * @param adquisitionType The type of acquisition for the batch.
 */
data class CachedBatch(
    val id: String,
    val quantity: Int,
    val expirationDate: String,
    val adquisitionType: String
)

/**
 * Wrapper for the cached supplies list with metadata.
 *
 * @param supplies The list of cached supplies.
 * @param cacheTimestamp The timestamp when the cache was last updated.
 */
data class CachedSuppliesData(
    val supplies: List<CachedSupply>,
    val cacheTimestamp: Long
)