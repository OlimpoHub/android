package com.app.arcabyolimpo.data.local.supplies.preferences

import com.app.arcabyolimpo.data.local.supplies.model.CachedSupply


import com.app.arcabyolimpo.domain.model.supplies.Supply
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local data source for supply operations.
 * Handles reading and writing supply data to local cache.
 */
@Singleton
class SupplyLocalDataSource @Inject constructor(
    private val preferences: SupplyPreferences
) {
    /**
     * Caches the supplies list locally.
     * Converts domain models to cached models.
     */
    fun cacheSuppliesList(supplies: List<Supply>) {
        val cachedSupplies = supplies.map { supply ->
            CachedSupply(
                id = supply.id,
                name = supply.name,
                imageUrl = supply.imageUrl
            )
        }
        preferences.saveSuppliesList(cachedSupplies)
    }

    /**
     * Retrieves cached supplies and converts them to domain models.
     * Returns null if no cache exists.
     */
    fun getCachedSuppliesList(): List<Supply>? {
        val cachedSupplies = preferences.getSuppliesList() ?: return null
        return cachedSupplies.map { cached ->
            Supply(
                id = cached.id,
                name = cached.name,
                imageUrl = cached.imageUrl,
                unitMeasure = "",
                batch = emptyList()
            )
        }
    }

    /**
     * Checks if cached data is still valid.
     */
    fun isCacheValid(): Boolean = preferences.isCacheValid()

    /**
     * Checks if any cached data exists.
     */
    fun hasCachedData(): Boolean = preferences.hasCachedData()

    /**
     * Clears all cached supply data.
     */
    fun clearCache() = preferences.clearCache()

    /**
     * Gets the age of the cache in milliseconds.
     */
    fun getCacheAge(): Long = preferences.getCacheAge()
}