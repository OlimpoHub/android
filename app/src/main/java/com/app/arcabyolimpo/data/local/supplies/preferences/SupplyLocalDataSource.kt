package com.app.arcabyolimpo.data.local.supplies.preferences

import com.app.arcabyolimpo.data.local.supplies.model.CachedSupply
import com.app.arcabyolimpo.data.local.supplies.model.CachedSupplyDetail
import com.app.arcabyolimpo.data.local.supplies.model.CachedBatch
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt
import com.app.arcabyolimpo.domain.model.supplies.Batch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local data source for caching supplies.
 */
@Singleton
class SupplyLocalDataSource @Inject constructor(
    private val preferences: SupplyPreferences
) {
    // ============ SUPPLIES LIST  ============

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

    // ============ SUPPLY DETAIL  ============

    /**
     * Caches a single supply detail with full information.
     * Uses SupplyBatchExt which has all the detail fields.
     */
    fun cacheSupplyDetail(supply: SupplyBatchExt) {
        val cachedDetail = CachedSupplyDetail(
            id = supply.id,
            name = supply.name,
            imageUrl = supply.imageUrl,
            unitMeasure = supply.unitMeasure,
            totalQuantity = supply.totalQuantity,
            workshop = supply.workshop,
            category = supply.category,
            status = supply.status,
            batches = supply.batch.map { batch ->
                CachedBatch(
                    id = batch.id,
                    quantity = batch.quantity,
                    expirationDate = batch.expirationDate,
                    adquisitionType = batch.adquisitionType
                )
            }
        )
        preferences.saveSupplyDetail(cachedDetail)
    }

    /**
     * Retrieves cached supply detail and converts to SupplyBatchExt.
     */
    fun getCachedSupplyDetail(supplyId: String): SupplyBatchExt? {
        val cachedDetail = preferences.getSupplyDetail(supplyId) ?: return null

        return SupplyBatchExt(
            id = cachedDetail.id,
            name = cachedDetail.name,
            imageUrl = cachedDetail.imageUrl,
            unitMeasure = cachedDetail.unitMeasure,
            totalQuantity = cachedDetail.totalQuantity,
            workshop = cachedDetail.workshop,
            category = cachedDetail.category,
            status = cachedDetail.status,
            batch = cachedDetail.batches.map { cachedBatch ->
                Batch(
                    id = cachedBatch.id,
                    quantity = cachedBatch.quantity,
                    expirationDate = cachedBatch.expirationDate,
                    adquisitionType = cachedBatch.adquisitionType
                )
            }
        )
    }

    fun isSupplyDetailCacheValid(supplyId: String): Boolean =
        preferences.isSupplyDetailCacheValid(supplyId)

    fun hasSupplyDetailCache(supplyId: String): Boolean =
        preferences.hasSupplyDetailCache(supplyId)

    fun clearSupplyDetail(supplyId: String) =
        preferences.clearSupplyDetail(supplyId)

    // ============ GENERAL CACHE METHODS ============

    fun isCacheValid(): Boolean = preferences.isCacheValid()
    fun hasCachedData(): Boolean = preferences.hasCachedData()
    fun clearCache() = preferences.clearCache()
    fun getCacheAge(): Long = preferences.getCacheAge()
}