package com.app.arcabyolimpo.data.local.supplybatches.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.app.arcabyolimpo.data.local.supplybatches.model.CachedSupplyBatch
import com.app.arcabyolimpo.data.local.supplybatches.preferences.SupplyBatchesConstants.KEY_BATCHES_LIST
import com.app.arcabyolimpo.data.local.supplybatches.preferences.SupplyBatchesConstants.KEY_LAST_UPDATE
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the caching of supply batch lists in SharedPreferences.
 *
 * This class provides a mechanism to store, retrieve, and validate lists of supply batches
 * locally. It uses a combination of an expiration date and a supply ID to create unique
 * keys for each cached list, allowing for multiple distinct lists to be stored.
 * Data is serialized to JSON format using Gson.
 *
 * @param context The application context, used to access SharedPreferences.
 * @param gson The Gson instance used for serializing and deserializing the batch list.
 */
@Singleton
class SupplyBatchesPreferences
    @Inject
    constructor(
        @ApplicationContext context: Context,
        private val gson: Gson,
    ) {
        private val prefs: SharedPreferences =
            context.getSharedPreferences(
                SupplyBatchesConstants.PREF_NAME,
                Context.MODE_PRIVATE,
            )

        /**
         * Generates a unique storage key for a supply batch list based on its expiration date
         * and the ID of the supply it belongs to.
         *
         * @param expirationDate The expiration date associated with the batch list.
         * @param idSupply The unique identifier of the supply.
         * @return A compound key string for use with SharedPreferences.
         */
        private fun generateKey(
            expirationDate: String,
            idSupply: String,
        ): String = "${expirationDate}_$idSupply"

        /**
         * Saves a list of supply batches and a timestamp to SharedPreferences.
         *
         * The list is serialized to a JSON string and stored under a unique key generated
         * from the expiration date and supply ID. A timestamp of the save operation is also stored.
         *
         * @param expirationDate The expiration date for the batches being saved.
         * @param idSupply The ID of the supply for the batches being saved.
         * @param supplyBatchesList The list of [SupplyBatchItem] objects to cache.
         */
        fun saveSupplyBatchesList(
            expirationDate: String,
            idSupply: String,
            supplyBatchesList: List<SupplyBatchItem>,
        ) {
            val key = generateKey(expirationDate, idSupply)
            prefs
                .edit {
                    putString("${SupplyBatchesConstants.KEY_BATCHES_LIST}_$key", gson.toJson(supplyBatchesList))
                    putLong("${SupplyBatchesConstants.KEY_LAST_UPDATE}_$key", System.currentTimeMillis())
                }
        }

        /**
         * Retrieves a cached supply batch list for a specific expiration date and supply ID.
         *
         * @param expirationDate The expiration date of the desired batch list.
         * @param idSupply The ID of the supply for the desired batch list.
         * @return A [CachedSupplyBatch] object containing the list and its last update time,
         *         or null if no cache exists for the given key.
         */
        fun getSupplyBatchCache(
            expirationDate: String,
            idSupply: String,
        ): CachedSupplyBatch? {
            val key = generateKey(expirationDate, idSupply)
            val json = prefs.getString("${SupplyBatchesConstants.KEY_BATCHES_LIST}_$key", null)
            val lastUpdate = prefs.getLong("${SupplyBatchesConstants.KEY_LAST_UPDATE}_$key", 0)

            if (json.isNullOrEmpty() || lastUpdate == 0L) return null

            val type = object : TypeToken<List<SupplyBatchItem>>() {}.type
            val supplyBatchesList: List<SupplyBatchItem> = gson.fromJson(json, type)

            return CachedSupplyBatch(
                supplyBatchesList = supplyBatchesList,
                lastUpdate = lastUpdate,
            )
        }

        /**
         * Checks if the cache for a specific supply batch list is still valid.
         *
         * Validity is determined by comparing the time elapsed since the last update against
         * a predefined cache duration constant.
         *
         * @param expirationDate The expiration date of the cache to check.
         * @param idSupply The supply ID of the cache to check.
         * @return `true` if the cache is still valid, `false` otherwise.
         */
        fun isCacheValid(
            expirationDate: String,
            idSupply: String,
        ): Boolean {
            val key = generateKey(expirationDate, idSupply)
            val lastUpdate = prefs.getLong("${SupplyBatchesConstants.KEY_LAST_UPDATE}_$key", 0)
            return System.currentTimeMillis() - lastUpdate < SupplyBatchesConstants.CACHE_DURATION
        }

        /**
         * Clears all data stored in the supply batches preferences file.
         *
         * This is typically called after a write operation (create, modify, delete) to
         * invalidate all caches and ensure data consistency.
         */
        fun clearCache() {
            prefs.edit { clear() }
        }
    }
