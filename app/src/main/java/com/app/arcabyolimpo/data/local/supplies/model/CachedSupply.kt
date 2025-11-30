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
 * Wrapper for the cached supplies list with metadata.
 */
data class CachedSuppliesData(
    val supplies: List<CachedSupply>,
    val cacheTimestamp: Long
)