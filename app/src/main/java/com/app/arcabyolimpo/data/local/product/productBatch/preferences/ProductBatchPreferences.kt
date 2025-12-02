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

    fun saveProductBatchList(
        productBatchList: List<ProductBatch>,
    ) {
        prefs
            .edit {
                putString(ProductBatchPreferencesConstants.KEY_BATCH_CACHE, gson.toJson(productBatchList))
                    .putLong(ProductBatchPreferencesConstants.KEY_LAST_UPDATE, System.currentTimeMillis())
            }
    }

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

    fun isCacheValid(): Boolean {
        val lastUpdate = prefs.getLong(ProductBatchPreferencesConstants.KEY_LAST_UPDATE, 0)
        if (lastUpdate == 0L) return false

        return System.currentTimeMillis() - lastUpdate < ProductBatchPreferencesConstants.CACHE_DURATION
    }
    fun clearCache() {
        prefs.edit().clear().apply()
    }
}