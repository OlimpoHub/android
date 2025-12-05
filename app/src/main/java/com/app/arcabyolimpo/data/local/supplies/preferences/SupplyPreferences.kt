package com.app.arcabyolimpo.data.local.supplies.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.app.arcabyolimpo.data.local.supplies.model.CachedSupply
import com.app.arcabyolimpo.data.local.supplies.model.CachedSupplyDetail
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

/**
 * Preferences for caching supplies.
 * Controlls the way the data is stored wether online or local
 */
class SupplyPreferences @Inject constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        SupplyConstants.PREFS_NAME,
        Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        private const val TAG = "SupplyPreferences"
    }

    /**
     * Saves a list of supplies to cache.
     */
    fun saveSuppliesList(supplies: List<CachedSupply>) {
        try {
            val json = gson.toJson(supplies)
            val timestamp = System.currentTimeMillis()

            prefs.edit().apply {
                putString(SupplyConstants.KEY_SUPPLIES_LIST, json)
                putLong(SupplyConstants.KEY_LAST_CACHE_TIME, timestamp)
                apply()
            }

            Log.d(TAG, "Successfully cached ${supplies.size} supplies at timestamp: $timestamp")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving supplies to cache", e)
        }
    }

    /**
     * Retrieves a list of cached supplies.
     *
     * @return List of cached supplies or null if not found
     */
    fun getSuppliesList(): List<CachedSupply>? {
        val json = prefs.getString(SupplyConstants.KEY_SUPPLIES_LIST, null)

        if (json.isNullOrEmpty()) {
            Log.d(TAG, "No cached supplies found")
            return null
        }

        return try {
            val type = object : TypeToken<List<CachedSupply>>() {}.type
            val supplies: List<CachedSupply> = gson.fromJson(json, type)
            Log.d(TAG, "Retrieved ${supplies.size} cached supplies")
            supplies
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "Error parsing cached supplies JSON", e)
            clearCache()
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error retrieving cached supplies", e)
            null
        }
    }

    // NEW METHODS FOR INDIVIDUAL SUPPLY DETAILS

    /**
     * Saves a single supply detail to cache.
     *
     * @param supplyDetail The supply detail to save
     */
    fun saveSupplyDetail(supplyDetail: CachedSupplyDetail) {
        try {
            val json = gson.toJson(supplyDetail)
            val timestamp = System.currentTimeMillis()
            val key = SupplyConstants.KEY_SUPPLY_DETAIL_PREFIX + supplyDetail.id
            val timeKey = SupplyConstants.KEY_SUPPLY_DETAIL_TIME_PREFIX + supplyDetail.id

            prefs.edit().apply {
                putString(key, json)
                putLong(timeKey, timestamp)
                apply()
            }

            Log.d(TAG, "Successfully cached supply detail for ID: ${supplyDetail.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving supply detail to cache", e)
        }
    }

    /**
     * Retrieves a single supply detail from cache.
     *
     * @param supplyId The ID of the supply to retrieve
     * @return The cached supply detail or null if not found
     */
    fun getSupplyDetail(supplyId: String): CachedSupplyDetail? {
        val key = SupplyConstants.KEY_SUPPLY_DETAIL_PREFIX + supplyId
        val json = prefs.getString(key, null)

        if (json.isNullOrEmpty()) {
            Log.d(TAG, "No cached detail found for supply ID: $supplyId")
            return null
        }

        return try {
            val detail: CachedSupplyDetail = gson.fromJson(json, CachedSupplyDetail::class.java)
            Log.d(TAG, "Retrieved cached detail for supply ID: $supplyId")
            detail
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "Error parsing cached supply detail JSON for ID: $supplyId", e)
            clearSupplyDetail(supplyId)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error retrieving cached supply detail", e)
            null
        }
    }

    /**
     * Checks if a specific supply detail cache is valid.
     *
     * @param supplyId The ID of the supply to check
     * @return true if the cache is valid, false otherwise
     */
    fun isSupplyDetailCacheValid(supplyId: String): Boolean {
        val timeKey = SupplyConstants.KEY_SUPPLY_DETAIL_TIME_PREFIX + supplyId
        val lastCacheTime = prefs.getLong(timeKey, 0L)

        if (lastCacheTime == 0L) {
            Log.d(TAG, "Supply detail cache invalid: no timestamp for ID: $supplyId")
            return false
        }

        val currentTime = System.currentTimeMillis()
        val cacheAge = currentTime - lastCacheTime
        val isValid = cacheAge < SupplyConstants.CACHE_VALIDITY_DURATION

        Log.d(TAG, "Supply detail cache validity for ID $supplyId: age=${cacheAge}ms, valid=$isValid")
        return isValid
    }

    /**
     * Checks if a specific supply detail is cached.
     *
     * @param supplyId The ID of the supply to check
     * @return true if the supply detail is cached, false otherwise
     */
    fun hasSupplyDetailCache(supplyId: String): Boolean {
        val key = SupplyConstants.KEY_SUPPLY_DETAIL_PREFIX + supplyId
        val timeKey = SupplyConstants.KEY_SUPPLY_DETAIL_TIME_PREFIX + supplyId
        return prefs.contains(key) && prefs.contains(timeKey)
    }

    /**
     * Clears a specific supply detail from cache.
     *
     * @param supplyId The ID of the supply to clear
     */
    fun clearSupplyDetail(supplyId: String) {
        val key = SupplyConstants.KEY_SUPPLY_DETAIL_PREFIX + supplyId
        val timeKey = SupplyConstants.KEY_SUPPLY_DETAIL_TIME_PREFIX + supplyId
        prefs.edit().apply {
            remove(key)
            remove(timeKey)
            apply()
        }
        Log.d(TAG, "Cleared cache for supply ID: $supplyId")
    }

    /**
     * Saves the timestamp when the cache was last updated.
     *
     * @param timestamp The timestamp to save
     */
    fun getLastCacheTime(): Long {
        return prefs.getLong(SupplyConstants.KEY_LAST_CACHE_TIME, 0L)
    }

    /**
     * Checks if the cache is still valid.
     *
     * @return true if the cache is valid, false otherwise
     */
    fun isCacheValid(): Boolean {
        val lastCacheTime = getLastCacheTime()
        if (lastCacheTime == 0L) {
            Log.d(TAG, "Cache invalid: no timestamp found")
            return false
        }

        val currentTime = System.currentTimeMillis()
        val cacheAge = currentTime - lastCacheTime
        val isValid = cacheAge < SupplyConstants.CACHE_VALIDITY_DURATION

        Log.d(TAG, "Cache validity check: age=${cacheAge}ms, maxAge=${SupplyConstants.CACHE_VALIDITY_DURATION}ms, valid=$isValid")
        return isValid
    }

    /**
     * Checks if the cache contains data.
     *
     * @return true if the cache contains data, false otherwise
     */
    fun hasCachedData(): Boolean {
        val hasTimestamp = getLastCacheTime() > 0L
        val hasList = getSuppliesList() != null
        val hasData = hasTimestamp && hasList

        Log.d(TAG, "Cache existence check: hasTimestamp=$hasTimestamp, hasList=$hasList, result=$hasData")
        return hasData
    }

    /**
     * Deletes the cache.
     */
    fun clearCache() {
        prefs.edit().clear().apply()
        Log.d(TAG, "Cache cleared")
    }

    /**
     * Returns the cache duration
     *
     * @return time of the cache
     */
    fun getCacheAge(): Long {
        val lastCacheTime = getLastCacheTime()
        if (lastCacheTime == 0L) return -1L
        return System.currentTimeMillis() - lastCacheTime
    }
}