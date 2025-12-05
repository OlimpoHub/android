package com.app.arcabyolimpo.data.local.product.productBatch.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.app.arcabyolimpo.data.local.product.productBatch.model.ProductBatchCache
import com.app.arcabyolimpo.data.local.product.productBatch.preferences.ProductBatchPreferencesConstants
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

/**
 * Preferences manager for caching product batch data.
 *
 * Handles saving, retrieving and validating cached product batch lists.
 * Ensures fast loading of batch information while minimizing network calls.
 */
@Singleton
class ProductBatchPreferences
@Inject
constructor(
    @ApplicationContext context: Context,
    private val gson: Gson,
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            ProductBatchPreferencesConstants.PREF_NAME,
            Context.MODE_PRIVATE,
        )

    /**
     * Saves a list of product batches to local cache.
     * Stores both the list and the timestamp of the update.
     */
    fun saveProductBatchList(
        productBatchList: List<ProductBatch>,
    ) {
        prefs.edit {
            putString(ProductBatchPreferencesConstants.KEY_BATCH_CACHE, gson.toJson(productBatchList))
            putLong(ProductBatchPreferencesConstants.KEY_LAST_UPDATE, System.currentTimeMillis())
        }
    }

    /**
     * Retrieves the cached product batch list if available.
     * Returns null if the list does not exist or is incomplete.
     */
    fun getProductBatchCache(): ProductBatchCache? {
        val json = prefs.getString(ProductBatchPreferencesConstants.KEY_BATCH_CACHE, null)
        val lastUpdate = prefs.getLong(ProductBatchPreferencesConstants.KEY_LAST_UPDATE, 0)

        if (json.isNullOrEmpty() || lastUpdate == 0L) return null

        val type = object : TypeToken<List<ProductBatch>>() {}.type
        val productBatchList: List<ProductBatch> = gson.fromJson(json, type)

        return ProductBatchCache(
            productBatchList = productBatchList,
            lastUpdate = lastUpdate,
        )
    }

    /**
     * Checks if the cached product batch data is still valid.
     * The validity period is defined in ProductBatchPreferencesConstants.
     */
    fun isCacheValid(): Boolean {
        val lastUpdate = prefs.getLong(ProductBatchPreferencesConstants.KEY_LAST_UPDATE, 0)
        if (lastUpdate == 0L) return false

        return System.currentTimeMillis() - lastUpdate <
                ProductBatchPreferencesConstants.CACHE_DURATION
    }

    /**
     * Clears all cached product batch data.
     */
    fun clearCache() {
        prefs.edit().clear().apply()
    }
}
