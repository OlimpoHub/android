package com.app.arcabyolimpo.data.local.supplies.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.app.arcabyolimpo.data.local.supplies.model.CachedSupply
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

/**
 * Manages local storage for supply data using SharedPreferences.
 * Handles caching, retrieval, and cache validation.
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
     * Saves the supplies list to cache with the current timestamp.
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
     * Retrieves the cached supplies list.
     * Returns null if no cache exists or parsing fails.
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
            // Clear corrupted cache
            clearCache()
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error retrieving cached supplies", e)
            null
        }
    }

    /**
     * Gets the timestamp of when the cache was last updated.
     * Returns 0 if no cache exists.
     */
    fun getLastCacheTime(): Long {
        return prefs.getLong(SupplyConstants.KEY_LAST_CACHE_TIME, 0L)
    }

    /**
     * Checks if the cached data is still valid based on the configured duration.
     * @return true if cache is valid, false if expired or doesn't exist
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
     * Checks if cache exists (regardless of validity).
     */
    fun hasCachedData(): Boolean {
        val hasTimestamp = getLastCacheTime() > 0L
        val hasList = getSuppliesList() != null
        val hasData = hasTimestamp && hasList

        Log.d(TAG, "Cache existence check: hasTimestamp=$hasTimestamp, hasList=$hasList, result=$hasData")
        return hasData
    }

    /**
     * Clears all cached supply data.
     */
    fun clearCache() {
        prefs.edit().clear().apply()
        Log.d(TAG, "Cache cleared")
    }

    /**
     * Gets the age of the cache in milliseconds.
     * Returns -1 if no cache exists.
     */
    fun getCacheAge(): Long {
        val lastCacheTime = getLastCacheTime()
        if (lastCacheTime == 0L) return -1L
        return System.currentTimeMillis() - lastCacheTime
    }
}