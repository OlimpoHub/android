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

        fun saveSupplyBatchesList(supplyBatchesList: List<SupplyBatchItem>) {
            prefs
                .edit {
                    putString(KEY_BATCHES_LIST, gson.toJson(supplyBatchesList))
                        .putLong(KEY_LAST_UPDATE, System.currentTimeMillis())
                }
        }

        fun getSupplyBatchCache(): CachedSupplyBatch? {
            val json = prefs.getString(KEY_BATCHES_LIST, null)
            val lastUpdate = prefs.getLong(KEY_LAST_UPDATE, 0)

            if (json.isNullOrEmpty() || lastUpdate == 0L) return null

            val type = object : TypeToken<List<SupplyBatchItem>>() {}.type
            val supplyBatchesList: List<SupplyBatchItem> = gson.fromJson(json, type)

            return CachedSupplyBatch(
                supplyBatchesList = supplyBatchesList,
                lastUpdate = lastUpdate,
            )
        }

        fun isCacheValid(): Boolean {
            val lastUpdate = prefs.getLong(KEY_LAST_UPDATE, 0)
            return System.currentTimeMillis() - lastUpdate < SupplyBatchesConstants.CACHE_DURATION
        }

        fun clearCache() {
            prefs.edit { clear() }
        }
    }
