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
         * Generates a unique key for each supply batch list based on expirationDate and idSupply
         */
        private fun generateKey(
            expirationDate: String,
            idSupply: String,
        ): String = "${expirationDate}_$idSupply"

        /**
         * Saves a supply batch list for a specific expirationDate and idSupply combination
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
         * Retrieves cached supply batch list for a specific expirationDate and idSupply
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

        fun isCacheValid(
            expirationDate: String,
            idSupply: String,
        ): Boolean {
            val key = generateKey(expirationDate, idSupply)
            val lastUpdate = prefs.getLong("${SupplyBatchesConstants.KEY_LAST_UPDATE}_$key", 0)
            return System.currentTimeMillis() - lastUpdate < SupplyBatchesConstants.CACHE_DURATION
        }

        fun clearCache() {
            prefs.edit { clear() }
        }
    }
